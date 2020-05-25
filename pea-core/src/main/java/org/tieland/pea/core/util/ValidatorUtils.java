package org.tieland.pea.core.util;

import java.util.function.Predicate;

/**
 * @author zhouxiang
 * @date 2020/5/19 11:34
 */
public final class ValidatorUtils {

    private ValidatorUtils(){
        //
    }

    /**
     * not null判断
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message){
        if(object == null){
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 自定义validator
     * @param predicate
     * @param data
     * @param message
     * @param <T>
     */
    public static <T> void customize(Predicate<T> predicate, T data, String message){
        if(!predicate.test(data)){
            throw new IllegalArgumentException(message);
        }
    }

}
