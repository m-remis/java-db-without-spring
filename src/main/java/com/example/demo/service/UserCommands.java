package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserCommands {

    private static final String PERSISTENCE_UNIT_NAME = "UserPU";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCommands.class);

    public void addUserCommand(UserEntity user) {
        LOGGER.info("[Adding user to DB]");
        findUserByIdCommand(user.getUserId())
                .ifPresentOrElse(found -> LOGGER.info("[User with userId {} already exists. Insertion aborted]", user.getUserId()),
                        () -> {
                            EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                            EntityManager entityManager = emf.createEntityManager();

                            EntityTransaction transaction = entityManager.getTransaction();
                            transaction.begin();
                            entityManager.persist(user);
                            transaction.commit();

                            entityManager.close();
                            emf.close();
                        });
    }

    public void printAllUsersCommand() {
        LOGGER.info("[Printing all users]");
        findAllUsersCommand().forEach(userEntity -> LOGGER.info(userEntity.toString()));
    }

    public List<UserEntity> findAllUsersCommand() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        List<UserEntity> users = entityManager.createQuery("SELECT e FROM UserEntity e", UserEntity.class).getResultList();
        entityManager.close();
        emf.close();
        return users;
    }

    public Optional<UserEntity> findUserByIdCommand(Integer userId) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        UserEntity user = entityManager.find(UserEntity.class, userId);

        entityManager.close();
        emf.close();

        return Optional.ofNullable(user);
    }

    public void deleteAllUsersCommand() {
        LOGGER.info("[Deleting all users]");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.createQuery("DELETE FROM UserEntity").executeUpdate();
        transaction.commit();

        entityManager.close();
        emf.close();
    }
}
