package org.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RFuture;
import org.redisson.api.RTopic;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.protocol.RedisCommands;
import org.tieland.pea.core.TieMessageContext;
import org.tieland.pea.core.TieDelayQueue;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2020/5/20 8:52
 */
@Slf4j
public class RedisTieDelayQueue extends RedissonObject implements TieDelayQueue {

    public static final String DEFAULT_PREFIX = "org:tieland:pea";
    private final String group;
    private final QueueTransferService queueTransferService;
    private final String channelName;
    private final String timeoutZSetName;
    private final String dataSetName;
    private final String expiredQueueName;
    private final RBlockingQueue<TieMessageContext> expiredQueue;

    public RedisTieDelayQueue(Redisson redisson, QueueTransferService queueTransferService, Codec codec, String group){
        this(redisson, queueTransferService, codec, group, DEFAULT_PREFIX);
    }

    public RedisTieDelayQueue(Redisson redisson, QueueTransferService queueTransferService, Codec codec,
                              String group, String prefix) {
        super(codec, redisson.getCommandExecutor(), group);
        this.group = group;
        this.channelName = prefixName(prefix+":"+"delay_queue_channel", group);
        this.dataSetName = prefixName(prefix+":"+"delay_data_set", group);
        this.expiredQueueName = prefixName(prefix+":"+"delay_queue", group);
        this.timeoutZSetName = prefixName(prefix+":"+"delay_queue_timeout", group);
        this.expiredQueue = new RedissonBlockingQueue(codec, commandExecutor, expiredQueueName, redisson);

        QueueTransferTask task = new QueueTransferTask(commandExecutor.getConnectionManager()) {


            @Override
            protected RFuture<Long> pushTaskAsync() {
                return commandExecutor.evalWriteAsync(getName(), LongCodec.INSTANCE, RedisCommands.EVAL_LONG,
                        "local expiredValues = redis.call('zrangebyscore', KEYS[2], 0, ARGV[1], 'limit', 0, ARGV[2]); "
                                + "if #expiredValues > 0 then "
                                    + "for i, v in ipairs(expiredValues) do "
                                        + "local value = redis.call('hget', KEYS[3], v);"
                                        + "local randomId, message = struct.unpack('dLc0', value);"
                                        + "redis.call('rpush', KEYS[1], message);"
                                        + "redis.call('hdel', KEYS[3], v);"
                                    + "end; "
                                    + "redis.call('zrem', KEYS[2], unpack(expiredValues));"
                                + "end; "
                                + "local v = redis.call('zrange', KEYS[2], 0, 0, 'WITHSCORES'); "
                                + "if v[1] ~= nil then "
                                    + "return v[2]; "
                                + "end "
                                + "return nil;",
                        Arrays.<Object>asList(expiredQueueName, timeoutZSetName, dataSetName),
                        System.currentTimeMillis(), 100);
            }

            @Override
            protected RTopic getTopic() {
                return new RedissonTopic(LongCodec.INSTANCE, commandExecutor, channelName);
            }
        };

        queueTransferService.schedule(expiredQueueName, task);

        this.queueTransferService = queueTransferService;
    }

    @Override
    public String group() {
        return group;
    }

    @Override
    public void offer(TieMessageContext context, long delay, TimeUnit timeUnit) throws InterruptedException {
        get(offerAsync(context, delay, timeUnit));
    }

    /**
     * 异步
     * @param context
     * @param delay
     * @param timeUnit
     * @return
     */
    private RFuture<Void> offerAsync(TieMessageContext context, long delay, TimeUnit timeUnit){
        if (delay < 0) {
            throw new IllegalArgumentException("Delay can't be negative");
        }

        long delayInMs = timeUnit.toMillis(delay);
        long timeout = System.currentTimeMillis() + delayInMs;

        long randomId = ThreadLocalRandom.current().nextLong();
        return commandExecutor.evalWriteAsync(getName(), codec, RedisCommands.EVAL_VOID,
                          "local value = struct.pack('dLc0', tonumber(ARGV[2]), string.len(ARGV[3]), ARGV[3]);"
                        + "redis.call('zadd', KEYS[1], ARGV[1], ARGV[4]);"
                        + "redis.call('hset', KEYS[2], ARGV[4], value);"
                        + "local v = redis.call('zrange', KEYS[1], 0, 0); "
                        + "if v[1] == ARGV[4] then "
                            + "redis.call('publish', KEYS[3], ARGV[1]); "
                        + "end;",
                Arrays.<Object>asList(timeoutZSetName, dataSetName, channelName),
                timeout, randomId, encode(context), context.getMessageId());
    }

    @Override
    public TieMessageContext take() throws InterruptedException {
        return expiredQueue.take();
    }

    @Override
    public boolean remove(String id) {
        return get(removeAsync(id));
    }

    /**
     * 异步删除
     * @param id
     * @return
     */
    private RFuture<Boolean> removeAsync(String id){
        return commandExecutor.evalWriteAsync(getName(), codec, RedisCommands.EVAL_BOOLEAN,
                "local s = redis.call('hget', KEYS[1], ARGV[1]);"
                        + "if s ~= nil then "
                        +   "redis.call('zrem', KEYS[2], ARGV[1]);"
                        +   "redis.call('hdel', KEYS[1], ARGV[1]);"
                        +   "return 1;"
                        + "end; "
                        + "return 0;",
                Arrays.<Object>asList(dataSetName, timeoutZSetName), id);
    }

    @Override
    public Integer size() {
        return get(sizeAsync());
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void release(TieMessageContext context) throws InterruptedException {
        //
    }

    /**
     * 异步统计size
     * @return
     */
    private RFuture<Integer> sizeAsync() {
        return commandExecutor.readAsync(getName(), codec, RedisCommands.HLEN, dataSetName);
    }

}
