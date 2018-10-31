
/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 by ThingPulse, Daniel Eichhorn
 * Copyright (c) 2018 by Fabrice Weinberg
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * ThingPulse invests considerable time and money to develop these open source libraries.
 * Please support us by buying our products (and not the clones) from
 * https://thingpulse.com
 *
 */

// Include the correct display library
// For a connection via I2C using Wire include
#include <Wire.h> // Only needed for Arduino 1.6.5 and earlier
#include "SSD1306.h" // legacy include: `#include "SSD1306Wire.h"`
// or #include "SH1106Wire.h", legacy include: `#include "SH1106.h"`
// For a connection via I2C using brzo_i2c (must be installed) include
// #include <brzo_i2c.h> // Only needed for Arduino 1.6.5 and earlier
// #include "SSD1306Brzo.h"
// #include "SH1106Brzo.h"
// For a connection via SPI include
// #include <SPI.h> // Only needed for Arduino 1.6.5 and earlier
// #include "SSD1306Spi.h"
// #include "SH1106SPi.h"

// Include custom images
#include "images.h"

// Initialize the OLED display using SPI
// D5 -> CLK
// D7 -> MOSI (DOUT)
// D0 -> RES
// D2 -> DC
// D8 -> CS
// SSD1306Spi        display(D0, D2, D8);
// or
// SH1106Spi         display(D0, D2);

// Initialize the OLED display using brzo_i2c
// D3 -> SDA
// D5 -> SCL
// SSD1306Brzo display(0x3c, D3, D5);
// or
// SH1106Brzo  display(0x3c, D3, D5);

// Initialize the OLED display using Wire library
// SSD1306Wire  display(0x3c, D3, D5);
SSD1306 display(0x3c, 5, 4);

// SH1106 display(0x3c, D3, D5);

#define DEMO_DURATION 3000
typedef void (*Demo)(void);

int demoMode = 0;
int counter = 1;
int incomingByte = 0; 

void setup()
{
    Serial.begin(9600);

    // Initialising the UI will init the display too.
    display.init();
    display.flipScreenVertically();
    display.setFont(ArialMT_Plain_10);
}

void drawFontFaceDemo()
{
    // Font Demo1
    // create more fonts at http://oleddisplay.squix.ch/
    display.setTextAlignment(TEXT_ALIGN_LEFT);
    display.setFont(ArialMT_Plain_10);
    display.drawString(0, 0, "Hello world");
    display.setFont(ArialMT_Plain_16);
    display.drawString(0, 10, "Hello OMiLAB");
    display.setFont(ArialMT_Plain_24);
    display.drawString(0, 26, "Hello Vimal");
}

void drawTextFlowDemo()
{
    display.setFont(ArialMT_Plain_10);
    display.setTextAlignment(TEXT_ALIGN_LEFT);
    display.drawStringMaxWidth(0, 0, 128,
                               "Lorem ipsum\n dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore.");
}

void drawTextAlignmentDemo()
{
    // Text alignment demo
    display.setFont(ArialMT_Plain_10);

    // The coordinates define the left starting point of the text
    display.setTextAlignment(TEXT_ALIGN_LEFT);
    display.drawString(0, 10, "Left aligned (0,10)");

    // The coordinates define the center of the text
    display.setTextAlignment(TEXT_ALIGN_CENTER);
    display.drawString(64, 22, "Center aligned (64,22)");

    // The coordinates define the right end of the text
    display.setTextAlignment(TEXT_ALIGN_RIGHT);
    display.drawString(128, 33, "Right aligned (128,33)");
}

void drawRectDemo()
{
    // Draw a pixel at given position
    for (int i = 0; i < 10; i++) {
        display.setPixel(i, i);
        display.setPixel(10 - i, i);
    }
    display.drawRect(12, 12, 20, 20);

    // Fill the rectangle
    display.fillRect(14, 14, 17, 17);

    // Draw a line horizontally
    display.drawHorizontalLine(0, 40, 20);

    // Draw a line horizontally
    display.drawVerticalLine(40, 0, 20);
}

void drawCircleDemo()
{
    for (int i = 1; i < 8; i++) {
        display.setColor(WHITE);
        display.drawCircle(32, 32, i * 3);
        if (i % 2 == 0) {
            display.setColor(BLACK);
        }
        display.fillCircle(96, 32, 32 - i * 3);
    }
}

void drawProgressBarDemo()
{
    int progress = (counter / 5) % 100;
    // draw the progress bar
    display.drawProgressBar(0, 32, 120, 10, progress);

    // draw the percentage as String
    display.setTextAlignment(TEXT_ALIGN_CENTER);
    display.drawString(64, 15, String(progress) + "%");
}

void drawImageDemo()
{
    // see http://blog.squix.org/2015/05/esp8266-nodemcu-how-to-create-xbm.html
    // on how to create xbm files
    display.drawXbm(34, 14, WiFi_Logo_width, WiFi_Logo_height, WiFi_Logo_bits);
}

void writeText(String s)
{
    display.setFont(ArialMT_Plain_10);
    display.setTextAlignment(TEXT_ALIGN_LEFT);
    display.drawStringMaxWidth(0, 0, 128, s);
}

Demo demos[] = { drawFontFaceDemo, drawTextFlowDemo, drawTextAlignmentDemo, drawRectDemo, drawCircleDemo, drawProgressBarDemo, drawImageDemo };
int demoLength = (sizeof(demos) / sizeof(Demo));
long timeSinceLastModeSwitch = 0;

int counterLoops = 0;
long startTime = millis();  


bool receivedNothing = true; 

void loop()
{
	// clear the display
    display.clear();
	
	String displayString= "";
   
    if(receivedNothing){
       displayString += "+ Nothing received ";   
    }else{
		displayString += "+ Something received "; 
	}

	displayString += "+ L:" + String(counterLoops);
	displayString += "+ S: " + String((int)((millis()-startTime) / 1000L));
	
	Serial.println( "loops scince Start: " + String(counterLoops) + " Secunds since Start: " + String((int)((millis()-startTime) / 1000L)));
	
	if (Serial.available() > 0) {
		if (Serial.available() > 0) {
			displayString += "+ Serial avaiable";
		}
		if (Serial.available() > 0) {
			
		}
		if (Serial.available() > 0) {
			// read the incoming byte:
			String readedString = Serial.readString();

			// say what you got:
			Serial.println("I received: " + String(readedString));
			displayString +=  "+ I received: " + String(readedString);
			receivedNothing = false;
			
		}
		
	}else{
	  displayString += "+ Read Serial";
	  }
	
    
	writeText(displayString);

    
    // write the buffer to the display
    display.display();
	counterLoops++;
    delay(2000);
}