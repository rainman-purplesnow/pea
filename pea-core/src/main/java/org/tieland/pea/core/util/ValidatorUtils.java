package org.tieland.pea.core.util;

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

}
