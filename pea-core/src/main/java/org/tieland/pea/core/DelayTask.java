package org.tieland.pea.core;

import lombok.extern.slf4j.Slf4j;

/**
 * 延迟业务Task
 * @author zhouxiang
 * @date 2020/5/20 10:13
 */
@Slf4j
public abstract class DelayTask<T> {

    public enum TaskResult {
        SUCCEED, FAILED
    }

    public final void execute(T payload){

        TaskResult taskResult;

        try{
            log.debug("begin handle. payload:{}", payload);
            taskResult = handle(payload);
            log.debug("end handle. payload:{}, result:{}", payload, taskResult);
        }catch (Exception ex){
            log.error(" handle occur exception. payload:{} ", payload, ex);
            error(payload, ex);
            return;
        }

        if(TaskResult.SUCCEED == taskResult){
            succeed(payload);
            log.debug(" do succeed. ");
            return;
        }

        if(TaskResult.FAILED == taskResult){
            failed(payload);
            log.debug(" do failed. ");
        }
    }

    /**
     * 实际业务处理逻辑
     * @param payload
     * @return
     */
    protected abstract TaskResult handle(T payload);

    /**
     * 业务处理成功之后
     * @param payload
     */
    protected abstract void succeed(T payload);

    /**
     * 业务处理失败之后
     * @param payload
     */
    protected abstract void failed(T payload);

    /**
     * 业务处理中出现异常处理
     * @param payload
     * @param ex
     */
    protected abstract void error(T payload, Exception ex);
}