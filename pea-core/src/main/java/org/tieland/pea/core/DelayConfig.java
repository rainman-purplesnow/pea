package org.tieland.pea.core;

import lombok.Data;

/**
 * @author zhouxiang
 * @date 2020/5/21 10:30
 */
@Data
public class DelayConfig {

    public static final Integer DEFAULT_TASK_TIMEOUT_SECONDS = 10;

    private String corn;

    private int concurrent = 5;

    private long maxDelaySeconds;

    private int listenerDelaySeconds;

    private int taskTimeoutSeconds = 30;

}
