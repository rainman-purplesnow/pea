package org.tieland.pea.core;

import lombok.Data;

/**
 * <p>Delay配置</p>
 * @author zhouxiang
 * @date 2020/5/21 10:30
 */
@Data
public class DelayConfig {

    /**
     * 默认Task并发度，10个线程
     */
    public static final int DEFAULT_CONCURRENT = 10;

    /**
     * 默认最大可延迟时间为1h
     */
    public static final long DEFAULT_MAX_DELAY_SECONDS = 3600;

    /**
     * 默认消息Task timeout 10s
     */
    public static final int DEFAULT_TASK_TIMEOUT_SECONDS = 10;

    /**
     * 默认listener延后10s启动
     */
    public static final int DEFAULT_LISTENER_DELAY_SECONDS = 10;

    /**
     * Task并发度，并行处理延迟消息的能力
     */
    private int concurrent = DEFAULT_CONCURRENT;

    /**
     * 可允许的最大延迟时间
     */
    private long maxDelaySeconds = DEFAULT_MAX_DELAY_SECONDS;

    /**
     * 消息listener延迟启动时间
     */
    private int listenerDelaySeconds = DEFAULT_LISTENER_DELAY_SECONDS;

    /**
     * 延迟Task 中心超时时间
     */
    private int taskTimeoutSeconds = DEFAULT_TASK_TIMEOUT_SECONDS;

}
