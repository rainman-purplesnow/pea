package org.tieland.pea.core;

/**
 * @author zhouxiang
 * @date 2020/5/21 9:21
 */
public class DispatchException extends RuntimeException {

    public DispatchException(String message){
        super(message);
    }

    public DispatchException(String message, Throwable throwable){
        super(message, throwable);
    }

    public DispatchException(Throwable throwable){
        super(throwable);
    }

}
