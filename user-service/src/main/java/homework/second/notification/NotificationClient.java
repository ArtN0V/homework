package homework.second.notification;

import homework.second.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "notification-service", fallback = NotificationClientFallback.class)
public interface NotificationClient {

    @PostMapping("/api/notifications")
    void sendNotification(UserDto user);
}
