package org.tieland.pea.core;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author zhouxiang
 * @date 2020/5/24 18:28
 */
@Builder
@ToString
@EqualsAndHashCode
public class CanceledTieMessage {

    /**
     * id
     */
    private String id;

    /**
     * 所属group
     */
    private String group;

    public String getId() {
        return id;
    }

    public String group() {
        return group;
    }
}
