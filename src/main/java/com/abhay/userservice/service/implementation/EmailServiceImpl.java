package com.abhay.userservice.service.implementation;

import com.abhay.userservice.service.EmailService;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static com.abhay.userservice.utils.EmailUtils.getEmailMessage;
import static com.abhay.userservice.utils.EmailUtils.getVerificationUrl;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String NEW_USER_ACCOUNT_VERIFICATION = "Welcome to the Frontend Team!";
    public static final String UTF_8 = "UTF-8";
    public static final String EMAIL_TEMPLATE = "EmailTemplate";
    private final JavaMailSender mailSender;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;


    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
    try{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setText(getEmailMessage(name, host, token));
        emailSender.send(message);

    }catch(Exception e){
        System.out.println(e.getMessage());
        throw new RuntimeException(e.getMessage());
    }
    }


    @Override
    @Async
    public void sendMimeMessageWithAttachment(String name, String to, String token) {
        try{
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8);
            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setText(getEmailMessage(name, host, token));
            // Adding attachments
            ClassPathResource img2 = new ClassPathResource("docs/Aggreement.pdf");
            ClassPathResource img1 = new ClassPathResource("images/generated-image.png");
            helper.addAttachment(Objects.requireNonNull(img1.getFilename()), img1);
            helper.addAttachment(Objects.requireNonNull(img2.getFilename()), img2);
            emailSender.send(message);

        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @Async
    public void sendMimeMessageWithEmbeddedImages(String name, String to, String token) {
        try{
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8);
            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setText(getEmailMessage(name, host, token));
            // Adding attachments
            ClassPathResource img2 = new ClassPathResource("docs/Aggreement.pdf");
            ClassPathResource img1 = new ClassPathResource("images/generated-image.png");
            helper.addInline(getContentId(img1.getFilename()), img1);
            helper.addInline(getContentId(img2.getFilename()), img2);
            emailSender.send(message);

        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }




    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try{
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", getVerificationUrl(host,token));
            String textMessage = templateEngine.process(EMAIL_TEMPLATE,context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8);
            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setText(textMessage,true);
            emailSender.send(message);

        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        try{
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", getVerificationUrl(host,token));
            String textMessage = templateEngine.process(EMAIL_TEMPLATE,context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8);
            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setText(textMessage,true);
            // Adding attachments
            MimeMultipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(textMessage,"text/html;charset=utf-8");
            multipart.addBodyPart(messageBodyPart);
            // Add Images to email body
            BodyPart imageBodyPart = new MimeBodyPart();
            System.out.println(System.getProperty("user.home"));
            DataSource dataSource = getDataSource();
            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID", "image");
            multipart.addBodyPart(imageBodyPart);

            message.setContent(multipart);
            emailSender.send(message);

        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private static DataSource getDataSource() {
        ClassPathResource imageResource = new ClassPathResource("images/generated-image.png");
        DataSource dataSource;

        try (InputStream inputStream = imageResource.getInputStream()) {
            dataSource = new ByteArrayDataSource(inputStream, "image/png");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image for email", e);
        }
        return dataSource;
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String fileName) {
        return "<"+fileName+">";
    }
}
