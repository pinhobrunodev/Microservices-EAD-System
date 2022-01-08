package com.ead.course.publishers;

import com.ead.course.dtos.NotificationCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandPublisher {


    @Value(value = "${ead.broker.exchange.notificationCommandExchange}")
    private String notificationCommandExchange;
    @Value(value = "${ead.broker.key.notificationCommandKey}")
    private String notificationCommandKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Method to send the events
    public void publishUserEvent(NotificationCommandDto notificationCommandDto) {
        rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey, notificationCommandDto);
    }


}
