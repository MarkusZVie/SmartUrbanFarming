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

@Rule(name = "LightSensorShortTermTooLow", description = "Create Message Light Shortterm depending")
public class LightSensorShortTermTooLow {
	
	private final String ruleName = "LightSensorShortTermTooLow";
	private final String factName1 = "ShortTermsimplyAVG";
	private final String factName2 = "ShortTermdifferenceAgainstExpectetLight";
	
	@Condition
    public boolean when(@Fact(factName1) float avg, @Fact(factName2) float elight) {
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
			if(elight>-0.8) {
				returnValue =  true;
			}else {
				returnValue = false;
			}
		}
		if(highestValue.equals("high")) {
			if(elight>-0.9) {
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
    	
		ArrayList<String> cropFactNameList = rm.getFactNamesThatStartsWith("Crop.LIGHT.");
		for(String s: cropFactNameList) {
			rm.getFact(s);
		}
		
    	String subject = "Heute weniger Licht als erwartet";
		String message = "";
		
		Message m = new Message(new Date(), subject, message, ruleName);
    	//nh.addActiveMessage(m);
	}
}
