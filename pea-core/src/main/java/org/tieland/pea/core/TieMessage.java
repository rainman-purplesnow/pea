package org.tieland.pea.core;

/**
 * 延迟消息抽象
 * @author zhouxiang
 * @date 2020/5/20 9:03
 */
public interface TieMessage<T> {

    /**
     * 消息id
     * @return
     */
    String getId();

    /**
     * 消息内容
     * @return
     */
    T getPayload();

    /**
     * 所属group
     * @return
     */
    String group();

}
