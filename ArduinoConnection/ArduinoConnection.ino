
// Include the correct display library
// For a connection via I2C using Wire include
#include <Wire.h> // Only needed for Arduino 1.6.5 and earlier
#include "SSD1306.h" // legacy include: `#include "SSD1306Wire.h"`
#include <SimpleDHT.h>
int pinDHT22 = 17;
SimpleDHT22 dht22(pinDHT22);

//light
int ldrPin = 35;
int analog_value =0;

//Hygrometer 
int hygPin = 32;
int hygrometer_value =0;

SSD1306 display(0x3c, 5, 4);

void setup()
{
  //light
   pinMode(ldrPin, INPUT);
  
    Serial.begin(9600);
	  delay(2000);
    // Initialising the UI will init the display too.
    display.init();
    display.flipScreenVertically();
    display.setFont(ArialMT_Plain_10);
}

int readHygrometer(){
  int hygrometer_value = analogRead(hygPin);
  //Serial.print("Hygrometer: ");
  //Serial.println(hygrometer_value);
  return hygrometer_value;
}

int readLight(){
  int analog_value = analogRead(ldrPin);
  //Serial.print("Light: ");
  //Serial.println(analog_value);
  return analog_value;
  }

float readTemperatur(){
	float temperature = 0;
	float humidity = 0;
	int err = SimpleDHTErrSuccess;
	if ((err = dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
		Serial.print("Read DHT22 failed, err="); Serial.println(err);delay(2000);
		return -1;
	}
	//Serial.print("Sample OK: ");
	//Serial.print((float)temperature); Serial.print(" *C, ");
	//Serial.print((float)humidity); Serial.println(" RH%");
	return (float)temperature;
}

float readHumidity(){
	float temperature = 0;
	float humidity = 0;
	int err = SimpleDHTErrSuccess;
	if ((err = dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
		Serial.print("Read DHT22 failed, err="); Serial.println(err);delay(2000);
		return -1;
	}
	//Serial.print("Sample OK: ");
	//Serial.print((float)temperature); Serial.print(" *C, ");
	//Serial.print((float)humidity); Serial.println(" RH%");
	return (float)humidity;
}
  
float* readDHT(){
	float temperature = 0;
	float humidity = 0;
	float arr[2] = {-1,-1};
	int err = SimpleDHTErrSuccess;
	if ((err = dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
		Serial.print("Read DHT22 failed, err="); Serial.println(err);delay(2000);
	
		return arr;
	}
	arr[0]=temperature;
	arr[1]=humidity;
	return arr;
	//Serial.print("Sample OK: ");
	//Serial.print((float)temperature); Serial.print(" *C, ");
	//Serial.print((float)humidity); Serial.println(" RH%");

}

void writeText(String s)
{
    display.setFont(ArialMT_Plain_10);
    display.setTextAlignment(TEXT_ALIGN_LEFT);
    display.drawStringMaxWidth(0, 0, 128, s);
}

long startTime = millis();  
String received = "none";

void loop()
{
	while (Serial.available() == 0){
		delay(1000);
    float temperature = 0;
    float humidity = 0;
    int err = SimpleDHTErrSuccess;
    if ((err = dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
        Serial.print("Read DHT22 failed, err="); Serial.println(err);delay(2000);
        temperature = -1;
        humidity = -1;
    }

    
		int light = readLight();
		int hygro = readHygrometer();
		display.clear();
		
		writeText(String((int)((millis()-startTime) / 1000L)) + " >>> " + String(received) + " " + 
		"t:" + String(temperature) + "°C; " +
		"hu:" + String(humidity) + "RH%; " +
		"l:" + String(light) + "; " +
		"hy:" + String(hygro) + ";");
		display.display();
		
		Serial.println("<timeSinceStart>" + String((int)((millis()-startTime) / 1000L)) + "</timeSinceStart>");
		Serial.println("<receivedThing>" + String(received) + " </receivedThing>");
		Serial.println("<temperature>" + String(temperature) + " </temperature>" );
		Serial.println("<humidity>" + String(humidity) + " </humidity>" );
		Serial.println("<light>" + String(light) + " </light>" );
		Serial.println("<hygro>" + String(hygro) + " </hygro>" );
		
	}
    if(Serial.available()){
        received = Serial.readStringUntil('\n');
    }
    if(received.equals("exit")){
      exit(0);
    }
}
