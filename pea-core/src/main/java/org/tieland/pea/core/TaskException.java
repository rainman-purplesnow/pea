package org.tieland.pea.core;

/**
 * @author zhouxiang
 * @date 2020/5/25 16:12
 */
public class TaskException extends RuntimeException {

    public TaskException(Throwable throwable){
        super(throwable);
    }

    public TaskException(String message, Throwable throwable){
        super(message, throwable);
    }

}
