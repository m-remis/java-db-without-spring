package com.example.demo;

import com.example.demo.config.DbConfig;
import com.example.demo.entity.UserEntity;
import com.example.demo.processor.CommandProcessor;
import com.example.demo.service.UserCommands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class TestSequence {

    private static UserCommands userCommands;
    private static CommandProcessor commandProcessor;

    @BeforeAll
    public static void setUp() {
        DbConfig.executeSchemaScript();
        userCommands = new UserCommands();
        commandProcessor = new CommandProcessor();
    }

    @Test
    void mainFunctionalityTest() throws InterruptedException {
        var consumerThread = new Thread(commandProcessor::processCommands);
        consumerThread.start();
        // Test users
        List.of(
                new UserEntity(1, "a1", "Robert"),
                new UserEntity(2, "a2", "Martin")
        ).forEach(tesUser -> commandProcessor.addCommand(() -> userCommands.addUserCommand(tesUser)));

        Assertions.assertFalse(userCommands.findAllUsersCommand().isEmpty());

        commandProcessor.addCommand(userCommands::printAllUsersCommand);

        commandProcessor.addCommand(userCommands::deleteAllUsersCommand);

        commandProcessor.addCommand(userCommands::printAllUsersCommand);

        // Sleep for a longer period to allow commands to be processed
        Thread.sleep(5000);
        commandProcessor.stop();
        consumerThread.interrupt();

        Assertions.assertTrue(userCommands.findAllUsersCommand().isEmpty());
    }
}
