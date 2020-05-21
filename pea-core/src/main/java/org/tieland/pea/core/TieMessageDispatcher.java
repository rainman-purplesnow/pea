package org.tieland.pea.core;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2020/5/19 13:41
 */
public abstract class TieMessageDispatcher {

    /**
     * 分发TieMessage到对应
     * @param message
     * @param delay
     * @param timeUnit
     * @param <T>
     * @throws DispatchException
     */
    public abstract <T> void dispatch(TieMessage<T> message, long delay, TimeUnit timeUnit) throws DispatchException;



    public abstract void cancel(TieMessage message) throws DispatchException;
}
