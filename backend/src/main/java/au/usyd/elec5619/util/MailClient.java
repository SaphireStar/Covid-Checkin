package au.usyd.elec5619.util;

import java.io.Serializable;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
/**
 * Send email support. 
 * 
 * @author Yonghe Tan
 */
@Component
public class MailClient implements Serializable {
	
	private static final Logger logger = LoggerFactory.getLogger(MailClient.class);
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SimpleMailMessage simpleMailMessage;
	
	public void sendMail(String userbox, String subject, String content) {
		simpleMailMessage.setTo(userbox);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		logger.info("Send an email notification to:", userbox);
		javaMailSender.send(simpleMailMessage);
	}
}
