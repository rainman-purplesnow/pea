package org.tieland.pea;

import lombok.Data;

/**
 * @author zhouxiang
 * @date 2020/5/20 17:19
 */
@Data
public class TestVO {

    private String id;

    private String data;

    public TestVO(){

    }

    public TestVO(String id, String data){
        this.id = id;
        this.data = data;
    }

}
