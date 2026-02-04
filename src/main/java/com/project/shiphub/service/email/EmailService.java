package com.project.shiphub.service.email;

public interface EmailService {
    void sendEmail(String to, String subject, String message);
}
