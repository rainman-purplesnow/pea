package org.tieland.pea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tieland.pea.core.TieDelayTask;

/**
 * @author zhouxiang
 * @date 2020/5/20 17:17
 */
@Slf4j
@Component
public class TestDelayTask<TestVO> extends TieDelayTask<TestVO> {

    @Override
    protected TaskResult handle(TestVO payload) {
        log.info("payload:{}", payload);
        return TaskResult.SUCCEED;
    }
}
