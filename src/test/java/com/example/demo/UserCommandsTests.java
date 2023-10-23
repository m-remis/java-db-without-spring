package com.example.demo;


import com.example.demo.config.DbConfig;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserCommands;
import org.junit.jupiter.api.*;

import java.util.List;

class UserCommandsTests {

    private UserCommands userCommands;

    @BeforeEach
    public void setUp() {
        DbConfig.executeSchemaScript();
        userCommands = new UserCommands();
    }

    @AfterEach
    public void tearDown() {
        userCommands.deleteAllUsersCommand();
    }

    @Test
    @DisplayName("Add user command")
    void testAddUserCommand() {
        var user = new UserDto(1, "guid", "username");

        userCommands.addUserCommand(user);

        Assertions.assertTrue(userCommands.findUserByIdCommand(1).isPresent());
        Assertions.assertEquals(user, userCommands.findUserByIdCommand(1).get());
    }

    @Test
    @DisplayName("Add user command with same ID twice")
    void testAddUserCommand_TryToSaveSamePrimaryKeyTwice() {
        var user1 = new UserDto(1, "guid", "username");
        var user2 = new UserDto(1, "guid2", "username2");

        userCommands.addUserCommand(user1);
        userCommands.addUserCommand(user2);

        Assertions.assertTrue(userCommands.findUserByIdCommand(1).isPresent());
        Assertions.assertEquals(user1, userCommands.findUserByIdCommand(1).get());
        Assertions.assertNotEquals(user2.userName(), userCommands.findUserByIdCommand(1).get().userName());
        Assertions.assertNotEquals(user2.guid(), userCommands.findUserByIdCommand(1).get().guid());
    }

    @Test
    @DisplayName("Get all users command")
    void testGetAllUsersCommand() {
        var usersToBeSaved = List.of(
                new UserDto(1, "guid", "username"),
                new UserDto(2, "guid2", "username2"),
                new UserDto(3, "guid3", "username3")
        );
        usersToBeSaved.forEach(userCommands::addUserCommand);

        Assertions.assertTrue(userCommands.findAllUsersCommand().containsAll(usersToBeSaved));
    }

    @Test
    @DisplayName("Delete all users command")
    void testDeleteAllUsersCommand() {
        var usersToBeSaved = List.of(
                new UserDto(2, "guid1", "username1"),
                new UserDto(2, "guid2", "username2"),
                new UserDto(3, "guid3", "username3")
        );
        usersToBeSaved.forEach(userCommands::addUserCommand);
        userCommands.deleteAllUsersCommand();

        Assertions.assertTrue(userCommands.findAllUsersCommand().isEmpty());
    }
}