package org.redisson;


import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.tieland.pea.core.TieMessageContext;
import org.tieland.pea.core.TieDelayQueue;

/**
 * 扩展redisson支持自定义延迟队列
 * @author zhouxiang
 * @date 2020/5/20 13:30
 */
public class TieRedisson {

    private Redisson redisson;

    private final QueueTransferService queueTransferService = new QueueTransferService();

    public TieRedisson(Redisson redisson) {
        this.redisson = redisson;
    }

    public TieDelayQueue getTieDelayQueue(String group){
        return getTieDelayQueue(new JsonJacksonCodec(), group);
    }

    public TieDelayQueue getTieDelayQueue(Codec codec, String group){
        return new RedisTieDelayQueue(redisson, queueTransferService, codec, group);
    }

}
