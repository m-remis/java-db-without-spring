package com.example.demo;

import com.example.demo.config.DbConfig;
import com.example.demo.dto.UserDto;
import com.example.demo.processor.CommandProcessor;
import com.example.demo.service.UserCommands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.demo.test.utils.TestingDelayUtil.delay;

class MainTestSequence {

    private static UserCommands userCommands;
    private static CommandProcessor commandProcessor;

    @BeforeAll
    public static void setUp() {
        DbConfig.executeSchemaScript();
        userCommands = new UserCommands();
        commandProcessor = new CommandProcessor();
    }

    @Test
    @DisplayName("Main test flow")
    void mainTestFlowTest() {
        var consumerThread = new Thread(commandProcessor::processCommands);
        consumerThread.start();
        commandProcessor.startProcessing();
        // Test users
        List.of(
                new UserDto(1, "a1", "Robert"),
                new UserDto(2, "a2", "Martin")
        ).forEach(tesUser -> commandProcessor.addCommand(() -> userCommands.addUserCommand(tesUser)));
        // Sleep this thread for a longer period to allow commands to be processed in consumer thread
        delay();
        Assertions.assertFalse(userCommands.findAllUsersCommand().isEmpty());

        commandProcessor.addCommand(userCommands::printAllUsersCommand);
        commandProcessor.addCommand(userCommands::deleteAllUsersCommand);
        commandProcessor.addCommand(userCommands::printAllUsersCommand);

        // Sleep this thread for a longer period to allow commands to be processed in consumer thread
        delay();
        commandProcessor.stopProcessing();

        Assertions.assertTrue(userCommands.findAllUsersCommand().isEmpty());
    }
}
