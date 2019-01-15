package rules;

import java.util.ArrayList;
import java.util.Date;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import Messages.Message;
import Messages.NotificationHandler;
import ruleManagement.RuleManager;

public class TestThing {
	
	private final String ruleName = "LightSensorShortTermTooLow";
	private final String factName1 = "ShortTermsimplyAVG";
	private final String factName2 = "ShortTermdifferenceAgainstExpectetLight";
	
    public boolean when(float avg, float elight) {
		RuleManager rm = RuleManager.getInstance();
		ArrayList<String> cropFactNameList = rm.getFactNamesThatStartsWith("Crop.LIGHT.");
		String highestValue= "low";
		boolean returnValue = false;
		for(String s: cropFactNameList) {
			if(highestValue.equals("low")) {
				highestValue = s;
			}
			if(highestValue.equals("medium") && s.equals("high")) {
				highestValue = s;
			}
		}
		if(highestValue.equals("low")) {
			if(elight>-0.6) {
				returnValue=  true;
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("medium")) {
			if(elight>-0.4) {
				returnValue =  true;
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("high")) {
			if(elight>-0.2) {
				returnValue = true;
			}else {
				returnValue = false;
			}
		}
		if(returnValue) {
			return true;
		}else {
			NotificationHandler nh = NotificationHandler.getInstance();
			ArrayList<Message> factRelatedList = nh.getActiveMessageByFactRelation(ruleName);
			for(Message m : factRelatedList) {
				nh.closeMessage(m.getMessageID());
			}
			return false;
		}
        
    }

    public void then() throws Exception {
    	RuleManager rm = RuleManager.getInstance();
    	NotificationHandler nh = NotificationHandler.getInstance();
    	//remove previous messages
    	ArrayList<Message> factRelatedList = nh.getActiveMessageByFactRelation(ruleName);
		for(Message m : factRelatedList) {
			nh.closeMessage(m.getMessageID());
		}
    	ArrayList<ArrayList<String>> cropInformationList = new  ArrayList<ArrayList<String>>();
		ArrayList<String> cropFactNameList = rm.getFactNamesThatStartsWith("Crop.LIGHT.");

		for(String s: cropFactNameList) {
			ArrayList<String> newCrop = new ArrayList<String>();
			newCrop.add(s.substring(s.lastIndexOf('.')+1));
			cropInformationList.add(newCrop);
		}

		System.out.println(cropInformationList.toString());
		
    	String subject = "Heute weniger Licht als erwartet";
		String message = "";
		
		Message m = new Message(new Date(), subject, message, ruleName);
    	//nh.addActiveMessage(m);
	}
}
