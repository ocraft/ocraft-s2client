package com.github.ocraft.test;

/*-
 * #%L
 * ocraft-s2client-api
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
