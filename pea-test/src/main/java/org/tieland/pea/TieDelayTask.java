package org.tieland.pea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tieland.pea.core.TieDelayTask;
import org.tieland.pea.core.TieMessage;

/**
 * @author zhouxiang
 * @date 2020/5/20 17:17
 */
@Slf4j
@Component
public class TieDelayTask<TestVO> extends TieDelayTask<TestVO> {

    @Override
    protected TaskResult handle(TieMessage<TestVO> message) {
        log.info("message:{}", message);
        return TaskResult.SUCCEED;
    }
}
