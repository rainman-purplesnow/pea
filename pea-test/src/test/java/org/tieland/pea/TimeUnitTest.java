package org.tieland.pea;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2020/5/24 10:53
 */
@Slf4j
public class TimeUnitTest {

    @Test
    public void test(){
        Flux.defer(()-> Flux.just(2,3)).map(i->{
            log.info("i:{}", i);
            if(i==2){
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return 1/(i-1);
        }).retry(1, throwable -> {
            log.info("adafaf");
            return throwable instanceof Exception;
        }).timeout(Duration.ofSeconds(2))
                .subscribe(i->{
                    log.info("i::::{}", i);
                });
    }



}
