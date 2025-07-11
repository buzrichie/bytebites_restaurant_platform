package org.week6lap.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.week6lap.notificationservice.model.OrderPlacedEvent;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String mailAccount;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendOrderConfirmation(OrderPlacedEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        Context context = new Context();
        context.setVariable("orderId", event.orderId());
        context.setVariable("totalAmount", event.status());
        context.setVariable("orderDate", event.restaurantId());

        String htmlContent = templateEngine.process("email-order-placed", context);

        helper.setTo(String.valueOf(event.userId()));
        helper.setFrom(mailAccount);
        helper.setSubject("Order Confirmation #" + event.orderId());
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
