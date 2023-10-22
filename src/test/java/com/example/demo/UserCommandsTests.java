package com.example.demo;


import com.example.demo.config.DbConfig;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserCommands;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    void testAddUserCommand() {
        var userEntity = new UserEntity();
        userEntity.setUserGuid("guid");
        userEntity.setUserName("user-name");
        userEntity.setUserId(1);

        userCommands.addUserCommand(userEntity);

        Assertions.assertTrue(userCommands.findUserByIdCommand(1).isPresent());
        Assertions.assertEquals(userEntity, userCommands.findUserByIdCommand(1).get());
    }

    @Test
    void testAddUserCommand_TryToSaveSamePrimaryKeyTwice() {
        var userEntity = new UserEntity();
        userEntity.setUserGuid("guid");
        userEntity.setUserName("user-name");
        userEntity.setUserId(1);

        var userEntity2 = new UserEntity();
        userEntity2.setUserGuid("guid2");
        userEntity2.setUserName("user-name2");
        userEntity2.setUserId(1);

        userCommands.addUserCommand(userEntity);
        userCommands.addUserCommand(userEntity2);

        Assertions.assertTrue(userCommands.findUserByIdCommand(1).isPresent());
        Assertions.assertEquals(userEntity, userCommands.findUserByIdCommand(1).get());
        Assertions.assertNotEquals(userEntity2.getUserName(), userCommands.findUserByIdCommand(1).get().getUserName());
        Assertions.assertNotEquals(userEntity2.getUserGuid(), userCommands.findUserByIdCommand(1).get().getUserGuid());
    }

    @Test
    void testGetAllUsersCommand() {
        var userEntity1 = new UserEntity();
        userEntity1.setUserGuid("guid1");
        userEntity1.setUserName("user-name1");
        userEntity1.setUserId(1);

        var userEntity2 = new UserEntity();
        userEntity2.setUserGuid("guid2");
        userEntity2.setUserName("user-name2");
        userEntity2.setUserId(2);

        var userEntity3 = new UserEntity();
        userEntity3.setUserGuid("guid3");
        userEntity3.setUserName("user-name3");
        userEntity3.setUserId(3);

        var usersToBeSaved = List.of(userEntity1, userEntity2, userEntity3);

        usersToBeSaved.forEach(item -> userCommands.addUserCommand(item));

        Assertions.assertTrue(userCommands.findAllUsersCommand().containsAll(usersToBeSaved));
    }

    @Test
    void testDeleteAllUsersCommand() {
        var userEntity1 = new UserEntity();
        userEntity1.setUserGuid("guid1");
        userEntity1.setUserName("user-name1");
        userEntity1.setUserId(1);

        var userEntity2 = new UserEntity();
        userEntity2.setUserGuid("guid2");
        userEntity2.setUserName("user-name2");
        userEntity2.setUserId(2);

        var userEntity3 = new UserEntity();
        userEntity3.setUserGuid("guid3");
        userEntity3.setUserName("user-name3");
        userEntity3.setUserId(3);

        var usersToBeSaved = List.of(userEntity1, userEntity2, userEntity3);

        usersToBeSaved.forEach(item -> userCommands.addUserCommand(item));
        userCommands.deleteAllUsersCommand();

        Assertions.assertTrue(userCommands.findAllUsersCommand().isEmpty());
    }

}