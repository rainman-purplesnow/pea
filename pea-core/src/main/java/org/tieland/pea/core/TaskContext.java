package org.tieland.pea.core;

import lombok.ToString;

/**
 * @author zhouxiang
 * @date 2020/5/25 17:37
 */
@ToString
public class TaskContext<T> {

    private T target;

    public TaskContext(T target){
        this.target = target;
    }

    public T getTarget() {
        return target;
    }
}
