package Messages;

import java.util.ArrayList;

public class NotificationHandler {

	private static NotificationHandler nh;
	private ArrayList<Message> activeMessages;
	private ArrayList<Message> closedMessages;
	private long maxMessageID;
	
	private NotificationHandler() {
		activeMessages = new ArrayList<Message>();
		closedMessages = new ArrayList<Message>();
		maxMessageID=0;
	}
	
	
	public long getMaxMessageID() {
		return maxMessageID++;
	}

	public static NotificationHandler getInstance() {
		if(nh==null) {
			nh = new NotificationHandler();
		}
		return nh;
	}
	
	public void closeMessage(long messageID) {
		Message toSwap = null;
		for(Message m: activeMessages) {
			if(m.getMessageID()==messageID) {
				toSwap = m;
			}
		}
		activeMessages.remove(toSwap);
		closedMessages.add(toSwap);
	}
	
	public void activateMessage(long messageID) {
		Message toSwap = null;
		for(Message m: closedMessages) {
			if(m.getMessageID()==messageID) {
				toSwap = m;
			}
		}
		closedMessages.remove(toSwap);
		activeMessages.add(toSwap);
	}
	
	public ArrayList<Message> getActiveMessageByFactRelation(String factName){
		ArrayList<Message> returList = new ArrayList<Message>();
		
		for(Message m : activeMessages) {
			if(m.getRelatedFact().equals(factName)) {
				returList.add(m);
			}
		}
		return returList;
	}
	
	public void addActiveMessage(Message m) {
		activeMessages.add(m);
	}
	public String printAllActiveMessages() {
		StringBuilder sb = new StringBuilder();
		for(Message m:activeMessages) {
			sb.append(m.toString()+"\n\n");
		}
		return sb.toString();
	}


	public ArrayList<Message> getActiveMessages() {
		return activeMessages;
	}


	public ArrayList<Message> getClosedMessages() {
		return closedMessages;
	}


	public String getMail() {
		StringBuilder sb = new StringBuilder();
		for(Message m : activeMessages) {
			sb.append(m.getSubject());
			sb.append('\n');
			sb.append(m.getMessage());
			sb.append('\n');
			sb.append(m.getMessage());
			sb.append('\n');
			sb.append('\n');
			
		}
		return sb.toString();
	}
	
}
