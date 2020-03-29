package com.nowcoder.communityy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailClient {

    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;//发送者

    /**
     * @param to      发送给谁
     * @param subject 邮件标题
     * @param content 邮件内容
     */
    public void sendMail(String to, String subject, String content) {
        try {
            //创建MimeMessage对象,构建一个邮件模板，添加内容之前是空的
            MimeMessage message = mailSender.createMimeMessage();
            //通过帮助类MimeMessageHelper构建邮件的详细的内容
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);//发件人
            helper.setTo(to);//对方
            helper.setSubject(subject);//设置邮件主题
            helper.setText(content, true);//邮件的格式，第二个参数为true，表示可以发送html格式的邮件
            mailSender.send(helper.getMimeMessage());//把helper构建好的MimeMessage对象传进JavaMailSender类的mailSender对象
        } catch (MessagingException e) {
            logger.error("发送邮件失败：" + e.getMessage());
        }
    }
}
