package exception;

/**
 * @author 90604
 * @project utils
 * @date 2020/2/17
 **/
public class EmptyException extends Exception {

    public EmptyException() {
        super("空值异常");
    }
}
