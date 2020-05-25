package org.tieland.pea.spring.starter;

import lombok.Data;
import org.tieland.pea.core.DelayConfig;

/**
 * <p>区分group后 Delay配置</p>
 * @author zhouxiang
 * @date 2020/5/24 18:06
 */
@Data
public class GroupDelayConfig extends DelayConfig {

    /**
     * group名称
     */
    private String name;

}
