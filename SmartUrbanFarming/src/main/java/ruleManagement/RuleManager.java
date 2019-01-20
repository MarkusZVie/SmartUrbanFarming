package ruleManagement;

import java.util.ArrayList;
import java.util.Date;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import Messages.Message;
import Messages.NotificationHandler;
import rules.HygroSensorLongTermTooHigh;
import rules.HygroSensorLongTermTooLow;
import rules.HygroSensorMiddleTermTooHigh;
import rules.HygroSensorMiddleTermTooLow;
import rules.HygroSensorShortTermTooHigh;
import rules.HygroSensorShortTermTooLow;
import rules.LightSensorLongTermTooHigh;
import rules.LightSensorLongTermTooLow;
import rules.LightSensorMiddleTermTooHigh;
import rules.LightSensorMiddleTermTooLow;
import rules.LightSensorShortTermTooHigh;
import rules.LightSensorShortTermTooLow;
import rules.TempSensorLongTermTooHigh;
import rules.SensorMeasurementsLongTerm;
import rules.SensorMeasurementsMiddleTerm;
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
		rulebase.register(new SensorMeasurementsMiddleTerm());
		rulebase.register(new SensorMeasurementsLongTerm());
		
		rulebase.register(new LightSensorShortTermTooLow());
		rulebase.register(new LightSensorMiddleTermTooLow());
		rulebase.register(new LightSensorLongTermTooLow());
		rulebase.register(new LightSensorLongTermTooHigh());
		rulebase.register(new LightSensorMiddleTermTooHigh());
		rulebase.register(new LightSensorShortTermTooHigh());
		
		rulebase.register(new HygroSensorLongTermTooHigh());
		rulebase.register(new HygroSensorLongTermTooLow());
		rulebase.register(new HygroSensorMiddleTermTooHigh());
		rulebase.register(new HygroSensorMiddleTermTooLow());
		rulebase.register(new HygroSensorShortTermTooHigh());
		rulebase.register(new HygroSensorShortTermTooLow());
		
		rulebase.register(new TempSensorLongTermTooHigh());
		
		
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
        /*
        TestThing t = new TestThing();
        if(t.when((float)0.20655714, (float)-0.793368)) {
        	try {
				t.then();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else {
        	try {
				t.then();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        */
        //System.out.println(NotificationHandler.getInstance().printAllActiveMessages());
        
		
	}
	
	public String getFact(String factName) {
		return factbase.get(factName).toString();
	
	}
	public ArrayList<String> getFactList(){
		return factList;
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
	public ArrayList<String> getFactNamesThatContainsThis(ArrayList<String> al){
		ArrayList<String> returnList = new ArrayList<String>();
		for(String s : factList) {
			boolean shouldBeAdded = true;
			for(String ss : al) {
				if(!s.contains(ss)) {
					shouldBeAdded = false;
				}
			}
			if(shouldBeAdded) {
				returnList.add(s);
			}
		}
		return returnList;
	}
	
}
