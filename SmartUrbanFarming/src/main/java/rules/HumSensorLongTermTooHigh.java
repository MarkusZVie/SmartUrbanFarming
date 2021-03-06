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

@Rule(name = "HumSensorLongTermTooHigh", description = "Create Message Humidity Longterm depending")
public class HumSensorLongTermTooHigh {
	
	private final String ruleName = "HumSensorLongTermTooHigh";
	private final String factName = "HumLongTerm"; 
	private final String modelParameter = "WATER";
	private final String temporalModifer = "Dieses Monat"; //{'heute','diese Woche','diesem Monat'}
		
	private final double lowMaximumValue = RulePreferences.getInstance().getHumLowMaxValue();
	private final double mediumMaximumValue = RulePreferences.getInstance().getHumMediumMaxValue();
	private final double highMaximumValue = RulePreferences.getInstance().getHumHighMaxValue();
	
	
	@Condition
    public boolean when(@Fact(factName) float hum) {
		RuleManager rm = RuleManager.getInstance();
		ArrayList<String> cropFactNameList = rm.getFactNamesThatStartsWith("Crop."+modelParameter+".");
		String highestValue= "high";
		boolean returnValue = false;
		for(String s: cropFactNameList) {
			String newS = rm.getFact(s);
			if(highestValue.equals("high")) {
				highestValue = newS;
			}
			if(highestValue.equals("medium") && newS.equals("low")) {
				highestValue = newS;
			}
		}
		
		if(highestValue.equals("low")) {
			if(hum>lowMaximumValue) {
				returnValue=  true;//send message
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("medium")) {
			if(hum>mediumMaximumValue) {
				returnValue =  true;
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("high")) {
			if(hum>highMaximumValue) {
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
		ArrayList<String> cropFactNameList = rm.getFactNamesThatStartsWith("Crop."+modelParameter+".");
		for(String s: cropFactNameList) {
			ArrayList<String> newCrop = new ArrayList<String>();
			String cropID = s.substring(s.lastIndexOf('.')+1);
			newCrop.add(cropID);
			newCrop.add(rm.getFact("Crop."+modelParameter+"."+cropID));
			newCrop.add(rm.getFact(factName));
			
			
			cropInformationList.add(newCrop);
		}
		for(ArrayList<String> cropAttributes: cropInformationList) {
			String subject = temporalModifer +" ist die Luftfeuchtigkeit höher als benötigt";
			String message ="";
			if(cropAttributes.get(1).equals("low")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' braucht die Luftfeuchtigkeit '"+cropAttributes.get(1)+ "', stellen Sie bitte die Kühlung an, um eine ideale Umgebung für die Pflanzen zu gewährleisten." ;
			}
			if(cropAttributes.get(1).equals("medium")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' braucht die Luftfeuchtigkeit '"+cropAttributes.get(1)+ "', stellen Sie bitte die Kühlung an, um eine ideale Umgebung für die Pflanzen zu gewährleisten. " ;
			}
			if(cropAttributes.get(1).equals("high")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' braucht die Luftfeuchtigkeit '"+cropAttributes.get(1)+ "', stellen Sie bitte die Kühlung an, um eine ideale Umgebung für die Pflanzen zu gewährleisten. " ;
			}
			
			Message m = new Message(new Date(), subject, message, ruleName);
			nh.addActiveMessage(m);
		}
    	
    	
	}
}
