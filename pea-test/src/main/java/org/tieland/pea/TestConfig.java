package org.tieland.pea;

import org.redisson.TieRedisson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.pea.core.TieDelayQueue;
import org.tieland.pea.spring.starter.PeaConfig;
import org.tieland.pea.spring.starter.TieMessageContainer;

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
    public TieMessageContainer tieMessageContainer(TieDelayQueue tieDelayQueue, TieDelayTask<TestVO> testDelayTask, PeaConfig config){
        return new TieMessageContainer(tieDelayQueue, testDelayTask, config);
    }


}
