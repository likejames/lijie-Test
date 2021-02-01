package lijie.test.RedissonTest;

import redis.clients.jedis.Jedis;

public class Test {
    public static void main(String[] args) {
        String key = "test12";
        //加锁
        DistributedRedisLock.acquire(key);
        //执行具体业务逻辑
        Test.xx1();
        //释放锁
        DistributedRedisLock.release(key);
        //返回结果
    }

    public static void xx() {
        Jedis jedis = new Jedis("114.116.230.141", 6379, 5000);
        jedis.auth("MyAicyber201415926");
        jedis.set("ss", "ss");
        System.out.println("业务处理");
    }

    public static void xx1() {
        Jedis jedis = new Jedis("114.116.230.141", 6379, 5000);
        jedis.auth("MyAicyber201415926");
        jedis.set("ss", "ss");
        System.out.println("业务处理1");
    }
}
