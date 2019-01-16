package rules;

public class RulePreferences {
	
	private static RulePreferences instance;
	private double lightlowMinimumValue;
	private double lightmediumMinimumValue;
	private double lighthighMinimumValue;
	private double lightlowMaximumValue;
	private double lightmediumMaximumValue;
	private double lighthighMaximumValue;
	
	private RulePreferences() {
		lightlowMinimumValue = -0.5;
		lightmediumMinimumValue = -0.4;
		lighthighMinimumValue = -0.3;
		
		lightlowMaximumValue= -0.1;
		lightmediumMaximumValue=0;
		lighthighMaximumValue=0.2;
		
	}
	
	public double getLightlowMinimumValue() {
		return lightlowMinimumValue;
	}

	public double getLightmediumMinimumValue() {
		return lightmediumMinimumValue;
	}

	public double getLighthighMinimumValue() {
		return lighthighMinimumValue;
	}

	public double getLightlowMaximumValue() {
		return lightlowMaximumValue;
	}

	public double getLightmediumMaximumValue() {
		return lightmediumMaximumValue;
	}

	public double getLighthighMaximumValue() {
		return lighthighMaximumValue;
	}

	public static RulePreferences getInstance() {
		if(instance==null) {
			instance = new RulePreferences();
		}
		return instance;
	}

	
	
	
	
}
