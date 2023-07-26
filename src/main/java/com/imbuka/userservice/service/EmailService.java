package com.imbuka.userservice.service;

public interface EmailService {
    void sendSimpleMailMessage(String name, String to, String token);
    void sendMimeMessageWithAttachments(String name, String to, String token);
     void sendMimeMessageWithAttachmentsFiles(String name, String to, String token);
    void sendHtmlEmail(String name, String to, String token);
    void sendHtmlEmailwithEmbeddedFiles(String name, String to, String token);

}
