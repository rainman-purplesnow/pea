package org.tieland.pea.spring.starter;

import org.apache.commons.collections.CollectionUtils;
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
import org.tieland.pea.core.util.Tuple2;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    public ConfigUtils configUtils(PeaConfig peaConfig){
        if(peaConfig == null || CollectionUtils.isEmpty(peaConfig.getGroups())){
            throw new ConfigException("PeaConfig not correct");
        }

        return new ConfigUtils(peaConfig);
    }

    @Bean
    public TieMessageDispatcher tieMessageDispatcher(List<TieDelayQueue> delayQueueList, ConfigUtils configUtils){
        Map<String, Tuple2<TieDelayQueue, DelayConfig>> delayQueueTupleMap = new ConcurrentHashMap<>(delayQueueList.size());
        delayQueueList.stream().forEach(tieDelayQueue -> {
            DelayConfig delayConfig = configUtils.get(tieDelayQueue.group());
            delayQueueTupleMap.put(tieDelayQueue.group(), new Tuple2<>(tieDelayQueue, delayConfig));
        });
        return new DefaultTieMessageDispatcher(delayQueueList, delayQueueTupleMap);
    }

}
