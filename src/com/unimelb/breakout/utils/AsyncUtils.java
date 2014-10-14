package com.unimelb.breakout.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.Handler;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * Run the request in separate thread.
 * @author Siyuan Zhang
 *
 */
public class AsyncUtils {
    // Used to run tasks off the main UI (or calling) thread.
    private static final ListeningExecutorService executorService =
            MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    static {
        // Shutdown executorService threads on JVM shutdown.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                // Force interrupt of threads (a more graceful shutdown is possible if desired).
                executorService.shutdownNow();
            }
        });
    }

    /**
     * Run a runnable in a worker thread.
     */
    public static void run(Runnable r) {
        executorService.submit(r);
    }

    /**
     * This method returns a future, a potentially unfinished calculation that may finish in the future.
     * To use this, it will need to attach a callback to it:
     * 
     * ListenableFuture<Object>future = Async.run(...)
     * Async.addCallback(activity, loginFuture, new FutureCallback<Object>() {
     *     @Override
     *     public void onSuccess(Object object) {
     *         // use object
     *     }
     *     @Override
     *     public void onFailure(Throwable throwable) {
     *         log.error("Throwable", throwable);
     *     }
     * });
     */
    public static <T> ListenableFuture<T> run(Callable<T> callable) {
        return executorService.submit(callable);
    }

    /**
     * Use this method to attach a callback to the future so that the communication is executed in 
     * a separate non-UI thread and the result will be handled by the UI thread.
     */
    public static <T> void addCallback(ListenableFuture<T> future, FutureCallback<T> callback) {
        final Handler handler = new Handler(); // Create handler in UI Thread.
        Futures.addCallback(future, callback, new Executor() {
            public void execute(Runnable r) {
                handler.post(r); // Use handler to run callback on UI thread.
            }
        });
    }
}