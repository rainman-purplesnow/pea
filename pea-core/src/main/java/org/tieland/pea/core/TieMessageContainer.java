package org.tieland.pea.core;

/**
 * <p>每个TieMessageListener对应有一个Container管理</p>
 * @author zhouxiang
 * @date 2020/5/24 18:01
 */
public interface TieMessageContainer {

    /**
     * 启动container
     */
    void start();

    /**
     * 停止container
     */
    void stop();
}
