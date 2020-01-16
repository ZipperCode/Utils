package utils.http;

public class NetWorkException extends Exception{

    public NetWorkException() {
        super("网络异常");
    }

    public NetWorkException(String message) {
        super(message);
    }

    public NetWorkException(String message, Throwable cause) {
        super(message, cause);
    }
}
