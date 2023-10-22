package com.example.demo;

import com.example.demo.processor.CommandProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

class CommandProcessorTests {

    private static CommandProcessor commandProcessor;

    @BeforeAll
    public static void setUp() {
        commandProcessor = new CommandProcessor();
    }

    @Test
    void testAddCommand_ShouldProcessCommands() throws InterruptedException {
        var counter = new AtomicInteger(0);
        Runnable testCommand = counter::incrementAndGet;
        commandProcessor.addCommand(testCommand);

        var consumerThread = new Thread(commandProcessor::processCommands);
        consumerThread.start();

        Thread.sleep(1000);

        commandProcessor.stop();
        consumerThread.interrupt();

        Assertions.assertEquals(1, counter.get());
    }

    @Test
    void testStopCommandProcessor_ShouldNotProcessCommands() throws InterruptedException {
        var counter = new AtomicInteger(0);
        Runnable testCommand = counter::incrementAndGet;

        var consumerThread = new Thread(commandProcessor::processCommands);
        consumerThread.start();

        commandProcessor.stop();
        Thread.sleep(1000);

        commandProcessor.addCommand(testCommand);

        consumerThread.interrupt();

        Assertions.assertEquals(0, counter.get());
        Assertions.assertTrue(commandProcessor.isExecuting());
    }
}
