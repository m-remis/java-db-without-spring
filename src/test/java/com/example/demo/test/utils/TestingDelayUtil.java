package com.example.demo.test.utils;

public class TestingDelayUtil {

    private static final boolean ENABLE_DELAY_FOR_TESTS = true;

    public static void delay() {
        if (ENABLE_DELAY_FOR_TESTS) {
            try {
                // Sleep for 0,5 seconds (500 milliseconds)
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Handle the InterruptedException
            }
        }
    }
}