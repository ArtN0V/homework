package org.notification.service;

import org.notification.kafka.UserOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JMSMailService implements MailService {
    private final String subjectCreateString = "Регистрация на сайте";
    private final String textCreateString = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
    private final String subjectDeleteString = "Удаление аккаунта";
    private final String textDeleteString = "Здравствуйте! Ваш аккаунт был удалён.";
    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromAddress;

    public JMSMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendUserOperationEmail(String email, UserOperation operation) {
        String subject;
        String text;

        switch (operation) {
            case CREATE -> {
                subject = subjectCreateString;
                text = textCreateString;
            }
            case DELETE -> {
                subject = subjectDeleteString;
                text = textDeleteString;
            }
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        }

        sendSimpleMessage(email, subject, text);
    }
}
