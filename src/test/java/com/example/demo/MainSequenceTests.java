package com.example.demo;

import com.example.demo.dto.UserDto;
import com.example.demo.processor.CommandProcessor;
import com.example.demo.service.UserCommands;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.example.demo.config.DbConfig.executeSchemaScript;
import static com.example.demo.test.utils.TestingDelayUtil.delay;

class MainSequenceTests {

    private UserCommands userCommands;
    private CommandProcessor commandProcessor;

    @BeforeEach
    public void setUp() {
        executeSchemaScript();
        userCommands = new UserCommands();
        commandProcessor = new CommandProcessor();
    }

    @AfterEach
    public void tearDown() {
        userCommands.deleteAllUsersCommand();
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
        // Delay to allow processing
        delay();
        Assertions.assertFalse(userCommands.findAllUsersCommand().isEmpty());

        commandProcessor.addCommand(userCommands::printAllUsersCommand);
        commandProcessor.addCommand(userCommands::deleteAllUsersCommand);
        commandProcessor.addCommand(userCommands::printAllUsersCommand);

        // Delay to allow processing
        delay();
        commandProcessor.stopProcessing();

        Assertions.assertTrue(userCommands.findAllUsersCommand().isEmpty());
    }
}
