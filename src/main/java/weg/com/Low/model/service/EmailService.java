//package weg.com.Low.model.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.util.Date;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    public void sendEmail(String destinatario, String assunto, String corpo) {
////        SimpleMailMessage message = new SimpleMailMessage();
////        message.setFrom("lowdemandasweg@gmail.com");
////        message.setTo(destinatario);
////        message.setSubject(assunto);
////        message.setText(corpo);
////        message.setText("<html><body><h1>Este é um e-mail HTML simples</h1>"
////                + "<p>Este é um parágrafo em HTML.</p></body></html>");
////        message.setSentDate(new Date());
//
//        String content = "<html><body><h1>Título da mensagem</h1>"
//                + "<p>Este é um parágrafo em HTML.</p></body></html>";
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//            helper.setFrom("lowdemandasweg@gmail.com");
//            helper.setTo(destinatario);
//            helper.setSubject(assunto);
//            helper.setText(content, true); // Define o conteúdo como HTML
//            javaMailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//
////        javaMailSender.send(message);
//    }
//
//}
