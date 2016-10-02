package thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(30, 60, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(2000), new ThreadPoolExecutor.CallerRunsPolicy());
    public static ThreadPoolExecutor fileWriteExecutor = new ThreadPoolExecutor(1, 3, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(2000), new ThreadPoolExecutor.CallerRunsPolicy());
}
