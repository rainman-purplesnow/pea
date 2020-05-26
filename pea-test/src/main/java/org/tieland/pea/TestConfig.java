package org.tieland.pea;

import org.redisson.TieRedisson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.pea.core.DefaultTieMessageContainer;
import org.tieland.pea.core.TieDelayQueue;
import org.tieland.pea.core.TieMessageContainer;
import org.tieland.pea.spring.starter.ConfigUtils;
import org.tieland.pea.web.TestVO;

/**
 * @author zhouxiang
 * @date 2020/5/20 17:17
 */
@Configuration
public class TestConfig {

    @Bean
    public TieDelayQueue tieDelayQueue(TieRedisson tieRedisson){
        return tieRedisson.getTieDelayQueue("11");
    }

    @Bean(
            initMethod = "start",
            destroyMethod = "stop"
    )
    public TieMessageContainer tieMessageContainer(TieDelayQueue tieDelayQueue, TestDelayTask testDelayTask, ConfigUtils configUtils){
        return new DefaultTieMessageContainer<>(tieDelayQueue, testDelayTask, configUtils.get(tieDelayQueue.group()));
    }


}
