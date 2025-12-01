package homework.second.notification;

import homework.second.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationClientFallback implements NotificationClient {
    @Override
    public void sendNotification(UserDto user) {
        System.out.println("Fallback: Notification service is down for user " + user.getEmail());
    }
}
