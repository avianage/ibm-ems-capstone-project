package com.ibm.auth.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ibm.auth.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:no-reply@example.com}")
    private String fromAddress;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toEmail);
            message.setSubject("Your password reset OTP");
            message.setText(
                    "Your OTP for password reset is: " + otp + "\n\n" +
                    "This code expires in 10 minutes. If you did not request this, " +
                    "please ignore this email or contact support."
            );
            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send OTP email to {}", toEmail, ex);
        }
    }

    @Override
    @Async
    public void sendPasswordChangedNotification(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toEmail);
            message.setSubject("Your password was changed");
            message.setText(
                    "This is a confirmation that your account password was just changed. " +
                    "If this wasn't you, please reset your password immediately and contact support."
            );
            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send password-changed notification to {}", toEmail, ex);
        }
    }
}
