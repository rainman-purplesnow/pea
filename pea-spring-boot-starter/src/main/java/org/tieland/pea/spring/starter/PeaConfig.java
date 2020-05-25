package org.tieland.pea.spring.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

/**
 * <p>pea配置</p>
 * @author zhouxiang
 * @date 2020/5/20 16:57
 */
@Data
@ConfigurationProperties(prefix = "config.common.pea")
public class PeaConfig {

    /**
     * 分组配置
     */
    private List<GroupDelayConfig> groups;

}


