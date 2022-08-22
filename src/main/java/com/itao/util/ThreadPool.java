package com.itao.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @Description 线程池
 * @Author sjt
 * @CreateTime 2022/08/22 13:54
 */
@Getter
@AllArgsConstructor
public class ThreadPool{

    private String threadId;
    private ExecutorService executorService;

    public void submit(Runnable task) {
         executorService.submit(task);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }
    public <T> Future<T> submit(Runnable task, T result) {
        return executorService.submit(task, result);
    }
}
