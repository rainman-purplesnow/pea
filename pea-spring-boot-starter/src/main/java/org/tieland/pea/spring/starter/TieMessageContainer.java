package org.tieland.pea.spring.starter;

import lombok.extern.slf4j.Slf4j;
import org.tieland.pea.core.DefaultTieMessageListener;
import org.tieland.pea.core.TieDelayQueue;
import org.tieland.pea.core.DelayTask;
import java.util.Optional;

/**
 * @author zhouxiang
 * @date 2020/5/21 10:40
 */
@Slf4j
public class TieMessageContainer<T> {

    private TieDelayQueue delayQueue;

    private DelayTask<T> delayTask;

    private PeaConfig config;

    private DefaultTieMessageListener<T> messageListener;

    public TieMessageContainer(TieDelayQueue delayQueue, DelayTask<T> delayTask, PeaConfig config){
        this.delayQueue = delayQueue;
        this.delayTask = delayTask;
        this.config = config;
    }

    public void start(){
        log.info(" TieMessageContainer starting...... ");
        Optional<GroupedDelayConfig> delayConfig = config.getGroups().stream().filter(groupedDelayConfig ->
                groupedDelayConfig.getName().equals(delayQueue.group())).findFirst();
        messageListener = new DefaultTieMessageListener(delayQueue, delayTask,
                delayConfig.orElseThrow(()->new ConfigException(String.format("no group:%s config", delayQueue.group()))));
        messageListener.listen();
        log.info(" TieMessageContainer started.group:{}", delayQueue.group());
    }

    public void stop(){
        log.info(" TieMessageContainer stopping...... ");
        messageListener.stop();
        log.info(" TieMessageContainer stopped...... ");
    }

}
