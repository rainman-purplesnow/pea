package org.tieland.pea.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouxiang
 * @date 2020/5/25 17:36
 */
public class TaskContextHolder {

    private static final Map<Thread, TaskContext> CONTEXT_HOLDER = new ConcurrentHashMap<>();

    private TaskContextHolder(){
        //
    }

    public static void set(TaskContext taskContext){
        CONTEXT_HOLDER.put(Thread.currentThread(), taskContext);
    }

    public static TaskContext get(){
        return get(Thread.currentThread());
    }

    public static TaskContext get(Thread thread){
        return CONTEXT_HOLDER.get(thread);
    }

    public static void clear(){
        clear(Thread.currentThread());
    }

    public static void clear(Thread thread){
        CONTEXT_HOLDER.remove(thread);
    }

}
