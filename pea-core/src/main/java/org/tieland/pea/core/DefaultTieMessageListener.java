package org.tieland.pea.core;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;

/**
 * <p>默认延迟消息Listener</>
 * <p>TieMessageListener与TieDelayQueue、TieDelayTask绑定</p>
 * @author zhouxiang
 * @date 2020/5/20 9:47
 */
@Slf4j
public class DefaultTieMessageListener<T> implements TieMessageListener {

    private final TieDelayQueue delayQueue;

    private volatile boolean running = false;

    private final DelayConfig config;

    private TieMessageConverter messageConverter = new DefaultTieMessageConverter();

    private DelayTask<T> delayTask;

    private ThreadPoolExecutor threadPoolExecutor;

    private TieMessageListenerAsyncStarter asyncStarter;

    public DefaultTieMessageListener(TieDelayQueue delayQueue, DelayTask<T> delayTask, DelayConfig config){
        this(delayQueue, delayTask, config, new ThreadPoolExecutor(config.getConcurrent(), config.getConcurrent(),
                1, TimeUnit.HOURS, new LinkedBlockingQueue<>(100)));
    }

    public DefaultTieMessageListener(TieDelayQueue delayQueue, DelayTask<T> delayTask, DelayConfig config, ThreadPoolExecutor threadPoolExecutor){
        this.delayQueue = delayQueue;
        this.config = config;
        this.threadPoolExecutor = threadPoolExecutor;
        this.delayTask = delayTask;
    }

    public void setMessageConverter(TieMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public void listen() {
        String threadName = "TieMessageListener-"+System.identityHashCode(this);
        asyncStarter = new TieMessageListenerAsyncStarter(threadName);
        asyncStarter.start();
        Runtime.getRuntime().addShutdownHook(new Thread(()->stop()));
        log.debug(" TieMessageListener is starting. ");
    }

    /**
     * 异步启动
     */
    private void start(){
        running = true;
    }

    /**
     * 优雅停止
     */
    public void stop() {
        running = false;

        try {
            log.debug(" TieMessageListenerAsyncStarter stopping. ");
            asyncStarter.join();
            log.debug(" TieMessageListenerAsyncStarter is stopped. ");
        } catch (InterruptedException e) {
            log.error("", e);
        }

        log.debug(" ThreadPoolExecutor start shutdown. ");
        threadPoolExecutor.shutdown();
        log.debug(" ThreadPoolExecutor already shutdown. ");
    }

    /**
     * 延迟消息异步starter
     */
    class TieMessageListenerAsyncStarter extends Thread {

        TieMessageListenerAsyncStarter(String name){
            super(name);
            setDaemon(true);
        }

        @Override
        public void run(){
            if(!running){
                try {
                    log.debug(" TieMessageListener will start in {} seconds. ", config.getListenerDelaySeconds());
                    TimeUnit.SECONDS.sleep(config.getListenerDelaySeconds());
                } catch (InterruptedException e) {
                    log.error("", e);
                }

                DefaultTieMessageListener.this.start();
                log.debug(" TieMessageListener is started. ");
            }

            while(running){
                try{
                    TieMessageContext context = delayQueue.take();
                    log.debug(" delay queue take message context.{} ", context);
                    threadPoolExecutor.submit(()->execute(context));
                }catch (Exception ex){
                    log.error("", ex);
                }
            }
        }

        /**
         * 实际执行delayTask
         * @param context TieMessage上下文
         */
        private void execute(TieMessageContext context){
            boolean release = true;
            Thread worker = Thread.currentThread();

            try{
                TieMessage<T> message = messageConverter.fromContext(context);
                log.debug("message:{}", message);

                Mono<Void> mono = Mono.defer(()->Mono.fromRunnable(()->{
                    log.debug(" delay task execute start ");
                    delayTask.execute(message.getPayload());
                    log.debug(" delay task execute end ");
                }));

                //具备执行timeout
                if(config.getTaskTimeoutSeconds()>0){
                    mono = mono.timeout(Duration.ofSeconds(config.getTaskTimeoutSeconds()), Mono.error(new TimeoutException("DelayTask execute timeout")))
                                .doOnError(throwable -> {
                                    if(throwable instanceof  TimeoutException){
                                        log.debug(" delay task interrupt start ");
                                        delayTask.interrupt(TaskContextHolder.get(worker));
                                        log.debug(" delay task interrupt end ");
                                    }
                                });
                }

                if(config.getRetriesOnError()>0){
                    mono = mono.retry(config.getRetriesOnError());
                }

                mono.doOnError(error->
                    log.error("context:{}", context, error)
                ).subscribe();
            } catch (ConverterException ex){
                release = false;
                log.error(" message converter error. context:{} ", context, ex);
            } catch (Exception ex){
                release = true;
                log.error(" delay task error. context:{} ", context, ex);
            } finally {
                if(context != null){
                    try {
                        if(release){
                            delayQueue.release(context);
                            log.debug(" release context.{} ", context);
                        }
                    }catch (Exception ex){
                        log.error(" delay queue release error. context:{} ", context, ex);
                    }
                }

                //释放
                TaskContextHolder.clear(worker);
            }
        }
    }
}
