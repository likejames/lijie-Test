package lijie.test.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThreadPool {
    private static final Logger logger= LoggerFactory.getLogger(ThreadPool.class);
    private ThreadPool(){}
    private static volatile ExecutorService executorService;

    private static void init(){
        if (executorService==null){
            synchronized (ThreadPool.class){
                if (executorService==null){
                    executorService=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),Runtime.getRuntime().availableProcessors()*2,1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2048),
                            new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }
    }

//    public static void submit(Runnable runnable){
//        if (executorService==null){
//            init();
//        }
//        executorService.submit(runnable);
//    }

    public static <T> Future<?> submit(Runnable callable){
        if (executorService==null){
            init();
        }
        Future<?> future=executorService.submit(callable);
        return future;
    }
}
