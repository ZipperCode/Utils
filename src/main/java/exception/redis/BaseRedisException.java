package exception.redis;

/**
 * @author 90604
 * @project utils
 * @date 2020/2/18
 **/
public class BaseRedisException extends Exception {

    public static final String BASE_TAG = "【Redis】";

    public BaseRedisException() {
        super(BASE_TAG + "Redis 操作异常");
    }

    public BaseRedisException(String message) {
        super(BASE_TAG + message);
    }
}
