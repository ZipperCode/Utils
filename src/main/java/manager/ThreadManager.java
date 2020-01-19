package manager;

import java.util.concurrent.*;

public class ThreadManager {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final String NETWORK_THREAD_NAME = "NetWorkThreadPool";
    private static final String IO_THREAD_NAME = "IOThread";
    /**
     * 默认核心线程数 最小四核最大八核
     */
    private static final int CORE_POOL_SIZE = Math.max(4, Math.min(CPU_COUNT - 1, 8));
    /**
     * 最大线程数
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 线程活跃时间
     */
    private static final long KEEP_ALIVE_SECONDS = 60L;

    private static final boolean isAsync = true;


    private static ExecutorService executorService = Executors.newCachedThreadPool((Runnable runnable)->{
        Thread thread = new Thread();
        thread.setUncaughtExceptionHandler(NetWorkExceptionHandler.getInstance());
        return thread;
    });

    /**
     * 同步 Httpclient 的网络线程池
     */
    private static ExecutorService netWorkExecutor;

//
//    private static ExecutorService asyncNetWorkExecutor = Executors.newSingleThreadExecutor((Runnable runnable)->{
//        Thread thread = new Thread(runnable);
//        thread.setUncaughtExceptionHandler(NetWorkExceptionHandler.getInstance());
//        return thread;
//    });

    /**
     * IO操作线程
     */
    private static ExecutorService fileExecutor = Executors.newSingleThreadExecutor((Runnable runnable)->{
        Thread thread = new Thread(runnable,IO_THREAD_NAME);
        thread.setUncaughtExceptionHandler(NetWorkExceptionHandler.getInstance());
        return thread;
    });

    static {
        if(isAsync){
            // 同步 Httpclient 的网络线程池
            netWorkExecutor = Executors.newSingleThreadExecutor((Runnable runnable)->{
                Thread thread = new Thread(runnable);
                thread.setUncaughtExceptionHandler(NetWorkExceptionHandler.getInstance());
                return thread;
            });
        }else{
             // asyncHttpClient 异步处理线程，使用单线程进行维护
            netWorkExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS,
                    TimeUnit.SECONDS, new LinkedBlockingDeque<>(), (Runnable runnable)-> {
                Thread thread = new Thread(runnable,NETWORK_THREAD_NAME);
                thread.setUncaughtExceptionHandler(NetWorkExceptionHandler.getInstance());
                return thread;
            });
        }
    }

    public static void executeNetWorkRunnable(Runnable runnable){
        //使用execute而不使用submit？execute 执行线程异常更容易捕获
        netWorkExecutor.execute(runnable);
    }

    public static void executeFileRunnable(Runnable runnable){
        fileExecutor.execute(runnable);
    }

    public static void shutdownNetworkExecutor(){
        netWorkExecutor.shutdown();
    }

    public static void shutdownFileExecutor(){
        fileExecutor.shutdown();
    }

}
