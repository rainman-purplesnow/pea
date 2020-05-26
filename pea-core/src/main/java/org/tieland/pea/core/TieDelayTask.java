package org.tieland.pea.core;

/**
 * <p>abstract 延迟业务Task</p>
 * @author zhouxiang
 * @date 2020/5/20 10:50
 */
public abstract class TieDelayTask<T> extends DelayTask<T> {

    /**
     * 默认业务处理成功之后
     * @param payload
     */
    @Override
    protected void succeed(T payload) {
    }

    /**
     * 默认业务处理失败之后
     * @param payload
     */
    @Override
    protected void failed(T payload) {
    }

    /**
     * 默认业务处理中出现异常处理
     * @param payload
     * @param ex
     */
    @Override
    protected void error(T payload, Exception ex) {
    }

    /**
     * 默认业务处理超时interrupt
     */
    @Override
    protected void interrupt(TaskContext taskContext){
        //
    }
}
