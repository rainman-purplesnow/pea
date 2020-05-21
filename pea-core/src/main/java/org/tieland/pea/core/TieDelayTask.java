package org.tieland.pea.core;

/**
 * @author zhouxiang
 * @date 2020/5/20 10:50
 */
public abstract class TieDelayTask<T> extends DelayTask<T> {

    @Override
    protected void succeed(T payload) {
    }

    @Override
    protected void failed(T payload) {
    }

    @Override
    protected void error(T payload, Exception ex) {
    }
}
