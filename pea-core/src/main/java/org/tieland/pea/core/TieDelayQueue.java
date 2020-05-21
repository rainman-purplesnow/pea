package org.tieland.pea.core;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2020/5/20 8:47
 */
public interface TieDelayQueue {

    /**
     * 所在group
     * @return
     */
    String group();

    /**
     * 入列
     * @param context
     * @param delay
     * @param timeUnit
     * @throws InterruptedException
     */
    void offer(TieMessageContext context, long delay, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 出列
     * @return
     * @throws InterruptedException
     */
    TieMessageContext take() throws InterruptedException;

    /**
     * 删除
     * @param id
     * @return
     */
    boolean remove(String id);

    /**
     * 大小
     * @return
     */
    Integer size();

    /**
     * 为空判断
     * @return
     */
    boolean isEmpty();

    /**
     * 释放
     * @param context
     * @throws InterruptedException
     */
    void release(TieMessageContext context) throws InterruptedException;

    /**
     * 重试
     * @param context
     * @throws InterruptedException
     */
    void retry(TieMessageContext context) throws InterruptedException;

}
