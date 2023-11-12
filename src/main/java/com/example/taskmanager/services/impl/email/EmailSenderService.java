package com.example.taskmanager.services.impl.email;

import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public void sendWithFile(String html, String subject, String to,MultipartFile[] files){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        Multipart multipart = new MimeMultipart("mixed");

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(html, "text/html; charset=utf-8");
        multipart.addBodyPart(htmlPart);

        for (MultipartFile file: files) {
            MimeBodyPart part = new MimeBodyPart();
            part.setContent(file.getBytes(),file.getContentType());
            part.setFileName(file.getOriginalFilename());
            part.setDescription(Part.ATTACHMENT);
            multipart.addBodyPart(part);
        }
        message.setContent(multipart);

        javaMailSender.send(message);
    }
}
