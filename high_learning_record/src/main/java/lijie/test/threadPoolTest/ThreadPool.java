package lijie.test.threadPoolTest;

import java.util.concurrent.*;

public class ThreadPool {
    private static volatile ExecutorService executorService;

    private static void init() {
        if (executorService == null) {
            synchronized (ThreadPool.class) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 1, TimeUnit.SECONDS, new ArrayBlockingQueue(2048),
                            new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        if (executorService == null) {
            init();
        }
        Future<T> future = executorService.submit(callable);
        return future;
    }

}
