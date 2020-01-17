package exception;

public class NetWorkException extends Exception{

    public NetWorkException() {
        this("网络异常",null);
    }

    public NetWorkException(String message) {
        this(message,null);
    }


    public NetWorkException(Throwable cause) {
        this("网络异常",cause);
    }

    public NetWorkException(String message, Throwable cause) {
        super(message, cause);
    }
}
