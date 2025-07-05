package org.week6lap.notificationservice.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.week6lap.notificationservice.model.OrderPlacedEvent;


@Slf4j
@Component
public class OrderEventListener {

    @RabbitListener(queues = "order.placed.queue")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Received order event: {}", event);
        // Send email or push notification
    }
}
