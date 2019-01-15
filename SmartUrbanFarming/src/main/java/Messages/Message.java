package Messages;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
	private Date creationDate;
	private String subject;
	private String relatedFact;
	

	private String message;
	private long messageID;
	
	public Message() {
		messageID=NotificationHandler.getInstance().getMaxMessageID();
	}
	
	

	public Message(Date creationDate, String subject, String message, String relatedFact) {
		super();
		this.creationDate = creationDate;
		this.subject = subject;
		this.message = message;
		this.relatedFact = relatedFact;
	}
	
	public long getMessageID() {
		return messageID;
	}
	
	public String getRelatedFact() {
		return relatedFact;
	}

	public void setRelatedFact(String relatedFact) {
		this.relatedFact = relatedFact;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



	@Override
	public String toString() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		StringBuilder sb = new StringBuilder();
		sb.append("Erstell Datum: " + sdf.format(creationDate) + "\n");
		sb.append("Betreff: " + subject + "\n");
		sb.append("Message:" + "\n" + message);

		return sb.toString();
	}
	
	
	
	
}
