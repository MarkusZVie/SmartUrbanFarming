# SmartUrbanFarming
This project creates a prototype to take care of plants in an urban, modular setting

Eclipse Version used :
https://www.eclipse.org/downloads/packages/release/2018-09/r/eclipse-ide-java-ee-developers

To extend This Program you go in your Eclipse EE.

•	Clone This Git Repository to any folder on your Computer
•	Start your Eclipse EE
•	Go To File/Import….
•	Choose Existing Maven Projects
•	Click Next>
•	Choose cloned Git folder
•	Click Finish
•	Right-Click on the Project
•	Select RunAs/Maven build
•	Write by Goles “clean install”
•	Press Run
•	Should be a Successful Build
•	Select once more RunAs/maven build
•	Write by goles “tomcat7:run -X”
•	Give that a Name
•	Press Apply
•	Press Run
•	The Server is Starting

To run this Program you require: 

•	Arduino with sensors
•	Raspberry Pi

Start setup:
•	Connect Arduino with the Raspberry Pi with a MicroUSB-cable
•	Supply power to the Raspberry Pi
•	Start Raspberry Pi
•	Start ArduinoAPI and Serial Monitor (Tools>SerialMonitor)
•	Wait up to 10 Seconds to check if you get an serial input signal
•	Close the ArduinoAPI & Serial Monitor
•	Open Shell
•	Browse to ~/git/SmartUrbanFarming (if necessary make git pull)
•	Browse to ~/git/SmartUrbanFarming/SmartUrbanFarming
•	If you pull before run sudo ../mavenInstall.sh
•	To Start run sudo ../startServer.sh 
•	alternative its possible to run sudo mvn tomcat7:run -X
