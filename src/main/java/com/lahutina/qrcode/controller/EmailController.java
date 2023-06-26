package com.lahutina.qrcode.controller;

import com.lahutina.qrcode.entity.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class EmailController {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;

    @PostMapping("/sendEmailWithPhoto")
    public ResponseEntity<String> sendEmailWithPhoto(@RequestBody EmailRequest emailRequest) {
        System.out.println(emailRequest.getImage());

        String image = emailRequest.getImage().substring(emailRequest.getImage().indexOf(",") + 1).trim();
        System.out.println(image);

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            byte[] imageBytes = Base64.getDecoder().decode(image);
            messageHelper.setText("Here is your generated QR code!");
            messageHelper.setSubject("QR Code Image");
            messageHelper.setTo(emailRequest.getEmail());
            messageHelper.setFrom(fromEmail);

            messageHelper.addAttachment("image.png", () -> new ByteArrayInputStream(imageBytes));

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with photo", e);
        }

        return ResponseEntity.ok("Email sent successfully.");
    }
}
