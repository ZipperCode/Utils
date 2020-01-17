package manager;

public class NetWorkExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static NetWorkExceptionHandler netWorkExceptionHandler = new NetWorkExceptionHandler();

    private NetWorkExceptionHandler(){
    }

    public static NetWorkExceptionHandler getInstance(){
        return netWorkExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        System.out.println("ExceptionHandler ==>"+thread.getName());
        System.out.println("当前线程是 ==> "+ Thread.currentThread().getName());
    }
}
