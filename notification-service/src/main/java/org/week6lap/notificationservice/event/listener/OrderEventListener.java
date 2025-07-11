package org.week6lap.notificationservice.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.week6lap.notificationservice.model.OrderPlacedEvent;
import org.week6lap.notificationservice.service.EmailService;


@Slf4j
@Component
public class OrderEventListener {
    @Autowired
    EmailService emailService;

    @RabbitListener(queues = "order.placed.queue")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        try {
            emailService.sendOrderConfirmation(event);
            log.info("Sent confirmation email for order {}", event.orderId());
        } catch (Exception e) {
            log.error("Failed to send confirmation email for order {}", event.orderId(), e);
        }
    }
}
