package com.example.demo;

import com.example.demo.processor.CommandProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.demo.test.utils.TestingDelayUtil.delay;

class CommandProcessorTests {

    private static CommandProcessor commandProcessor;

    @BeforeAll
    public static void setUp() {
        commandProcessor = new CommandProcessor();
    }

    @Test
    @DisplayName("Add command to processor")
    void testAddCommand_ShouldProcessCommands() {
        var counter = new AtomicInteger(0);
        Runnable testCommand = counter::incrementAndGet;
        var consumerThread = new Thread(commandProcessor::processCommands);
        consumerThread.start();
        commandProcessor.startProcessing();
        commandProcessor.addCommand(testCommand);
        // Sleep this thread for a longer period to allow commands to be processed in consumer thread
        delay();

        Assertions.assertEquals(1, counter.get());
    }

    @Test
    @DisplayName("Stopped command processor, should not process submitted commands")
    void testStopCommandProcessor_ShouldNotProcessCommands() {
        var counter = new AtomicInteger(0);
        Runnable testCommand = counter::incrementAndGet;
        var consumerThread = new Thread(commandProcessor::processCommands);
        consumerThread.start();
        commandProcessor.addCommand(testCommand);

        Assertions.assertEquals(0, counter.get());
        Assertions.assertFalse(commandProcessor.isExecuting());
    }
}
