package homework.second.service;

import homework.second.dto.UserDto;
import homework.second.kafka.UserEventProducer;
import homework.second.mapper.UserMapper;
import homework.second.model.UserEntity;
import homework.second.notification.NotificationClient;
import homework.second.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserEventProducer producer;
    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private UserService service;

    @Test
    void createUser_shouldSaveAndReturnDto() {
        UserDto dto = new UserDto();
        dto.setName("Artem");
        dto.setEmail("a@example.com");
        dto.setAge(21);

        when(repository.findByEmail("a@example.com")).thenReturn(Optional.empty());

        UserEntity saved = new UserEntity("Artem", "a@example.com", 21);
        saved.setId(1L);
        when(repository.save(UserMapper.entityFromDto(dto))).thenReturn(saved);

        UserDto result = service.createUser(dto);

        assertEquals(1L, result.getId());
        assertEquals("Artem", result.getName());
        assertEquals("a@example.com", result.getEmail());
        assertEquals(21, result.getAge());
    }

    @Test
    void getUser_shouldReturnDtoIfExists() {
        UserEntity entity = new UserEntity("User", "u@example.com", 20);
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        UserDto dto = service.getUser(1L);

        assertEquals(dto.getName(), "User");
    }

    @Test
    void getUser_shouldThrowIfNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getUser(999L));
    }
}
