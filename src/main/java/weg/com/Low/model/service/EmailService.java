package weg.com.Low.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String destinatario, String assunto, String corpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lowdemandasweg@gmail.com");
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(corpo);
        javaMailSender.send(message);
    }

}
