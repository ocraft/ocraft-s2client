package com.github.ocraft.test;

import java.util.concurrent.TimeUnit;

public class Threads {

    public static void delay(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
