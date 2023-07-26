package com.imbuka.userservice.service.impl;

import com.imbuka.userservice.service.EmailService;
import com.imbuka.userservice.util.EmailUtil;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;
import java.util.Objects;

import static com.imbuka.userservice.util.EmailUtil.*;

@Service
@RequiredArgsConstructor
//The Async annotation will ensure methods will run in a separate thread
@Async
public class EmailServiceImpl implements EmailService {
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailtemplate";
    public static final String TEXT_HTML_ENCODING = "text/html";
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getEmailMesaage(name, host, token));
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMesaage(name, host, token));
            //Add attachments
            FileSystemResource google = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/google.jpeg"));
            FileSystemResource bed = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/futurebed.jpeg"));
            FileSystemResource resume = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/resume.pdf"));
            helper.addAttachment(Objects.requireNonNull(google.getFilename()), google);
            helper.addAttachment(Objects.requireNonNull(bed.getFilename()), bed);
            helper.addAttachment(Objects.requireNonNull(resume.getFilename()), resume);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachmentsFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMesaage(name, host, token));
            //Add attachments
            FileSystemResource google = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/google.jpeg"));
            FileSystemResource bed = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/futurebed.jpeg"));
            FileSystemResource resume = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/resume.pdf"));
            helper.addInline(getContentId(google.getFilename()), google);
            helper.addInline(getContentId(bed.getFilename()), bed);
            helper.addInline(getContentId(resume.getFilename()), resume);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            Context context = new Context();
//            context.setVariable("name", name);
//            context.setVariable("url", getVerificationUrl(host, token));
            //combine the above two lines to one using hashmap
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            //Add attachments
           /* FileSystemResource google = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/google.jpeg"));
            FileSystemResource bed = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/futurebed.jpeg"));
            FileSystemResource resume = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/resume.pdf"));
            helper.addInline(getContentId(google.getFilename()), google);
            helper.addInline(getContentId(bed.getFilename()), bed);
            helper.addInline(getContentId(resume.getFilename()), resume);*/
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @Async
    public void sendHtmlEmailwithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
           // helper.setText(text, true);
            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            //Add attachments
           /* FileSystemResource google = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/google.jpeg"));
            FileSystemResource bed = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/futurebed.jpeg"));
            FileSystemResource resume = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/images/resume.pdf"));
            helper.addInline(getContentId(google.getFilename()), google);
            helper.addInline(getContentId(bed.getFilename()), bed);
            helper.addInline(getContentId(resume.getFilename()), resume);*/

            //Add Html Email Body
            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
            mimeMultipart.addBodyPart(messageBodyPart);

            //Add images to the email body
            BodyPart imageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(System.getProperty("user.home") + "/Pictures/images/futurebed.jpeg");
            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID", "image");
            mimeMultipart.addBodyPart(imageBodyPart);

            message.setContent(mimeMultipart);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private MimeMessage getMimeMessage() {
        return mailSender.createMimeMessage();
    }

    private String getContentId(String fileName) {
        return "<" + fileName + ">";
    }
}
