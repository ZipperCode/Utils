package utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 90604
 * @project utils
 * @date 2020/2/15
 **/
public class RedisConfig {

    public static final String HOST = "";
    public static final int PORT = 6379;
    /**
     * redis 密码
     */
    public static final String PASSWORD = "";
    /**
     * 超时时间
     */
    public static final int TIMEOUT = 10000;
    /**
     * 最大连接数 -1 表示无限制
     */
    public static final int MAX_TOTAL = 1024;
    /**
     * 最大空闲数
     */
    public static final int MAX_IDLE = 8;
    /**
     * 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException
     */
    public static final int MAX_WAIT = 10000;
    /**
     *  在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
     */
    public static final boolean TEST_ON_BORROW = true;

    /**
     * 键过期时间
     */
    public static final int EXPIRES = -1;


    /**
     * 获取默认线程池配置
     * @return
     */
    public static JedisPoolConfig getRedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_TOTAL);
        config.setMaxWaitMillis(MAX_WAIT);
        config.setTestOnBorrow(TEST_ON_BORROW);
        config.setMaxIdle(MAX_IDLE);
        return config;
    }

}
