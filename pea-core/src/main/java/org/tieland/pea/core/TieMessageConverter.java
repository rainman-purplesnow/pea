package org.tieland.pea.core;

/**
 * @author zhouxiang
 * @date 2020/5/20 8:54
 */
public interface TieMessageConverter {

    /**
     * 从MessageContext提取转换TieMessage
     * @param context
     * @param <T>
     * @return
     * @throws ConverterException
     */
    <T> TieMessage<T> fromContext(TieMessageContext context) throws ConverterException;

    /**
     * TieMessage写入MessageContext
     * @param message
     * @param context
     * @throws ConverterException
     */
    void writeContext(TieMessage message, TieMessageContext context) throws ConverterException;
}
