package org.notification.kafka;

import org.notification.service.MailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    private final MailService mailService;

    public NotificationListener(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void handle(UserEvent event) {
        if (event == null || event.getEmail() == null) {
            return;
        }
        String email = event.getEmail();
        UserOperation operation = event.getOperation();

        mailService.sendUserOperationEmail(email, operation);
    }
}
