package com.github.ocraft.test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MultiThreadedStressTester {

    private static final int DEFAULT_THREAD_COUNT = 2;

    private final ExecutorService executor;
    private final int threadCount;
    private final int iterationCount;
    private final AtomicBoolean errorOccurred = new AtomicBoolean(false);
    private boolean stopIfError;

    public MultiThreadedStressTester(int iterationCount) {
        this(DEFAULT_THREAD_COUNT, iterationCount);
    }

    public MultiThreadedStressTester(int threadCount, int iterationCount) {
        this.threadCount = threadCount;
        this.iterationCount = iterationCount;
        this.executor = Executors.newCachedThreadPool();
    }

    public MultiThreadedStressTester(int threadCount, int iterationCount, ThreadFactory threadFactory) {
        this.threadCount = threadCount;
        this.iterationCount = iterationCount;
        this.executor = Executors.newCachedThreadPool(threadFactory);
    }

    public int totalActionCount() {
        return threadCount * iterationCount;
    }

    public MultiThreadedStressTester stress(final Runnable action) throws InterruptedException {
        spawnThreads(action).await();
        return this;
    }

    public MultiThreadedStressTester stress(final Runnable action, boolean stopIfError) throws InterruptedException {
        this.stopIfError = stopIfError;
        spawnThreads(action).await();
        return this;
    }

    public void blitz(long timeoutMs, final Runnable action) throws InterruptedException, TimeoutException {
        if (!spawnThreads(action).await(timeoutMs, MILLISECONDS)) {
            throw new TimeoutException("timed out waiting for blitzed actions to complete successfully");
        }
    }

    private CountDownLatch spawnThreads(final Runnable action) {
        final CountDownLatch finished = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    repeat(action);
                } catch (Exception e) {
                    errorOccurred.compareAndSet(false, true);
                    throw e;
                } finally {
                    finished.countDown();
                }
            });
        }
        return finished;
    }

    public boolean errorOccurred() {
        return errorOccurred.get();
    }

    private void repeat(Runnable action) {
        for (int i = 0; doNotStop() && i < iterationCount; i++) {
            action.run();
        }
    }

    private boolean doNotStop() {
        return !stopIfError || !errorOccurred.get();
    }

    public void shutdown() {
        executor.shutdown();
    }
}