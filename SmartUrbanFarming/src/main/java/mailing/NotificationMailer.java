package mailing;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import Messages.NotificationHandler;

//source: https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/


public class NotificationMailer {
	
	private final static String username = "SmartUrbanFarmingUniVie@gmail.com";
	private final static String password = "%?_7M2Zd3cdba?zpYmTsCKV%cU5ujxq@";
	private static NotificationMailer instance;
	
	private static ArrayList<MailDate> notificationMailList;
	
	
	private NotificationMailer() {
		//private constructor -> singelton
		notificationMailList = new ArrayList<MailDate>();
	}
	
	



	public ArrayList<String> getNotificationMailList() {
		ArrayList<String> returnList= new ArrayList<String>();
		for(MailDate md: notificationMailList) {
			returnList.add(md.getMail());
		}
		return returnList;
	}

	public void addNotificationMailList(String mail) {
		this.notificationMailList.add(new MailDate(mail, null));
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








	public void doMail() {
		if(NotificationHandler.getInstance().getActiveMessages().size()>0) {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost("http://volquir90.bplaced.net/mailer.php");
	
			for(MailDate mail: notificationMailList) {
				boolean sendMail=false;
				
				if(mail.getDate()==null) {
					sendMail=true;
				}else {
					if(mail.getDate().getTime()< (new Date().getTime()-(60*60*1000*24))) {
						sendMail=true;
					}else {
					}
				}
					
				if(sendMail) {
					// Request parameters and other properties.
					List<NameValuePair> params = new ArrayList<NameValuePair>(2);
					params.add(new BasicNameValuePair("apiKey", "sjhf8938hrssd!!!§$l98h92bf9h2989hg"));
					params.add(new BasicNameValuePair("text", NotificationHandler.getInstance().getMail()));
					params.add(new BasicNameValuePair("subject", "SmartUrbanFarming Notification"));
					params.add(new BasicNameValuePair("mailTo", mail.getMail()));
					try {
						httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
						//Execute and get the response.
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
		
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mail.setDate(new Date());
				}
			}
		}
		
		

		
		
		
	}
}
