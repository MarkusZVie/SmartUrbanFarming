package rules;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import mailing.NotificationMailer;
import servlets.NotificationSettings;

@Rule(name = "Test Rule", description = "Do something")
public class TestRule {
	
	@Condition
    public boolean when(@Fact("light") float light) {
		if(light < 0.1) {
			return true;
		}else {
			return false;
		}
        
    }

    @Action
    public void then() throws Exception {
        System.out.println("--------------->Light is under 10 %");
        System.out.println("--------------->Send Mail to MailList");
        System.out.println("--------------->"+NotificationMailer.getInstance().sendMailToMailList("Light Is under 10%", "Hello guys \n the light is under 10% \n today is the"+ new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(new Date())));
    }
}
