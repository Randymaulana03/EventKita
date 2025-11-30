package com.eventkita.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

    private final JavaMailSender mailSender;

    public EmailUtils(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Kirim email biasa
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    // Hanya dipakai untuk verifikasi akun
    public void sendVerificationEmail(String to, String verifyCode) {
        sendEmail(
                to,
                "Verify Your EventKita Account",
                "Kode verifikasi akun kamu adalah: " + verifyCode
        );
    }
}
