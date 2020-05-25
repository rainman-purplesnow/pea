# pea
redis延迟队列

# 思路
参考Redisson中RedissonDelayedQueue
RedisTieDelayQueue存在丢失问题
原理：
queue入列时 id-->zset  data-->hash
超时后，zset和hash中删除相应数据，同时data-->queue  
丢失问题，需要从业务角度控制重发



