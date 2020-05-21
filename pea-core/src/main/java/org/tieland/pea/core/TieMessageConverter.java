package org.tieland.pea.core;

/**
 * @author zhouxiang
 * @date 2020/5/20 8:54
 */
public interface TieMessageConverter {

    /**
     * 从MessageContext提取转换TieMessage
     * @param context
     * @return
     * @throws ConverterException
     */
    TieMessage fromContext(TieMessageContext context) throws ConverterException;

    /**
     * TieMessage写入MessageContext
     * @param message
     * @param context
     * @param <T>
     * @throws ConverterException
     */
    void writeContext(TieMessage message, TieMessageContext context) throws ConverterException;
}
