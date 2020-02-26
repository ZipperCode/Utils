package utils.redis;

import exception.EmptyException;
import exception.redis.BaseRedisException;
import exception.redis.KeysNotFoundException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;
import utils.ValidUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static utils.redis.RedisConfig.*;

/**
 * @author 90604
 * @project utils
 * @date 2020/2/15
 **/
public class RedisUtils {
    private static JedisPool jedisPool;

    static {
        jedisPool = new JedisPool(getRedisPoolConfig(),HOST,PORT,TIMEOUT,PASSWORD);
    }

    public static Jedis getRedis(){
        return jedisPool.getResource();
    }

    public static void setRedisPool(String host, int port, String password){
        jedisPool = new JedisPool(getRedisPoolConfig(),host,port,TIMEOUT,password);
    }

    public static void closePool(){
        if(jedisPool != null && !jedisPool.isClosed()){
            jedisPool.close();
        }
    }

    /**
     * 选择数据库
     * @param db 数据库编号
     */
    public static void select(int db){
        Jedis redis = getRedis();
        redis.select(db);
    }

    /**
     * 设置某个键的有效时间
     * @param key 键
     * @param second 时间 秒
     * @return 设置成功true 否则false
     */
    public static boolean setExpires(String key, int second){
        if(ValidUtils.isEmpty(key) || ValidUtils.isEmpty(second) || !hasKey(key)){
            return false;
        }
        Jedis redis = getRedis();
        redis.expire(key,second);
        redis.close();
        return true;
    }

    /**
     * key持久化设置 返回1 表示key已经持久化，0 表示key不存在或者key已经持久化
     * @param key
     * @return
     */
    public static boolean persist(String key){
        if(ValidUtils.isEmpty(key)){
            return false;
        }
        Jedis redis = getRedis();
        Long persist = redis.persist(key);
        redis.close();
        return  persist == 1;
    }

    /**
     * 查看某个键的存活时间，0 表示key不存在或者key为空
     * @param key
     * @return
     */
    public static long second(String key){
        if(ValidUtils.isEmpty(key)){
            return 0;
        }
        Jedis redis = getRedis();
        Long ttl = redis.ttl(key);
        redis.close();
        return ttl;
    }

    /**
     * 返回redis所存储的键类型
     * @param key
     * @return
     */
    public static String type(String key){
        if(ValidUtils.isEmpty(key)){
            return "null";
        }
        Jedis redis = getRedis();
        String type = redis.type(key);
        redis.close();
        return type;
    }


    /**
     * redis 是否包含键值对
     * @param key 键
     * @return
     */
    public static boolean hasKey(String key){
        if(ValidUtils.isEmpty(key)){
            return false;
        }
        Jedis redis = getRedis();
        boolean flag = redis.exists(key);
        redis.close();
        return flag;
    }

    public static boolean hasKeys(String...keys){
        if(ValidUtils.isEmpty(keys)){
            return false;
        }
        Jedis redis = getRedis();
        boolean flag =  redis.exists(keys) == keys.length;
        redis.close();
        return flag;
    }

    /**
     * 匹配存在的key，参数空引发异常
     * @param pattern 正则匹配
     * @return 结果
     * @throws KeysNotFoundException
     */
    public static Set<String> keys(String pattern) throws EmptyException {
        if(ValidUtils.isEmpty(pattern)){
            throw new EmptyException();
        }
        Jedis redis = getRedis();
        Set<String> keys = redis.keys(pattern);
        redis.close();
        return keys;
    }

    public static boolean deleteKey(String key){
        if(ValidUtils.isEmpty(key) || !hasKey(key)){
            return false;
        }
        Jedis redis = getRedis();
        redis.del(key);
        redis.close();
        return true;
    }

    public static boolean deleteKeys(String...keys){
        if(ValidUtils.isEmpty(keys) || !hasKeys(keys)){
            return false;
        }
        Jedis redis = getRedis();
        redis.del(keys);
        redis.close();
        return true;
    }

    /**
     * 重命名key 如果newKey存在则覆盖
     * @param oldKey
     * @param newKey
     * @throws EmptyException
     */
    public static void rename(String oldKey, String newKey) throws EmptyException, BaseRedisException {
        rename(oldKey,newKey,true);
    }

    /**
     * 如果新键已经存在且cover为true 则表示覆盖新键，否则抛出异常信息
     * @param oldKey
     * @param newKey
     * @param cover
     * @throws EmptyException
     * @throws BaseRedisException
     */
    public static void rename(String oldKey, String newKey, boolean cover) throws EmptyException, BaseRedisException {
        if(ValidUtils.isEmpty(oldKey,newKey)){
            throw new EmptyException();
        }
        Jedis redis = getRedis();
        try{
            if(!cover && redis.exists(newKey)){
                throw new BaseRedisException("重命名失败，新键已经存在");
            }else
                redis.rename(oldKey,newKey);
        }finally {
            redis.close();
        }
    }

    public static List<String> sort(String key) throws EmptyException {
        if(ValidUtils.isEmpty(key)){
            throw new EmptyException();
        }
        Jedis redis = getRedis();
        List<String> sort = redis.sort(key);
        redis.close();
        return sort;
    }

    public static List<String> sort(String key,int order) throws EmptyException {
        if(ValidUtils.isEmpty(key)){
            throw new EmptyException();
        }
        Jedis redis = getRedis();
        SortingParams sortingParams = new SortingParams();
        if(order > 0){
            sortingParams.asc();
        }else{
            sortingParams.desc();
        }
        List<String> sort = redis.sort(key);
        redis.close();
        return sort;
    }

    /**
     * 限制排序结果
     * @param key 要排序的key
     * @param start 开始位置
     * @param pageSize 页大小也就是限制数量
     * @return
     * @throws EmptyException
     */
    public static List<String> sort(String key,int start, int pageSize) throws EmptyException {
        if(ValidUtils.isEmpty(key)){
            throw new EmptyException();
        }
        Jedis redis = getRedis();
        SortingParams sortingParams = new SortingParams();
        sortingParams.limit(start,pageSize).asc();
        List<String> sort = redis.sort(key,sortingParams);
        redis.close();
        return sort;
    }

    public static List<String> sort(String key, SortingParams sortingParams) throws EmptyException {
        if(ValidUtils.isEmpty(key)){
            throw new EmptyException();
        }
        Jedis redis = getRedis();
        List<String> sort = redis.sort(key,sortingParams);
        redis.close();
        return sort;
    }


    public static boolean setString(String key, String value){
        if(ValidUtils.isEmpty(key,value)){
            return false;
        }
        Jedis redis = getRedis();
        redis.set(key,value);
        redis.close();
        return true;
    }

    public static boolean setString(String key, String value, int expires){
        if(ValidUtils.isEmpty(key,value) || ValidUtils.isEmpty(expires)){
            return false;
        }
        Jedis redis = getRedis();
        redis.set(key,value);
        redis.expire(key,expires);
        redis.close();
        return true;
    }

    public static String getString(String key){
        if(ValidUtils.isEmpty(key)){
            return null;
        }
        Jedis redis = getRedis();
        String value = redis.get(key);
        return value;
    }

//    public static long getStringValueLength(String key){
//
//    }


}
