package com.stan.cryptoTrading.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    //Method for creating the mail(subject and body) and then send it
 //   public void sendVerification(String email, String otp) throws MessagingException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true, "utf-8");
//
//        String subject = "VerificationCode of OTP";
//        String body = "Your OTP is: " + otp;
//
//        mimeMessage.setFrom("waylorob@gmail.com");
//
//        mimeMessageHelper.setFrom("waylorob@gmail.com");
//        mimeMessageHelper.setTo(email);
//        mimeMessageHelper.setSubject(subject);
//        mimeMessageHelper.setText(body);
//
//        try {
//            javaMailSender.send(mimeMessage);
//        } catch (MailException mailException){
//            throw new MailSendException(mailException.getMessage());
//        }
//    }
    public void sendVerification(String email, String otp) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("waylorob@gmail.com");
        message.setTo(email);
        message.setSubject("VerificationCode of OTP");
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);

    }
}
