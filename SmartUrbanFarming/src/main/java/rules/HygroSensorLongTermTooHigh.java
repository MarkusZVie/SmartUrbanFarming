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

@Rule(name = "HygroSensorLongTermTooHigh", description = "Create Message Hygrometer Shortterm depending")
public class HygroSensorLongTermTooHigh {
	
	private final String ruleName = "HygroSensorLongTermTooHigh";
	private final String factName = "HygroLongTerm"; // {'HygroShortTerm','HygroMiddleTerm','HygroLongTerm'}
	private final String modelParameter = "WATER";
	private final String temporalModifer = "dieses Monat"; //{'heute','diese Woche','diesem Monat'}
		
	private final double lowMaximumValue = RulePreferences.getInstance().getHygroLowMaxValue();
	private final double mediumMaximumValue = RulePreferences.getInstance().getHygroMediumMaxValue();
	private final double highMaximumValue = RulePreferences.getInstance().getHygroHighMaxValue();
	
	
	@Condition
    public boolean when(@Fact(factName) float hygro) {
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
			if(hygro>lowMaximumValue) {
				returnValue=  true;//send message
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("medium")) {
			if(hygro>mediumMaximumValue) {
				returnValue =  true;
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("high")) {
			if(hygro>highMaximumValue) {
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
			String subject = temporalModifer +" war mehr Feuchtigkeit als erwartet";
			String message ="";
			if(cropAttributes.get(1).equals("low")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' hat die Feuchtikeits-Verträglichkeit von '"+cropAttributes.get(1)+"' hatte "+temporalModifer+" jedoch "+(Double.parseDouble(cropAttributes.get(2)+"")-lowMaximumValue)*100+" % mehr Feuchtigkeit als höchst verträglich (insgesamt "+((Double.parseDouble(cropAttributes.get(2)+""))*100)+" % statt maximal "+ lowMaximumValue*100 + " %).";
			}
			if(cropAttributes.get(1).equals("medium")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' hat die Feuchtikeits-Verträglichkeit von '"+cropAttributes.get(1)+"' hatte "+temporalModifer+" jedoch "+(Double.parseDouble(cropAttributes.get(2)+"")-mediumMaximumValue)*100+" % mehr Feuchtigkeit als höchst verträglich (insgesamt "+((Double.parseDouble(cropAttributes.get(2)+""))*100)+" % statt maximal "+ mediumMaximumValue*100 + " %).";
			}
			if(cropAttributes.get(1).equals("high")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' hat die Feuchtikeits-Verträglichkeit von '"+cropAttributes.get(1)+"' hatte "+temporalModifer+" jedoch "+(Double.parseDouble(cropAttributes.get(2)+"")-highMaximumValue)*100+" % mehr Feuchtigkeit als höchst verträglich (insgesamt "+((Double.parseDouble(cropAttributes.get(2)+""))*100)+" % statt maximal "+ highMaximumValue*100 + " %).";
			}
			
			Message m = new Message(new Date(), subject, message, ruleName);
			nh.addActiveMessage(m);
		}
    	
    	
	}
}
