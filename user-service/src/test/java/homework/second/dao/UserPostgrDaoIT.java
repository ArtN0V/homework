package homework.second.dao;

import homework.second.model.UserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoIT {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    void setUp() {
        POSTGRES.start();

        Configuration cfg = new Configuration();

        cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        cfg.setProperty("hibernate.connection.url", POSTGRES.getJdbcUrl());
        cfg.setProperty("hibernate.connection.username", POSTGRES.getUsername());
        cfg.setProperty("hibernate.connection.password", POSTGRES.getPassword());
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        cfg.setProperty("hibernate.show_sql", "false");

        cfg.addAnnotatedClass(UserEntity.class);

        sessionFactory = cfg.buildSessionFactory();

        userDao = new UserPostgrsDao(sessionFactory);
    }

    @AfterAll
    void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        POSTGRES.stop();
    }

    @Test
    void createAndFindById_shouldReturnSavedUser() {
        UserEntity user = new UserEntity("ITUser", "it@example.com", 21);

        UserEntity saved = userDao.create(user);
        assertThat(saved.getId()).isNotNull();

        UserEntity loaded = userDao.findById(saved.getId());
        assertThat(loaded).isNotNull();
        assertThat(loaded.getEmail()).isEqualTo("it@example.com");
    }

    @Test
    void findAll_update_delete_flow() {
        UserEntity user1 = new UserEntity("A", "a@test", 20);
        UserEntity user2 = new UserEntity("B", "b@test", 22);

        userDao.create(user1);
        userDao.create(user2);

        List<UserEntity> all = userDao.findAll();
        assertThat(all.size()).isGreaterThanOrEqualTo(2);

        UserEntity toUpdate = all.get(0);
        toUpdate.setName("UpdatedName");
        UserEntity updated = userDao.update(toUpdate);
        assertThat(updated.getName()).isEqualTo("UpdatedName");

        boolean deleted = userDao.delete(updated.getId());
        assertThat(deleted).isTrue();
    }
}

