package rules;

public class RulePreferences {
	
	private static RulePreferences instance;
	private double lightlowMinimumValue;
	private double lightmediumMinimumValue;
	private double lighthighMinimumValue;
	private double lightlowMaximumValue;
	private double lightmediumMaximumValue;
	private double lighthighMaximumValue;
	
	private double hygroLowMinValue;
	private double hygroMediumMinValue;
	private double hygroHighMinValue;
	
	private double hygroLowMaxValue;
	private double hygroMediumMaxValue;
	private double hygroHighMaxValue;
	
	
	private RulePreferences() {
		lightlowMinimumValue = -0.5;
		lightmediumMinimumValue = -0.4;
		lighthighMinimumValue = -0.3;
		
		lightlowMaximumValue= -0.1;
		lightmediumMaximumValue=0;
		lighthighMaximumValue=0.2;
		
		hygroLowMinValue =  0.4;
		hygroMediumMinValue = 0.55;
		hygroHighMinValue = 0.7;
		
		hygroLowMaxValue =  0.8;
		hygroMediumMaxValue = 0.9;
		hygroHighMaxValue = 0.95;
		
	}
	
	
	
	public double getHygroLowMinValue() {
		return hygroLowMinValue;
	}



	public double getHygroMediumMinValue() {
		return hygroMediumMinValue;
	}



	public double getHygroHighMinValue() {
		return hygroHighMinValue;
	}



	public double getHygroLowMaxValue() {
		return hygroLowMaxValue;
	}



	public double getHygroMediumMaxValue() {
		return hygroMediumMaxValue;
	}



	public double getHygroHighMaxValue() {
		return hygroHighMaxValue;
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
