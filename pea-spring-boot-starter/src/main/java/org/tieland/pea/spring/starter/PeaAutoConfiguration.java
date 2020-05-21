package org.tieland.pea.spring.starter;

import org.redisson.Redisson;
import org.redisson.TieRedisson;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.pea.core.*;

import java.util.List;

/**
 * @author zhouxiang
 * @date 2020/5/20 16:41
 */
@Configuration
@EnableConfigurationProperties({PeaConfig.class})
@AutoConfigureAfter(RedissonAutoConfiguration.class)
public class PeaAutoConfiguration {

    @Bean
    public TieRedisson tieRedisson(RedissonClient redissonClient){
        return new TieRedisson((Redisson)redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean(TieMessageConverter.class)
    public TieMessageConverter tieMessageConverter(){
        return new DefaultTieMessageConverter();
    }

    @Bean
    public TieMessageDispatcher tieMessageDispatcher(List<TieDelayQueue> delayQueueList){
        DefaultTieMessageDispatcher dispatcher = new DefaultTieMessageDispatcher(delayQueueList);
        dispatcher.init();
        return dispatcher;
    }

}
