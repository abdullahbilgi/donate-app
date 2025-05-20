package com.project.donate.mail;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class MailMessage implements Serializable {
    private String to;
    private String subject;
    private String message;
    private Map<String, byte[]> attachments;

    public MailMessage() {
        this.attachments = new HashMap<>();
    }

    public MailMessage(String to, String subject, String message) {
        this();
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public void addAttachment(String filename, byte[] content) {
        this.attachments.put(filename, content);
    }
}