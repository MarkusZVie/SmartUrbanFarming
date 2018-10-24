package rules;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "Test Rule", description = "Do something")
public class TestRule {
	
	@Condition
    public boolean when(@Fact("value") int value) {
		if(value > 5) {
			return true;
		}else {
			return false;
		}
        
    }

    @Action
    public void then() throws Exception {
        System.out.println("value is over 5");
    }
}
