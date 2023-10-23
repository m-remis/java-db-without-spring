package com.example.demo;

import com.example.demo.processor.CommandProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.example.demo.test.utils.TestingDelayUtil.delay;

class CommandProcessorTests {

    private CommandProcessor commandProcessor;

    @BeforeEach
    public void setUp() {
        commandProcessor = new CommandProcessor();
    }

    @Test
    @DisplayName("Add commands to processor")
    void testAddCommands_ShouldProcessCommands() {
        final int howManyCommandsToTest = 10;
        // Create a list of counters and a list of commands
        var counters = new ArrayList<AtomicInteger>();
        var commands = new ArrayList<Runnable>();
        // Initialize counters and create commands
        IntStream.rangeClosed(1, howManyCommandsToTest).forEach(item -> counters.add(new AtomicInteger(0)));
        counters.forEach(atomicInteger -> commands.add(atomicInteger::incrementAndGet));
        // Start the consumer thread and the command processing
        var consumerThread = new Thread(commandProcessor::processCommands);
        consumerThread.start();
        commandProcessor.startProcessing();
        // Add commands to the processor
        commands.forEach(commandProcessor::addCommand);
        // Delay to allow processing
        delay();
        // Verify that all counters have been incremented to 1
        counters.forEach(finishedCounter -> Assertions.assertEquals(1, finishedCounter.get()));
    }

    @Test
    @DisplayName("Stopped command processor, should not process submitted commands")
    void testStopCommandProcessor_ShouldNotProcessCommands() {
        var counter = new AtomicInteger(0);
        Runnable testCommand = counter::incrementAndGet;
        var consumerThread = new Thread(commandProcessor::processCommands);
        consumerThread.start();
        commandProcessor.addCommand(testCommand);
        // Delay to allow processing
        delay();
        Assertions.assertEquals(0, counter.get());
        Assertions.assertFalse(commandProcessor.isExecuting());
    }
}
