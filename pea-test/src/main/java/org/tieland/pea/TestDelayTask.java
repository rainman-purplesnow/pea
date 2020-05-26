package org.tieland.pea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tieland.pea.core.TaskContext;
import org.tieland.pea.core.TaskContextHolder;
import org.tieland.pea.core.TieDelayTask;
import org.tieland.pea.web.TestVO;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2020/5/20 17:17
 */
@Slf4j
@Component
public class TestDelayTask extends TieDelayTask<TestVO> {

    @Override
    protected TaskResult handle(TestVO payload) throws Exception {
        log.info("payload:{}", payload);
        if(payload.getId().equals("20")){
            throw new Exception();
        }else{
            try {
                log.info("{}", Thread.currentThread().getName());
                TaskContextHolder.set(new TaskContext(Thread.currentThread()));
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
        return TaskResult.SUCCEED;
    }

    @Override
    protected void interrupt(TaskContext taskContext){
        ((Thread)taskContext.getTarget()).interrupt();
    }

}
