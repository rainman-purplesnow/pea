package org.tieland.pea.core.util;

/**
 * @author zhouxiang
 * @date 2020/5/25 9:13
 */
public class Tuple2<E, F> {

    private E t1;

    private F t2;

    public Tuple2(E e, F f){
        this.t1 = e;
        this.t2 = f;
    }

    public E getT1(){
        return t1;
    }

    public F getT2(){
        return t2;
    }

}
