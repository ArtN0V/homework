package org.notification;

import org.junit.jupiter.api.Test;
import org.notification.kafka.UserEvent;
import org.notification.kafka.UserOperation;
import org.notification.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "user-events" }, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@ActiveProfiles("test")
public class NotificationIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockBean
    private MailService mailService;

    @Test
    public void whenCreateEvent_mailIsSent() throws Exception {
        UserEvent event = new UserEvent(UserOperation.CREATE, "test@example.com", 123L, "oleh");
        kafkaTemplate.send("user-events", event.getUserId().toString(), event);
        kafkaTemplate.flush();

        Thread.sleep(800);

        verify(mailService).sendSimpleMessage(eq("test@example.com"), eq("Регистрация на сайте"),
                eq("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан."));
    }

    @Test
    public void whenDeleteEvent_mailIsSent() throws Exception {
        UserEvent event = new UserEvent(UserOperation.DELETE, "del@example.com", 124L, "oleh");
        kafkaTemplate.send("user-events", event.getUserId().toString(), event);
        kafkaTemplate.flush();

        Thread.sleep(800);

        verify(mailService).sendSimpleMessage(eq("del@example.com"), eq("Удаление аккаунта"),
                eq("Здравствуйте! Ваш аккаунт был удалён."));
    }
}
