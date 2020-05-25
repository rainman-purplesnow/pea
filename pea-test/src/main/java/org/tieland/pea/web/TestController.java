package org.tieland.pea.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tieland.pea.TestVO;
import org.tieland.pea.core.DefaultTieMessage;
import org.tieland.pea.core.TieMessageContainer;
import org.tieland.pea.core.TieMessageDispatcher;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2020/5/21 10:08
 */
@RestController
public class TestController {

    @Autowired
    private TieMessageDispatcher tieMessageDispatcher;

    @Autowired
    private TieMessageContainer tieMessageContainer;

    @GetMapping("/test")
    public String test(@RequestParam("id")String id, @RequestParam("delay") Long delay){
        tieMessageDispatcher.dispatch(DefaultTieMessage.builder().group("11").id(id).payload(new TestVO(id,"data")).build(), delay, TimeUnit.SECONDS);
        return "success";
    }

    @GetMapping("/stop")
    public String stop(){
        tieMessageContainer.stop();
        return "success";
    }
}
