package exception.redis;

/**
 * @author 90604
 * @project exception
 * @date 2020/2/17
 **/
public class KeysNotFoundException extends BaseRedisException {

    public KeysNotFoundException(){
        super("不存在的Key");
    }
}
