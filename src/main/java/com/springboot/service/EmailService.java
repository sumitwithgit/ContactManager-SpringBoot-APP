package com.springboot.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService 
{
	public boolean sendEmail(String message,String subject,String to)
	{
		boolean f=false;
		
		String from="chakravartisumit11@gmail.com";
		
		String host="smtp.gmail.com";
		
		Properties properties = System.getProperties();
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(from, "***********");
			}
			
		});
		
		session.setDebug(true);
		
		
		
		MimeMessage mimeMessage=new MimeMessage(session);
		
		try {
//			set from 
			mimeMessage.setFrom(from);
			
//			set recipient
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			
//			set subject
			mimeMessage.setSubject(subject);
			
//			set  content 
			
			mimeMessage.setText(message);
			
			Transport.send(mimeMessage);
			
			f=true;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return f;
	}
}
