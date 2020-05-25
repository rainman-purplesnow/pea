package org.tieland.pea.spring.starter;

import org.springframework.beans.BeanUtils;
import org.tieland.pea.core.DelayConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>PeaConfig转换为DelayConfig Utils</p>
 * @author zhouxiang
 * @date 2020/5/25 10:18
 */
public class ConfigUtils {

    private static Map<String, DelayConfig> configMap;

    protected ConfigUtils(PeaConfig peaConfig){
        configMap = new ConcurrentHashMap<>(peaConfig.getGroups().size());
        peaConfig.getGroups().forEach(groupedDelayConfig -> {
            DelayConfig delayConfig = new DelayConfig();
            BeanUtils.copyProperties(groupedDelayConfig, delayConfig);
            configMap.put(groupedDelayConfig.getName(), delayConfig);
        });
    }

    public DelayConfig get(String group){
        return configMap.get(group);
    }

}
