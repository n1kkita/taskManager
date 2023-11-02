package com.example.taskmanager.services.impl.email;

import jakarta.mail.Part;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    @Value(value = "${email}")
    private String from;
    private final JavaMailSender javaMailSender;
    @SneakyThrows
    public void send(String htmlContent,String to,String subject){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }
    @SneakyThrows
    public void sendWithFile(String subject, MultipartFile file){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(from);
        helper.setTo("kovalnikita209@gmail.com");
        helper.setSubject(subject);
        message.setContent(file.getBytes(), file.getContentType());
        message.setFileName(file.getOriginalFilename());
        message.setDisposition(Part.ATTACHMENT);

        javaMailSender.send(message);
    }
}
