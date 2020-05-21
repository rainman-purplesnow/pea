package org.tieland.pea.spring.starter;

import lombok.Data;
import org.tieland.pea.core.DelayConfig;

/**
 * @author zhouxiang
 * @date 2020/5/20 17:04
 */
@Data
public class GroupedDelayConfig extends DelayConfig {

    private String name;

}
