package com.ps;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Example {

    public static void main(String[] args) {
        sendEmail();
        getAllInboxMessagesViaPOP3();
    }

    private static void sendEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        String username = "pogrebnij@gmail.com";
        String password = "****";
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getInstance(props, authenticator);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pogrebnij@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("pavlo.pohrebnyi@asg.com"));
            message.setSubject("Testing Subject");
            message.setText("Hello Mr. Pavlo");
            Transport.send(message);
            System.out.println("Message is sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getAllInboxMessagesViaPOP3() {
        String host = "pop.gmail.com";
        String mailStoreType = "pop3";
        String username = "pogrebnij@gmail.com";
        String password = "****";
        Properties properties = new Properties();
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        try {
            Session emailSession = Session.getDefaultInstance(properties);
            // Create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");
            store.connect(host, username, password);
            // Create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            // Retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("Retrieved " + messages.length + " messages");
            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());
            }
            // Close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
