package org.tieland.pea.core;

/**
 *
 * @author zhouxiang
 * @date 2020/5/20 9:33
 */
public class ConverterException extends RuntimeException {

    public ConverterException(String message){
        super(message);
    }

    public ConverterException(Throwable throwable){
        super(throwable);
    }

    public ConverterException(String message, Throwable throwable){
        super(message, throwable);
    }

}
