package arduino;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.SQLException;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener;
import model_parser.DB_connection;
import ruleManagement.RuleManager;

import java.util.ArrayList;
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
private static final int TIME_OUT = 4000;
/** Default bits per second for COM port. */
private static final int DATA_RATE = 9600;

private static ArduinoConnectionController instance;

private ArrayList<String> log;

private int countSerialEvents;

private ArduinoConnectionController() {
	log=new ArrayList<String>();
	countSerialEvents =0;
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
	@SuppressWarnings("rawtypes")
	Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

	//First, Find an instance of serial port as set in PORT_NAMES.
	while (portEnum.hasMoreElements()) {
		CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
		for (String portName : PORT_NAMES) {
			if (currPortId.getName().equals(portName)) {
				portId = currPortId;
				break;
			}
		}
	}
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
			if(inputLine.startsWith("<values>") && inputLine.endsWith("</values>") ) {
				parseInputline(inputLine);
				
			}
			log.add(inputLine);
			System.out.println(inputLine);
			
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	countSerialEvents ++;
	if(countSerialEvents >= 100) {
		try {
			DB_connection.exportToCSV("SELECT * FROM sensordata", "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		countSerialEvents =0;
	}
	// Ignore all the other eventTypes, but you should consider the other ones.
}

private void parseInputline(String inputLine) {
	String[] values = inputLine.split(";");
	
	float temperature = Float.parseFloat(values[1]);
	float humidity = Float.parseFloat(values[2])/100;
	float light = Float.parseFloat(values[3])/100;
	float hygro =  Float.parseFloat(values[4])/100;
	
	DB_connection.dbSensors("Modul1", humidity+"", temperature+"", light+"", hygro+"");
	RuleManager.getInstance().addFactToFactase("humidity", humidity);
	RuleManager.getInstance().addFactToFactase("temperature", humidity);
	RuleManager.getInstance().addFactToFactase("light", light);
	RuleManager.getInstance().addFactToFactase("hygro", hygro);
	
}

public synchronized void write(String s) {
	PrintWriter pOutput = new PrintWriter(output);
	pOutput.print(s);
	pOutput.flush();
	pOutput.close();
}

public synchronized void writeWithIPAdress(String s) {
	String ipAdress = "IP:";
	try {
		@SuppressWarnings("rawtypes")
		Enumeration e = NetworkInterface.getNetworkInterfaces();
		while(e.hasMoreElements())
		{
		    NetworkInterface n = (NetworkInterface) e.nextElement();
		    @SuppressWarnings("rawtypes")
			Enumeration ee = n.getInetAddresses();
		    while (ee.hasMoreElements())
		    {
		        InetAddress i = (InetAddress) ee.nextElement();
		        if(i instanceof Inet6Address||i.getHostAddress().equals("127.0.0.1")) {
		        	//those are not allowed
		        }else {
		        	if(ipAdress.equals("IP:")) {
			        	 ipAdress += i.getHostAddress();
			        }else {
			        	 ipAdress += " and " + i.getHostAddress();
			        }
		        }
		        
		    }
		}
	} catch (SocketException e) {
		ipAdress = "No Ip found";
	}
	PrintWriter pOutput = new PrintWriter(output);
	pOutput.print(ipAdress + s);
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

public synchronized ArrayList<String> getLog(){
	return log;
}




}