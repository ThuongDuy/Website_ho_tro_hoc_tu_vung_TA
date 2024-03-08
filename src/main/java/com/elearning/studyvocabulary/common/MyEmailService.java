package com.elearning.studyvocabulary.common;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MyEmailService {
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	public void sendEmail(String to, String subject, String username, String password) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                messageHelper.setFrom("thuongduy2210@gmail.com");
                messageHelper.setTo(to);
                messageHelper.setSubject(subject);
 
                Context context = new Context();
                context.setVariable("username", username);
                context.setVariable("password", password);
                String content = templateEngine.process("email", context);
 
                messageHelper.setText(content, true);
            }
        };
 
        javaMailSender.send(preparator);
    }
}
