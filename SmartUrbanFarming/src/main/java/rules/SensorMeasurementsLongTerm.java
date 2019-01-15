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

@Rule(name = "LongTermpercentOfMosurementsDone", description = "Create Message if Mesarements are abitary")
public class SensorMeasurementsLongTerm {
	
	private final String factName = "LongTermpercentOfMosurementsDone";
	
	public SensorMeasurementsLongTerm() {
		

	}

	@Condition
    public boolean when(@Fact(factName) float pOfM) {
		if(pOfM < 0.8) {
			return true;
		}else {
			NotificationHandler nh = NotificationHandler.getInstance();
			ArrayList<Message> factRelatedList = nh.getActiveMessageByFactRelation(factName);
			for(Message m : factRelatedList) {
				nh.closeMessage(m.getMessageID());
			}
			return false;
		}
        
    }

    @Action
    public void then() throws Exception {
    	NotificationHandler nh = NotificationHandler.getInstance();
		RuleManager rm = RuleManager.getInstance();
		
    	ArrayList<Message> factRelatedList = nh.getActiveMessageByFactRelation(factName);
		for(Message m : factRelatedList) {
			nh.closeMessage(m.getMessageID());
		}
		
		String subject = "Dieses Monat signifikant weniger Sensordaten als erwartet";
		String message = "";
		String number = "";
		if(Float.parseFloat(rm.getFact(factName))*100<0) {
			number="< 1";
		}else {
			double n = (Float.parseFloat(rm.getFact(factName))*100);
			number =  n +"";
		}
		
		if(Boolean.parseBoolean(rm.getFact("MiddletTermTimeAdjusted")))
		{
			message="Seit Beginn der Messungen am " + rm.getFact("StartOfMonitoring") +" wurden nur "+ number +" % der Messungen durchgeführt. \n"
					+ "Aufgrund von einer geringeren Datenmenge sind die Vorhersagen und Einschätzungen über den Status des Moduls nicht so exakt. ";
		}else {
			message="Seit letzdem Monat wurden nur "+ number +" % der Messungen durchgeführt. \n"
					+ "Aufgrund von einer geringeren Datenmenge sind die Vorhersagen und Einschätzungen über den Status des Moduls nicht so exakt. ";
			
		}
		Message m = new Message(new Date(), subject, message, factName);
    	nh.addActiveMessage(m);
	}
}
