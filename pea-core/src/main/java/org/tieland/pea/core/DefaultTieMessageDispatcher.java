package org.tieland.pea.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2020/5/21 8:48
 */
@Slf4j
public class DefaultTieMessageDispatcher extends TieMessageDispatcher {

    private List<TieDelayQueue> delayQueueList;

    private Map<String, TieDelayQueue> delayQueueMap = new ConcurrentHashMap<>();

    private TieMessageConverter messageConverter = new DefaultTieMessageConverter();

    public DefaultTieMessageDispatcher(List<TieDelayQueue> delayQueueList){
        this.delayQueueList = delayQueueList;
    }

    public void init(){
        if(CollectionUtils.isEmpty(delayQueueList)){
            log.info(" no TieDelayQueue find. ");
            return;
        }

        delayQueueList.stream().forEach(delayQueue->delayQueueMap.put(delayQueue.group(), delayQueue));
        log.info(" DefaultTieMessageDispatcher init success. ");
    }

    @Override
    public <T> void dispatch(TieMessage<T> message, long delay, TimeUnit timeUnit) throws DispatchException {
        TieDelayQueue delayQueue = delayQueueMap.get(message.group());
        if(delayQueue == null){
            throw new DispatchException(String.format(" no TieDelayQueue match with group:%s ", message.group()));
        }

        try{
            TieMessageContext messageContext = TieMessageContext.builder().clazz(message.getPayload().getClass()).build();
            messageConverter.writeContext(message, messageContext);
            delayQueue.offer(messageContext, delay, timeUnit);
            log.debug(" dispatch success. message:{} ", message);
        }catch (InterruptedException ex){
            throw new DispatchException("", ex);
        }
    }

    @Override
    public void cancel(TieMessage message) throws DispatchException {
        TieDelayQueue delayQueue = delayQueueMap.get(message.group());
        if(delayQueue == null){
            throw new DispatchException(String.format(" no TieDelayQueue match with group:%s ", message.group()));
        }

        try{
            boolean removed = delayQueue.remove(message.getId());
            if(!removed){
                throw new DispatchException(String.format("TieMessage id:%s not exist", message.getId()));
            }
        }catch (Exception ex){
            throw new DispatchException(ex);
        }

    }
}
