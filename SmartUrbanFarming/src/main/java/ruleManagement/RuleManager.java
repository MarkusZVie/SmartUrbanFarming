package ruleManagement;

import java.util.ArrayList;
import java.util.Date;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import Messages.Message;
import Messages.NotificationHandler;
import rules.SensorMeasurementsShortTerm;
import rules.TestRule;

public class RuleManager {
	
	private static RuleManager instance;
	private static Facts factbase;
	private static Rules rulebase;
	private static RulesEngine rulesEngine;
	private static ArrayList<String> factList;
	
	private RuleManager() {
		factbase = new Facts();
		rulebase = new Rules();
		rulesEngine = new DefaultRulesEngine();
		factList = new ArrayList<String>();
		registerRules();
	}
	


	//singelton
	public static RuleManager getInstance() {
		if(instance == null) {
			instance = new RuleManager();
		}
		return instance;
	}

	//all relevant rules should be registerd here
	private void registerRules() {
		rulebase.register(new TestRule()); //this is for later remove
		rulebase.register(new SensorMeasurementsShortTerm()); //this is for later remove
		
	}
	
	//add fact
	public synchronized void addFactToFactase(String name, Object factvalue ) {
		if(factList == null) {
			factList = new ArrayList<String>();
		}
		if(!factList.contains(name)) {
			factList.add(name);
		}
		factbase.put(name, factvalue);
	}

	public synchronized ArrayList<String> getFacts() {
		ArrayList<String> returnList = new ArrayList<String>();
		for(String s: factList) {
			returnList.add("Fact:" + s +" -> " + factbase.get(s));
		}
		return returnList;
	}
	
	public synchronized void fireRules(){
        rulesEngine.fire(rulebase, factbase);
		
	}
	
	public String getFact(String factName) {
		return factbase.get(factName).toString();
	
	}
	public ArrayList<String> getFactNamesThatStartsWith(String prefix){
		ArrayList<String> returnList = new ArrayList<String>();
		for(String s : factList) {
			if(s.startsWith(prefix)) {
				returnList.add(s);
			}
		}
		
		return returnList;
	}
}
