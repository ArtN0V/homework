package homework.second.servise;

import homework.second.dao.UserDao;
import homework.second.model.UserEntity;
import homework.second.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldCallDaoAndReturnUserWithId() {
        UserEntity input = new UserEntity("Ivan", "ivan@example.com", 30);
        UserEntity saved = new UserEntity("Ivan", "ivan@example.com", 30);
        saved.setId(1L);

        when(userDao.create(any(UserEntity.class))).thenReturn(saved);

        UserEntity result = userService.createUser(input.getName(), input.getEmail(), input.getAge());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userDao, times(1)).create(any(UserEntity.class));
    }

    @Test
    void getUser_whenExists_thenReturn() {
        UserEntity u = new UserEntity("Petr", "petr@example.com", 25);
        u.setId(2L);

        when(userDao.findById(2L)).thenReturn(u);

        UserEntity result = userService.getUser(2L);

        assertEquals(u, result);
        verify(userDao).findById(2L);
    }

    @Test
    void listUsers_shouldReturnList() {
        when(userDao.findAll()).thenReturn(java.util.List.of(new UserEntity("A","a@a",10)));

        var list = userService.listUsers();

        assertTrue(list.size() > 0);
        verify(userDao).findAll();
    }

    @Test
    void deleteUser_shouldCallDaoDelete() {
        when(userDao.delete(5L)).thenReturn(true);

        boolean res = userService.deleteUser(5L);

        assertTrue(res);
        verify(userDao).delete(5L);
    }
}

