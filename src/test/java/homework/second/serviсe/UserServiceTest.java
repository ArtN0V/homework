package homework.second.serviсe;

import homework.second.dto.UserDto;
import homework.second.model.UserEntity;
import homework.second.repository.UserRepository;
import homework.second.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldSaveAndReturnDto() {
        UserEntity entity = new UserEntity("Artem", "a@example.com", 21);
        entity.setId(1L);
        when(repo.save(any(UserEntity.class))).thenReturn(entity);

        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Artem");
        dto.setEmail("a@example.com");
        dto.setAge(21);

        UserDto result = service.createUser(dto);

        assertEquals(result.getName(), "Artem");
        verify(repo, times(1)).save(any(UserEntity.class));
    }

    @Test
    void getUser_shouldReturnDtoIfExists() {
        UserEntity entity = new UserEntity("User", "u@example.com", 20);
        entity.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(entity));

        UserDto dto = service.getUser(1L);

        assertEquals(dto.getName(), "User");
    }

    @Test
    void getUser_shouldThrowIfNotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getUser(999L));
    }
}
