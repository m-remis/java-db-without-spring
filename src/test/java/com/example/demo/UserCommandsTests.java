package com.example.demo;


import com.example.demo.config.DbConfig;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserCommands;
import org.junit.jupiter.api.*;

import java.util.List;

class UserCommandsTests {

    private static UserCommands userCommands;

    @BeforeAll
    public static void setUp() {
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
        var userEntity = new UserDto(1, "guid", "username");

        userCommands.addUserCommand(userEntity);

        Assertions.assertTrue(userCommands.findUserByIdCommand(1).isPresent());
        Assertions.assertEquals(userEntity, userCommands.findUserByIdCommand(1).get());
    }

    @Test
    @DisplayName("Add user command with same ID twice")
    void testAddUserCommand_TryToSaveSamePrimaryKeyTwice() {
        var userEntity = new UserDto(1, "guid", "username");
        var userEntity2 = new UserDto(1, "guid2", "username2");

        userCommands.addUserCommand(userEntity);
        userCommands.addUserCommand(userEntity2);

        Assertions.assertTrue(userCommands.findUserByIdCommand(1).isPresent());
        Assertions.assertEquals(userEntity, userCommands.findUserByIdCommand(1).get());
        Assertions.assertNotEquals(userEntity2.userName(), userCommands.findUserByIdCommand(1).get().userName());
        Assertions.assertNotEquals(userEntity2.guid(), userCommands.findUserByIdCommand(1).get().guid());
    }

    @Test
    @DisplayName("Get all users command")
    void testGetAllUsersCommand() {
        var userEntity1 = new UserDto(1, "guid", "username");
        var userEntity2 = new UserDto(2, "guid2", "username2");
        var userEntity3 = new UserDto(3, "guid3", "username3");

        var usersToBeSaved = List.of(userEntity1, userEntity2, userEntity3);
        List.of(userEntity1, userEntity2, userEntity3).forEach(item -> userCommands.addUserCommand(item));

        Assertions.assertTrue(userCommands.findAllUsersCommand().containsAll(usersToBeSaved));
    }

    @Test
    @DisplayName("Delete all users command")
    void testDeleteAllUsersCommand() {
        var user1 = new UserDto(2, "guid1", "username1");
        var user2 = new UserDto(2, "guid2", "username2");
        var user3 = new UserDto(3, "guid3", "username3");

        var usersToBeSaved = List.of(user1, user2, user3);
        usersToBeSaved.forEach(item -> userCommands.addUserCommand(item));
        userCommands.deleteAllUsersCommand();

        Assertions.assertTrue(userCommands.findAllUsersCommand().isEmpty());
    }
}