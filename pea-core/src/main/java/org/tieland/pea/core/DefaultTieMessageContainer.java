package org.tieland.pea.core;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxiang
 * @date 2020/5/24 18:02
 */
@Slf4j
public class DefaultTieMessageContainer<T> implements TieMessageContainer {

    private TieDelayQueue delayQueue;

    private DelayTask<T> delayTask;

    private DefaultTieMessageListener<T> messageListener;

    private DelayConfig config;

    public DefaultTieMessageContainer(TieDelayQueue delayQueue, DelayTask<T> delayTask, DelayConfig config){
        this.delayQueue = delayQueue;
        this.delayTask = delayTask;
        this.config = config;
    }

    @Override
    public void start() {
        log.info(" TieMessageContainer starting...... ");
        messageListener = new DefaultTieMessageListener(delayQueue, delayTask, config);
        messageListener.listen();
        log.info(" TieMessageContainer started.group:{}", delayQueue.group());
    }

    @Override
    public void stop() {
        log.info(" TieMessageContainer stopping...... ");
        messageListener.stop();
        log.info(" TieMessageContainer stopped.group:{}", delayQueue.group());
    }
}
