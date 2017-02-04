package ru.jleague13.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jleague13.repository.ParamDao;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author ashevenkov 04.02.17 13:21.
 */
@Service
public class YandexMail {

    private Log log = LogFactory.getLog(YandexMail.class);

    @Autowired
    private ParamDao paramDao;


    public void sendMail(String to, String subject, String text) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.yandex.ru");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    paramDao.getParam("mail.user"),
                                    paramDao.getParam("mail.password"));
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("alterloki@yandex.ru"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e) {
            log.error("Error sending mail.", e);
        }
    }
}
