package com.nowcoder.communityy;

import com.nowcoder.communityy.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityyApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;//使用模板引擎，如果是springmvc的话会自动封装数据模型

    @Test
    public void testText() {
        mailClient.sendMail("jiasn.zhou@foxmail.com", "test", "welcome");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();//使用模板引擎的context设置数据模型【org.thymeleaf.context.Context】
        context.setVariable("username", "风清扬");
        //把已设置了数据的context传进templateEngine引擎类加载，文件/mail/demo是要发送的template类型对象
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);//打印内容到控制台测试

        //调用发邮件功能
        mailClient.sendMail("jiasn.zhou@foxmail.com", "html", content);
    }
}
