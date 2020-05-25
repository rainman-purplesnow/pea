package org.tieland.pea;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2020/5/24 10:53
 */
public class TimeUnitTest {

    @Test
    public void test(){
        System.out.println(TimeUnit.DAYS.convert(3600, TimeUnit.MINUTES));
    }

}
