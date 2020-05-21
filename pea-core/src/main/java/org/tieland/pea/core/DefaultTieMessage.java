package org.tieland.pea.core;

import lombok.Builder;

import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2020/5/20 9:04
 */
@Builder
public class DefaultTieMessage<T> implements TieMessage<T>, Serializable {

    private static final long serialVersionUID = -2099361280227412101L;

    private final T payload;

    private final String id;

    private final String group;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public T getPayload() {
        return this.payload;
    }

    @Override
    public String group() {
        return this.group;
    }
}
