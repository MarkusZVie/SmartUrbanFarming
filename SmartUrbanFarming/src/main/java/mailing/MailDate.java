package mailing;

import java.util.Date;

public class MailDate {
	private String mail;
	private Date date;
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public MailDate(String mail, Date date) {
		super();
		this.mail = mail;
		this.date = date;
	}
	
	
}
