package homework.second.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Отвечает за отправку UserEvent в Kafka.
 */
@Component
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic = "user-events";

    public UserEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(UserEvent event) {
        kafkaTemplate.send(topic, event.getUserId() != null ? event.getUserId().toString() : null, event);
    }
}
