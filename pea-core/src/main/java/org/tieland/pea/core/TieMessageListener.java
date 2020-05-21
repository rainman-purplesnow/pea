package org.tieland.pea.core;

/**
 * 延迟消息Listener
 * @author zhouxiang
 * @date 2020/5/20 9:45
 */
public interface TieMessageListener {

    /**
     * 监听延迟TieMessage
     */
    void listen();

}
