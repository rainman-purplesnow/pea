package org.tieland.pea.core;

import lombok.extern.slf4j.Slf4j;
import org.tieland.pea.core.util.Tuple2;
import org.tieland.pea.core.util.ValidatorUtils;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>默认延迟消息TieMessage Dispatcher</p>
 * @author zhouxiang
 * @date 2020/5/21 8:48
 */
@Slf4j
public class DefaultTieMessageDispatcher extends TieMessageDispatcher {

    private List<TieDelayQueue> delayQueueList;

    private TieMessageConverter messageConverter;

    private Map<String, Tuple2<TieDelayQueue, DelayConfig>> delayQueueTupleMap;

    public DefaultTieMessageDispatcher(List<TieDelayQueue> delayQueueList, Map<String, Tuple2<TieDelayQueue, DelayConfig>> delayQueueTupleMap){
        this(delayQueueList, new DefaultTieMessageConverter(), delayQueueTupleMap);
    }

    public DefaultTieMessageDispatcher(List<TieDelayQueue> delayQueueList, TieMessageConverter messageConverter, Map<String, Tuple2<TieDelayQueue, DelayConfig>> delayQueueTupleMap){
        this.delayQueueList = delayQueueList;
        this.messageConverter = messageConverter;
        this.delayQueueTupleMap = delayQueueTupleMap;
    }

    @Override
    public <T> void dispatch(TieMessage<T> message, long delay, TimeUnit timeUnit) throws DispatchException {
        ValidatorUtils.notNull(message, "message must not null");
        ValidatorUtils.notNull(timeUnit, "timeUnit must not null");
        ValidatorUtils.customize((delayTime)->delayTime>0, delay, "delay must positive");

        Tuple2<TieDelayQueue, DelayConfig> delayQueueTuple = delayQueueTupleMap.get(message.group());
        if(delayQueueTuple == null || delayQueueTuple.getT1() == null){
            throw new DispatchException(String.format(" no TieDelayQueue match with group:%s ", message.group()));
        }

        if(TimeUnit.SECONDS.convert(delay, timeUnit) > delayQueueTuple.getT2().getMaxDelaySeconds()){
            throw new DispatchException(String.format(" TieDelayQueue group:%s max delay seconds:%s ", message.group(),
                    delayQueueTuple.getT2().getMaxDelaySeconds()));
        }

        try{
            TieMessageContext messageContext = TieMessageContext.builder()
                    .messageId(message.getId())
                    .clazz(message.getPayload().getClass())
                    .group(message.group()).build();
            messageConverter.writeContext(message, messageContext);
            delayQueueTuple.getT1().offer(messageContext, delay, timeUnit);
            log.debug(" dispatch success. message:{} ", message);
        }catch (InterruptedException ex){
            throw new DispatchException("", ex);
        }
    }

    @Override
    public void cancel(CanceledTieMessage message) throws DispatchException {
        Tuple2<TieDelayQueue, DelayConfig> delayQueueTuple = delayQueueTupleMap.get(message.group());
        if(delayQueueTuple == null || delayQueueTuple.getT1() == null){
            throw new DispatchException(String.format(" no TieDelayQueue match with group:%s ", message.group()));
        }

        if(delayQueueTuple.getT1() == null){
            throw new DispatchException(String.format(" no TieDelayQueue match with group:%s ", message.group()));
        }

        try{
            boolean removed = delayQueueTuple.getT1().remove(message.getId());
            if(!removed){
                throw new DispatchException(String.format("TieMessage id:%s not exist", message.getId()));
            }
        }catch (Exception ex){
            throw new DispatchException(ex);
        }

    }
}
