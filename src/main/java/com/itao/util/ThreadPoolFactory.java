package com.itao.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 生产线程池的工厂
 * @Author sjt
 * @CreateTime 2022/08/22 13:52
 */
public class ThreadPoolFactory {

    private static final Map<String, ThreadPool> pools = new ConcurrentHashMap<>();

    public static ThreadPool threadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        String threadId = UUID.randomUUID().toString().replace("-", "");
        ThreadPool threadPool = new ThreadPool(threadId, executorService);
        pools.put(threadId, threadPool);
        return threadPool;
    }

    public static void colse(ThreadPool threadPool) {
        if (threadPool == null) {
            return;
        }
        ThreadPool pool = pools.remove(threadPool.getThreadId());
        if (pool != null) {
            pool.getExecutorService().shutdown();
        }
    }
}
