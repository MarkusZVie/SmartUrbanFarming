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

@Rule(name = "SensorMeasurementsShortTerm", description = "Create Message if Mesarements are abitary")
public class SensorMeasurementsShortTerm {
	
	private final String factName = "ShortTermpercentOfMosurementsDone";
	
	public SensorMeasurementsShortTerm() {
		

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
		
		String subject = "Heute signifikant weniger Sensordaten als erwartet";
		String message = "";
		if(Boolean.parseBoolean(rm.getFact("ShortTermTimeAdjusted")))
		{
			message="Seit Beginn der Messungen am " + rm.getFact("StartOfMonitoring") +" wurden nur "+ Float.parseFloat(rm.getFact(factName))*100 +" % der Messungen durchgeführt. \n"
					+ "Aufgrund von einer geringeren Datenmenge sind die Vorhersagen und Einschätzungen über den Status des Moduls nicht so exakt. ";
		}else {
			message="Seit gestern wurden nur "+ Float.parseFloat(rm.getFact(factName))*100 +" % der Messungen durchgeführt. \n"
					+ "Aufgrund von einer geringeren Datenmenge sind die Vorhersagen und Einschätzungen über den Status des Moduls nicht so exakt. ";
			
		}
		Message m = new Message(new Date(), subject, message, factName);
    	nh.addActiveMessage(m);
	}
}
