# pea
redis延迟队列

# 思路
参考Redisson中RedissonDelayedQueue
RedisTieDelayQueue存在丢失问题
原理：
queue入列时 id-->zset  data-->hash
超时后，zset和hash中删除相应数据，同时data-->queue  
丢失问题，突然宕机或者app奔溃，会造成丢失，需要从业务角度控制重发

# 延伸
如果从redis端考虑丢失问题，则需要改进
入队：id->zset  data->hash
超时后：id->idzset data->queue  从zset删除id
如果成功则从 data和idzset中删除
如果失败 也是从 data和idzset中删除
如果异常 也是从 data和idzset中删除
如果突然宕机，暂时脏数据  idzset和hash中都存在，需要monitor检测，超过一定时间的message重发

