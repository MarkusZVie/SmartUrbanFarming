package arduino;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;



public class ArduinoConnectionController implements SerialPortEventListener {
	SerialPort serialPort;
    /** The port we're normally going to use. */
private static final String PORT_NAMES[] = { 
		"/dev/tty.usbserial-A9007UX1", // Mac OS X
                    "/dev/ttyACM0", // Raspberry Pi
		"/dev/ttyUSB0", // Linux
		"COM3", // Windows
		"COM4", // Windows
};
/**
* A BufferedReader which will be fed by a InputStreamReader 
* converting the bytes into characters 
* making the displayed results codepage independent
*/
private BufferedReader input;
/** The output stream to the port */
private OutputStream output;
/** Milliseconds to block while waiting for port open */
private static final int TIME_OUT = 2000;
/** Default bits per second for COM port. */
private static final int DATA_RATE = 9600;

private static ArduinoConnectionController instance;

private ArrayList<String> log;

private ArduinoConnectionController() {
	log=new ArrayList<String>();
	initialize();
}

public synchronized static ArduinoConnectionController getInstance(){
	 if(instance == null){
           instance = new ArduinoConnectionController();
       }
   return instance;
}

public void initialize() {
            // the next line is for Raspberry Pi and 
            // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
            //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
	CommPortIdentifier portId = null;
	Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

	//First, Find an instance of serial port as set in PORT_NAMES.
	while (portEnum.hasMoreElements()) {
		CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
		System.out.println("currPortId= " + currPortId);
		for (String portName : PORT_NAMES) {
			
			if (currPortId.getName().equals(portName)) {
				portId = currPortId;
				break;
			}
		}
	}
	System.out.println(portId);
	if (portId == null) {
		System.out.println("Could not find COM port.");
		return;
	}

	try {
		// open serial port, and use class name for the appName.
		serialPort = (SerialPort) portId.open(this.getClass().getName(),
				TIME_OUT);

		// set port parameters
		serialPort.setSerialPortParams(DATA_RATE,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		// open the streams
		input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		output = serialPort.getOutputStream();

		// add event listeners
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
		
	} catch (Exception e) {
		System.err.println(e.toString());
	}
	System.out.println(portId);
}

/**
 * This should be called when you stop using the port.
 * This will prevent port locking on platforms like Linux.
 */
public synchronized void close() {
	if (serialPort != null) {
		serialPort.removeEventListener();
		serialPort.close();
	}
	
}

/**
 * Handle an event on the serial port. Read the data and print it.
 */
public synchronized void serialEvent(SerialPortEvent oEvent) {
	if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
		try {
			String inputLine=input.readLine();
			System.out.println(inputLine);
			log.add(inputLine);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	// Ignore all the other eventTypes, but you should consider the other ones.
}

public synchronized void write(String s) {
	System.out.println("sdsdsd");
	PrintWriter pOutput = new PrintWriter(output);
	pOutput.print(s);
	pOutput.flush();
	pOutput.close();
}

public synchronized String getWebLog() {
	StringBuilder sb = new StringBuilder();
	for(String s : log) {
		sb.append(s + "<br>");
	}
	return sb.toString();
}




}