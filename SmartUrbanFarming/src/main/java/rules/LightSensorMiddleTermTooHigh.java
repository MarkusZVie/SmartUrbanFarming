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

@Rule(name = "LightSensorMiddleTermTooHigh", description = "Create Message Light Longterm depending")
public class LightSensorMiddleTermTooHigh {
	
	private final String ruleName = "LightSensorMiddleTermTooHigh";
	private final String factName1 = "MiddletTermsimplyAVG";
	private final String factName2 = "MiddletTermdifferenceAgainstExpectetLight";
	private final double lowMaximumValue = RulePreferences.getInstance().getLightlowMaximumValue();
	private final double mediumMaximumValue = RulePreferences.getInstance().getLightmediumMaximumValue();
	private final double highMaximumValue = RulePreferences.getInstance().getLighthighMaximumValue();
	
	@Condition
    public boolean when(@Fact(factName1) float avg, @Fact(factName2) float elight) {
		RuleManager rm = RuleManager.getInstance();
		ArrayList<String> cropFactNameList = rm.getFactNamesThatStartsWith("Crop.LIGHT.");
		String highestValue= "low";
		boolean returnValue = false;
		for(String s: cropFactNameList) {
			String newS = rm.getFact(s);
			if(highestValue.equals("low")) {
				highestValue = newS;
			}
			if(highestValue.equals("medium") && newS.equals("high")) {
				highestValue = newS;
			}
		}
		
		if(highestValue.equals("low")) {
			if(elight>lowMaximumValue) {
				returnValue=  true;
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("medium")) {
			if(elight>mediumMaximumValue) {
				returnValue =  true;
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("high")) {
			if(elight>highMaximumValue) {
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

    @Action
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
			String cropID = s.substring(s.lastIndexOf('.')+1);
			newCrop.add(cropID);
			newCrop.add(rm.getFact("Crop.LIGHT."+cropID));
			newCrop.add(rm.getFact(factName1));
			newCrop.add(rm.getFact(factName2));
			
			
			cropInformationList.add(newCrop);
		}
		
		for(ArrayList<String> cropAttributes: cropInformationList) {
			String subject = "Diese Woche war weniger Licht als erwartet";
			String message ="";
			if(cropAttributes.get(1).equals("low")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' hat die Licht Verträglichkeit von '"+cropAttributes.get(1)+"' hatte diese Woche jedoch "+(Double.parseDouble(cropAttributes.get(3)+"")-lowMaximumValue)*100+" % mehr licht als maximal zulässig (insgesamt "+((Double.parseDouble(cropAttributes.get(3)+""))*100)*(-1)+" % mehr als erwartet) und einen eine Durchschnitts Belichtung von "+cropAttributes.get(2)+".";
			}
			if(cropAttributes.get(1).equals("medium")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' hat die Licht Verträglichkeit von '"+cropAttributes.get(1)+"' hatte diese Woche jedoch "+(Double.parseDouble(cropAttributes.get(3)+"")-mediumMaximumValue)*100+" % mehr licht als maximal zulässig  (insgesamt "+((Double.parseDouble(cropAttributes.get(3)+""))*100)*(-1)+" % mehr als erwartet) und einen eine Durchschnitts Belichtung von "+cropAttributes.get(2)+".";
			}
			if(cropAttributes.get(1).equals("high")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' hat die Licht Verträglichkeit von '"+cropAttributes.get(1)+"' hatte diese Woche jedoch "+(Double.parseDouble(cropAttributes.get(3)+"")-highMaximumValue)*100+" % mehr licht als maximal zulässig  (insgesamt "+((Double.parseDouble(cropAttributes.get(3)+""))*100)*(-1)+" % mehr als erwartet) und einen eine Durchschnitts Belichtung von "+cropAttributes.get(2)+".";
			}
			
			Message m = new Message(new Date(), subject, message, ruleName);
			nh.addActiveMessage(m);
		}
    	
    	
	}
}
