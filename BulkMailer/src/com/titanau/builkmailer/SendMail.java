package com.titanau.builkmailer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	
	public static void main(String[] args) throws IOException {

		//Config Information
		String username = "yourUserName";
		String password = "*************";
		String fileLocation = "FullPathOfTheFile\\emailInfo.csv";
		
		//Main Code Begins here
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,password);
				}
			});
		List<String>emailList = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
		String line = reader.readLine();
		Map<String,String>nameMap = new HashMap<>();
		while(null != line) {
			List<String>lineList = Arrays.asList(line.split(","));
			System.out.println(lineList);
			line = reader.readLine();
			emailList.add(lineList.get(0));
			nameMap.put(lineList.get(0), lineList.get(1));	
		}
		reader.close();
		for(String mail : emailList) {
			System.out.print("Sending Mail to : " + mail);
			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(mail));
				message.setSubject("Your Message Subject");
				StringBuilder mailBuilder = new StringBuilder();
				mailBuilder.append("Dear ").append(nameMap.get(mail)).append(", <br><br>");
				mailBuilder.append("ALL THE BEST....!");
				message.setContent(mailBuilder.toString(),"text/html; charset=utf-8");
				Transport.send(message);
				System.out.println(" - Done");
			} catch (MessagingException e) {
				System.out.print(" - Failed");
			}
		}
	}

}
