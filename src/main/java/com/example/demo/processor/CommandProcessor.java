package com.example.demo.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandProcessor {

    private final BlockingQueue<Runnable> commandQueue = new LinkedBlockingQueue<>();
    private volatile boolean executeCommands = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandProcessor.class);

    public synchronized void addCommand(Runnable command) {
        LOGGER.info("Adding command...");
        var added = commandQueue.offer(command);
        LOGGER.info("Command added: {}", added);
    }

    public void processCommands() {
        LOGGER.info("Processing commands, enabled: {}", executeCommands);
        while (executeCommands) {
            try {
                Runnable command = commandQueue.take();
                command.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void stopProcessing() {
        executeCommands = false;
    }

    public void startProcessing() {
        executeCommands = true;
    }

    public boolean isExecuting() {
        return executeCommands;
    }
}
