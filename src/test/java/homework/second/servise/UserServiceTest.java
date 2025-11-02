package homework.second.servise;

import homework.second.dao.UserDao;
import homework.second.model.UserEntity;
import homework.second.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(userDao, times(1)).create(any(UserEntity.class));
    }

    @Test
    void getUser_whenExists_thenReturn() {
        UserEntity u = new UserEntity("Petr", "petr@example.com", 25);
        u.setId(2L);

        when(userDao.findById(2L)).thenReturn(u);

        UserEntity result = userService.getUser(2L);

        assertThat(result).isEqualTo(u);
        verify(userDao).findById(2L);
    }

    @Test
    void listUsers_shouldReturnList() {
        when(userDao.findAll()).thenReturn(java.util.List.of(new UserEntity("A","a@a",10)));

        var list = userService.listUsers();

        assertThat(list).hasSize(1);
        verify(userDao).findAll();
    }

    @Test
    void deleteUser_shouldCallDaoDelete() {
        when(userDao.delete(5L)).thenReturn(true);

        boolean res = userService.deleteUser(5L);

        assertThat(res).isTrue();
        verify(userDao).delete(5L);
    }
}

