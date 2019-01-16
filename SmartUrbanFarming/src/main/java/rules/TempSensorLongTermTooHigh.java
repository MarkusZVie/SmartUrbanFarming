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

@Rule(name = "TempSensorLongTermTooHigh", description = "Create Message Temperature Shortterm depending")
public class TempSensorLongTermTooHigh {
	
	private final String ruleName = "TempSensorLongTermTooHigh";
	private final String factName = "TempLongTerm"; // {'HygroShortTerm','HygroMiddleTerm','HygroLongTerm'}
	private final String modelParameter = "TEMPERATURE";
	private final String temporalModifer = "dieses Monat"; //{'heute','diese Woche','diesem Monat'}
		
	private final double lowMaximumValue = RulePreferences.getInstance().getTempLowMaxValue();
	private final double mediumMaximumValue = RulePreferences.getInstance().getTempMediumMaxValue();
	private final double highMaximumValue = RulePreferences.getInstance().getTempHighMaxValue();
	
	
	@Condition
    public boolean when(@Fact(factName) float temp) {
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
			if(temp>lowMaximumValue) {
				returnValue=  true;//send message
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("medium")) {
			if(temp>mediumMaximumValue) {
				returnValue =  true;
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("high")) {
			if(temp>highMaximumValue) {
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
			String subject = temporalModifer +" die Temperatur höher als erwartet";
			String message ="";
			if(cropAttributes.get(1).equals("low")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' hat die Temperatur-Verträglichkeit von '"+cropAttributes.get(1)+"'es hatte "+temporalModifer+" jedoch "+(Double.parseDouble(cropAttributes.get(2)+"")-lowMaximumValue)*100+" % höhere Temperaturen als höchstens verträglich (insgesamt "+((Double.parseDouble(cropAttributes.get(2)+""))*100)+" % statt maximal "+ lowMaximumValue*100 + " %).";
			}
			if(cropAttributes.get(1).equals("medium")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' hat die Temperatur-Verträglichkeit von '"+cropAttributes.get(1)+"'es hatte "+temporalModifer+" jedoch "+(Double.parseDouble(cropAttributes.get(2)+"")-mediumMaximumValue)*100+" % höhere Temperaturen als höchstens verträglich (insgesamt "+((Double.parseDouble(cropAttributes.get(2)+""))*100)+" % statt maximal "+ mediumMaximumValue*100 + " %).";
			}
			if(cropAttributes.get(1).equals("high")) {
				message = "Die Pflanze: '"+cropAttributes.get(0)+"' hat die Temperatur-Verträglichkeit von '"+cropAttributes.get(1)+"'es hatte "+temporalModifer+" jedoch "+(Double.parseDouble(cropAttributes.get(2)+"")-highMaximumValue)*100+" % höhere Temperaturen als höchstens verträglich (insgesamt "+((Double.parseDouble(cropAttributes.get(2)+""))*100)+" % statt maximal "+ highMaximumValue*100 + " %).";
			}
			
			Message m = new Message(new Date(), subject, message, ruleName);
			nh.addActiveMessage(m);
		}
    	
    	
	}
}
