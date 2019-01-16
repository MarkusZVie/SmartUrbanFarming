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
	
	private double tempLowMinValue;
	private double tempMediumMinValue;
	private double tempHighMinValue; 
	
	private double tempLowMaxValue;
	private double tempMediumMaxValue;
	private double tempHighMaxValue; 
	
	private double humLowMinValue;
	private double humMediumMinValue;
	private double humHighMinValue;
	
	private double humLowMaxValue;
	private double humMediumMaxValue;
	private double humHighMaxValue;
	
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
		
		tempLowMinValue = 5;
		tempMediumMinValue =	 15;
		tempHighMinValue = 25;
		
		tempLowMaxValue = 15;
		tempMediumMaxValue = 25;
		tempHighMaxValue = 35;
		
		humLowMinValue = 40;
		humMediumMinValue =	 60;
		humHighMinValue = 80;
		
		humLowMaxValue = 60;
		humMediumMaxValue = 80;
		humHighMaxValue = 90;
		
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


	public double getTemplowMinValue() {
		return tempLowMinValue ;
	}

	public double getTempMediumMinValue() {
		return tempMediumMinValue ;
	}

	public double getTempHighMinValue() {
		return tempHighMinValue ;
	}

	public double getTempLowMaxValue() {
		return tempLowMaxValue ;
	}

	public double getTempMediumMaxValue() {
		return tempMediumMaxValue ;
	}

	public double getTempHighMaxValue() {
		return tempHighMaxValue ;
	}	
	

	public double getHumlowMinValue() {
		return humLowMinValue ;
	}

	public double getHumMediumMinValue() {
		return humMediumMinValue ;
	}

	public double getHumHighMinValue() {
		return humHighMinValue ;
	}

	public double getHumLowMaxValue() {
		return humLowMaxValue ;
	}

	public double getHumMediumMaxValue() {
		return humMediumMaxValue ;
	}

	public double getHumHighMaxValue() {
		return humHighMaxValue ;
	}
	
	public static RulePreferences getInstance() {
		if(instance==null) {
			instance = new RulePreferences();
		}
		return instance;
	}

	
	
	
	
}
