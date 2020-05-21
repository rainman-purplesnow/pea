package org.tieland.pea.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouxiang
 * @date 2020/5/19 14:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TieMessageContext {

    private Class clazz;

    private String messageId;

    private String body;

    private String corn;

}
