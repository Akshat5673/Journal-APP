package com.nineleaps.journalApp.Services.Impls;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final String from;

    @Autowired
    public EmailService(JavaMailSender javaMailSender,
                        @Value("${spring.mail.username}") String from) {
        this.javaMailSender = javaMailSender;
        this.from = from;
    }

    public void sendEmail(String to, String subject, String body){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            javaMailSender.send(mailMessage);
            log.info("Mail sent successfully !");
        }catch (Exception e){
            log.error("Error while sending email : ", e);
        }
    }

}
