package org.tieland.pea.core;

import java.util.concurrent.TimeUnit;

/**
 * 延迟消息TieMessage Dispatcher
 * @author zhouxiang
 * @date 2020/5/19 13:41
 */
public abstract class TieMessageDispatcher {

    /**
     * 分发TieMessage到对应Queue
     * 根据group匹配Queue
     * @param message
     * @param delay
     * @param timeUnit
     * @param <T>
     * @throws DispatchException
     */
    public abstract <T> void dispatch(TieMessage<T> message, long delay, TimeUnit timeUnit) throws DispatchException;

    /**
     * 取消TieMessage
     * 当TieMessage还未发送时，可以取消；如果TieMessage已经发送或者不存在，会报 DispatchException
     * @param message
     * @throws DispatchException
     */
    public abstract void cancel(CanceledTieMessage message) throws DispatchException;
}
