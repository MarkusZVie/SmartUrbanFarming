package mailing;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//source: https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/


public class NotificationMailer {
	
	private final static String username = "SmartUrbanFarmingUniVie@gmail.com";
	private final static String password = "%?_7M2Zd3cdba?zpYmTsCKV%cU5ujxq@";
	private static NotificationMailer instance;
	
	private static ArrayList<String> notificationMailList;
	
	
	private NotificationMailer() {
		//private constructor -> singelton
		notificationMailList = new ArrayList<String>();
	}
	
	



	public ArrayList<String> getNotificationMailList() {
		return notificationMailList;
	}

	public void addNotificationMailList(String mail) {
		this.notificationMailList.add(mail);
	}
	
	public void deleteNotificationMailList(String mail) {
		for(int i=0; i<notificationMailList.size(); i++) {
			if(notificationMailList.get(i).equals(mail)) {
				notificationMailList.remove(i);
			}
		}
	}





	public static NotificationMailer getInstance() {
		if(instance == null) {
			instance = new NotificationMailer();
		}
		return instance;
	}





	public static String sendMailToMailList(String subject, String text) {
		StringBuilder sb = new StringBuilder();
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {
			sb.append("MailSendTo:");
			System.out.println(notificationMailList.toString() + notificationMailList.size());
			for(String mail: notificationMailList) {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				message.addRecipient(Message.RecipientType.TO,new InternetAddress(mail));
				message.setSubject(subject);
				message.setText(text);
				Transport.send(message);
				sb.append(mail + ";");
			}
			

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
}
