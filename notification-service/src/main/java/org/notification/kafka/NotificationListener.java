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
        if (operation == UserOperation.CREATE) {
            String subject = "Регистрация на сайте";
            String text = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
            mailService.sendSimpleMessage(email, subject, text);
        } else if (operation == UserOperation.DELETE) {
            String subject = "Удаление аккаунта";
            String text = "Здравствуйте! Ваш аккаунт был удалён.";
            mailService.sendSimpleMessage(email, subject, text);
        }
    }
}
