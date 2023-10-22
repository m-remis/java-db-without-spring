package com.example.demo.processor;

import java.util.LinkedList;
import java.util.Queue;

public class CommandProcessor {

    private final Queue<Runnable> commandQueue = new LinkedList<>();
    private boolean executeCommands = false;

    public void addCommand(Runnable command) {
        synchronized (commandQueue) {
            commandQueue.offer(command);
            // Notify the consumer thread
            commandQueue.notifyAll();
        }
    }

    public void processCommands() {
        executeCommands = true;
        while (executeCommands) {
            Runnable command;
            synchronized (commandQueue) {
                while (commandQueue.isEmpty()) {
                    try {
                        commandQueue.wait(); // Wait for commands to be added
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                command = commandQueue.poll();
            }

            if (command != null) {
                command.run();
            }
        }
    }

    public synchronized void stop() {
        executeCommands = false;
    }

    public synchronized boolean isExecuting() {
        return executeCommands;
    }
}
