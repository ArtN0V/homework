package homework.second.service;

import homework.second.dto.UserDto;
import homework.second.exeption.NotFoundException;
import homework.second.kafka.UserEvent;
import homework.second.kafka.UserEventProducer;
import homework.second.kafka.UserOperation;
import homework.second.mapper.UserMapper;
import homework.second.model.UserEntity;
import homework.second.notification.NotificationClient;
import homework.second.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final UserEventProducer producer;
    private final NotificationClient notificationClient;

    public UserService(UserRepository repository, UserEventProducer producer, NotificationClient notificationClient) {
        this.repository = repository;
        this.producer = producer;
        this.notificationClient = notificationClient;
    }

    /** Создание нового пользователя */
    public UserDto createUser(UserDto dto) {
        repository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("User with this email already exists");
        });

        UserEntity entity = UserMapper.entityFromDto(dto);
        UserEntity saved = repository.save(entity);
        logger.info("Created user with id {}", saved.getId());

        producer.publish(new UserEvent(UserOperation.CREATE, dto.getEmail(), dto.getId(), dto.getName()));

        // Отправка уведомления через external service с Circuit Breaker
        sendNotificationWithCircuitBreaker(dto);

        return UserMapper.dtoFromEntity(saved);
    }

    /** Получение пользователя по ID */
    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        UserEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        return UserMapper.dtoFromEntity(entity);
    }

    /** Получение всех пользователей */
    @Transactional(readOnly = true)
    public List<UserDto> listUsers() {
        return repository.findAll().stream()
                .map(UserMapper::dtoFromEntity)
                .collect(Collectors.toList());
    }

    /** Обновление пользователя */
    public UserDto updateUser(Long id, UserDto dto) {
        UserEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
        UserMapper.updateEntityFromDto(entity, dto);
        UserEntity updated = repository.save(entity);

        logger.info("Updated user with id {}", updated.getId());

        return UserMapper.dtoFromEntity(updated);
    }

    /** Удаление пользователя */
    public void deleteUser(Long id) {
        Optional<UserEntity> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found with id " + id);
        }

        repository.deleteById(id);
        producer.publish(new UserEvent(UserOperation.CREATE, user.get().getEmail(), id, user.get().getName()));

        sendNotificationWithCircuitBreaker(UserMapper.dtoFromEntity(user.get()));

        logger.info("Deleted user with id {}", id);
    }

    @CircuitBreaker(name = "notificationServiceCircuitBreaker", fallbackMethod = "notificationFallback")
    public void sendNotificationWithCircuitBreaker(UserDto user) {
        notificationClient.sendNotification(user);
    }

    public void notificationFallback(UserDto user, Throwable t) {
        logger.warn("Notification service is unavailable for user {}: {}", user.getEmail(), t.toString());
    }

}