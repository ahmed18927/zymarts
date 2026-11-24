package com.example.SaaS.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationCode(
            String to,
            String code
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Verification Code");
        message.setText(
                "Your verification code is: "
                        + code
        );

        mailSender.send(message);
    }

    public void sendResetPasswordCode(
            String to,
            String code
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Reset Password");

        message.setText(
                "Your reset password code is: "
                        + code
        );

        mailSender.send(message);
    }
}
