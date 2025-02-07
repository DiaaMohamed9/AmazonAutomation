package com.amazon.utils;

public class Helpers {
    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000); // Convert seconds to milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
