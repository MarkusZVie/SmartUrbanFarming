
// Include the correct display library
// For a connection via I2C using Wire include
#include <Wire.h> // Only needed for Arduino 1.6.5 and earlier
#include "SSD1306.h" // legacy include: `#include "SSD1306Wire.h"`
#include <SimpleDHT.h>
int pinDHT22 = 17;
SimpleDHT22 dht22(pinDHT22);

// light
int ldrPin = 35;
int analog_value = 0;

// Hygrometer
int hygPin = 32;
int hygrometer_value = 0;

SSD1306 display(0x3c, 5, 4);

void setup()
{
    // light
    pinMode(ldrPin, INPUT);

    Serial.begin(9600);
    delay(2000);
    // Initialising the UI will init the display too.
    display.init();
    display.flipScreenVertically();
    display.setFont(ArialMT_Plain_10);
}

int readHygrometer()
{
    int hygrometer_value = analogRead(hygPin);
    // Serial.print("Hygrometer: ");
    // Serial.println(hygrometer_value);
    return hygrometer_value;
}

int readLight()
{
    int analog_value = analogRead(ldrPin);
    // Serial.print("Light: ");
    // Serial.println(analog_value);
    return analog_value;
}

float readTemperatur()
{
    float temperature = 0;
    float humidity = 0;
    int err = SimpleDHTErrSuccess;
    if ((err = dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess)
    {
        Serial.print("Read DHT22 failed, err=");
        Serial.println(err);
        delay(2000);
        return -1;
    }
    // Serial.print("Sample OK: ");
    // Serial.print((float)temperature); Serial.print(" *C, ");
    // Serial.print((float)humidity); Serial.println(" RH%");
    return (float)temperature;
}

float readHumidity()
{
    float temperature = 0;
    float humidity = 0;
    int err = SimpleDHTErrSuccess;
    if ((err = dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess)
    {
        Serial.print("Read DHT22 failed, err=");
        Serial.println(err);
        delay(2000);
        return -1;
    }
    // Serial.print("Sample OK: ");
    // Serial.print((float)temperature); Serial.print(" *C, ");
    // Serial.print((float)humidity); Serial.println(" RH%");
    return (float)humidity;
}

float* readDHT()
{
    float temperature = 0;
    float humidity = 0;
    float arr[2] = { -1, -1 };
    int err = SimpleDHTErrSuccess;
    if ((err = dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess)
    {
        Serial.print("Read DHT22 failed, err=");
        Serial.println(err);
        delay(2000);

        return arr;
    }
    arr[0] = temperature;
    arr[1] = humidity;
    return arr;
    // Serial.print("Sample OK: ");
    // Serial.print((float)temperature); Serial.print(" *C, ");
    // Serial.print((float)humidity); Serial.println(" RH%");
}

void writeText(String s)
{
    display.setFont(ArialMT_Plain_10);
    display.setTextAlignment(TEXT_ALIGN_LEFT);
    display.drawStringMaxWidth(0, 0, 128, s);
}

long startTime = millis();
String received = "none";


int timeIntervalForAVGRead = 20; // in ms
int targetIntervalTime = 1000; // in ms
int numberOfReads = targetIntervalTime/timeIntervalForAVGRead;
int targetGeneralIntervalTIme = 10000; // in ms
int timeIntervalForGeneralMesure = (targetGeneralIntervalTIme) - targetIntervalTime; // ReadInterval - AVG Mesure time  -> target 1-5 min

void loop()
{
    while (Serial.available() == 0)
    {
        // read N times and make the AVG
        
		
		//stailising the read by average on 10 Secund read
		float finalTemperature = 0;
        float finalHumidity = 0;
		int finalLight = 0;
        int finalHygro = 0;
		
		//hum and temp are not avg
		float temperature = 0;
        float humidity = 0;
        int err = SimpleDHTErrSuccess;
        if ((err = dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess)
        {
            Serial.print("Read DHT22 failed, err=");
            Serial.println(err);
            delay(2000);
            temperature = -1;
            humidity = -1;
        }
		
        for (int i = 0; i <= numberOfReads; i++)
        {
            delay(timeIntervalForAVGRead);
            // read sensors
            
            int light = readLight();
            int hygro = readHygrometer();
			
			//sum the values upper_bound
			finalLight += light;
			finalHygro += hygro;
			
        }
		
		//make the AVG out of the sum
		finalTemperature = temperature;
		finalHumidity = humidity;
		finalLight = finalLight/numberOfReads;
		finalHygro = finalHygro/numberOfReads;

		//adjust analog Read
		//hygrometer
		int hygrometerMax = 3900;
		int hygrometerMin = 1200;
		//hygrometer low value is wet (high %) high value is dry (low %)
		
		//cut out extrem values
		int finalHygroAdjusted = finalHygro;
		if(finalHygroAdjusted>hygrometerMax){
			finalHygroAdjusted = hygrometerMax;
		}
		if(finalHygroAdjusted<hygrometerMin){
			finalHygroAdjusted = hygrometerMin;
		}
		int hygroDiffrence = hygrometerMax-hygrometerMin;
		finalHygroAdjusted = finalHygro-hygrometerMin; //set base line
		finalHygroAdjusted = (finalHygroAdjusted - (hygrometerMax-hygrometerMin)) * (-1); //invers numbers
		double finalHygroAdjustedPercent = (double)finalHygroAdjusted/(double)hygroDiffrence; //make it in %
		//light
		int lightMax = 4600;
		int lightMin = 0;
		int finalLightAdjusted = finalLight;
		if(finalLightAdjusted>lightMax){
			finalLightAdjusted = lightMax;
		}
		if(finalLightAdjusted<lightMin){
			finalLightAdjusted = lightMin;
		}
		int lightDiffrence = lightMax-lightMin;
		
		
		double finalLightAdjustedPercent = (double)finalLightAdjusted/(double)lightDiffrence; //make it in %
		//make Display Output
        display.clear();

        writeText("(secStart:"+String((int)((millis() - startTime) / 1000L)) + ")" 
            + " (temp:" + String(finalTemperature) + "°C)" 
			+ " (humi:" + String(finalHumidity) + "%)" 
			+ " (ligh:"  + String(finalLightAdjustedPercent) + "%) " 
			+ " (hygr:" + String(finalHygroAdjustedPercent) + "%) " 
			+ " (reco:" + String(received) + ")");
        display.display();

		//Serial Output
        Serial.println("<timeSinceStart>" + String((int)((millis() - startTime) / 1000L)) + "</timeSinceStart>"); //optional
        Serial.println("<receivedThing>" + String(received) + " </receivedThing>");//optional
        Serial.println("<values>;" + String(finalTemperature) + ";" + String(finalHumidity) + ";" + String(finalLightAdjustedPercent) + ";" + String(finalHygroAdjustedPercent) + ";" + "</values>");
		delay(targetGeneralIntervalTIme);
    }
    if (Serial.available())
    {
        received = Serial.readStringUntil('\n');
    }
    if (received.equals("exit"))
    {
        exit(0);
    }
	//wait N time before the next read
	
}
