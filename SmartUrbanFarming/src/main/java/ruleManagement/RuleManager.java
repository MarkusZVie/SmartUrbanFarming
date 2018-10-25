package ruleManagement;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import rules.TestRule;

public class RuleManager {
	public void fireRules(){
		 // create facts
        Facts facts = new Facts();
        facts.put("value", 19);
        // create rules
        Rules rules = new Rules();
        rules.register(new TestRule());
        
        // create a rules engine and fire rules on known facts
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);
		
	}
}
