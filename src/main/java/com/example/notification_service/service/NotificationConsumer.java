package com.example.notification_service.service;

import com.example.notification_service.dto.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Service
public class NotificationConsumer {

    private final JsonMapper jsonMapper;

    public NotificationConsumer(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(
            topics = "${kafka.topic.order-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {

        OrderCreatedEvent event =
                jsonMapper.readValue(
                        message,
                        OrderCreatedEvent.class
                );

        log.info("Received order-created event: {}", event);

        sendEmail(event);
    }

    private void sendEmail(OrderCreatedEvent event) {
        log.info("Email sent to customer: {}; Order ID: {}; Product: {}; Quantity: {}",
                event.customerId(), event.orderId(), event.product(), event.quantity());
    }
}
