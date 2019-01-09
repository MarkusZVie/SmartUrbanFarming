package ruleManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.shredzone.commons.suncalc.SunTimes;

import model_parser.DB_connection;

public class PersistenceFactReasoningThread extends Thread{

	private boolean runs;
	private int timeInterval;
	private RuleManager rm;
	private SimpleDateFormat sdf;
	
	public synchronized void setRuns(boolean runs) {
		this.runs = runs;
	}


	public PersistenceFactReasoningThread() {
		runs = true;
		timeInterval = 10000; //Time Interval 10 sec
		rm = RuleManager.getInstance();
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}


	public void run() {
		System.out.println("PersistenceFactReasoningThread Started");
		while(runs) {
			calcShortTermLightFact();
			calcMiddleTermLightFact();
			calcLongTermLightFact();
			try {
				this.sleep(timeInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}


	private void calcLongTermLightFact() {
		//get Data
		ArrayList<ArrayList<String>> lightSensorData = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> locationModule = new ArrayList<ArrayList<String>>();
		
		try {
						
			
			locationModule = DB_connection.readDB(""
					+ "SELECT CITY, LONG, LAT FROM location INNER JOIN farming_module ON farming_module.LOC_NAME = location.LOC_NAME "
					+ "WHERE FARM_NAME LIKE 'Farming Module-29409'");
			
			lightSensorData = DB_connection.readDB(""
					+ "SELECT LIGHT, TIME_ "
					+ "FROM sensordata "
					+ "WHERE TIME_>= '" + sdf.format(new Date(System.currentTimeMillis()/1000-24*60*60*31)) + "'"
					+ "AND FARM_NAME LIKE 'Modul1'");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		ArrayList<String> lightFacts = calcLightFacts(lightSensorData,locationModule,31);
		for(int i=0; i<lightFacts.size();) {
			String factName = "LongTerm"+lightFacts.get(i++);
			String factvalue = lightFacts.get(i++);
			rm.addFactToFactase(factName, factvalue);
		}
		
	}


	private void calcMiddleTermLightFact() {
		//get Data
		ArrayList<ArrayList<String>> lightSensorData = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> locationModule = new ArrayList<ArrayList<String>>();
		
		try {
						
			
			locationModule = DB_connection.readDB(""
					+ "SELECT CITY, LONG, LAT FROM location INNER JOIN farming_module ON farming_module.LOC_NAME = location.LOC_NAME "
					+ "WHERE FARM_NAME LIKE 'Farming Module-29409'");
			
			lightSensorData = DB_connection.readDB(""
					+ "SELECT LIGHT, TIME_ "
					+ "FROM sensordata "
					+ "WHERE TIME_>= '" + sdf.format(new Date(System.currentTimeMillis()-24*60*60*1000*7)) + "'"
					+ "AND FARM_NAME LIKE 'Modul1'");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> lightFacts = calcLightFacts(lightSensorData,locationModule,7);
		for(int i=0; i<lightFacts.size();) {
			String factName = "MiddletTerm"+lightFacts.get(i++);
			String factvalue = lightFacts.get(i++);
			rm.addFactToFactase(factName, factvalue);
		}
		
		
	}


	private void calcShortTermLightFact() {
		
		//get Data
		ArrayList<ArrayList<String>> lightSensorData = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> locationModule = new ArrayList<ArrayList<String>>();
		
		try {
						
			
			locationModule = DB_connection.readDB(""
					+ "SELECT CITY, LONG, LAT FROM location INNER JOIN farming_module ON farming_module.LOC_NAME = location.LOC_NAME "
					+ "WHERE FARM_NAME LIKE 'Farming Module-29409'");
			
			lightSensorData = DB_connection.readDB(""
					+ "SELECT LIGHT, TIME_ "
					+ "FROM sensordata "
					+ "WHERE TIME_>= '" + sdf.format(new Date(System.currentTimeMillis()-24*60*60*1000)) + "'"
					+ "AND FARM_NAME LIKE 'Modul1'");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ArrayList<String> lightFacts = calcLightFacts(lightSensorData,locationModule,1);
		for(int i=0; i<lightFacts.size();) {
			String factName = "ShortTerm"+lightFacts.get(i++);
			String factvalue = lightFacts.get(i++);
			rm.addFactToFactase(factName, factvalue);
		}
		
		
		
		
		
		
	}
	
	private ArrayList<String> calcLightFacts(ArrayList<ArrayList<String>> lightSensorData, ArrayList<ArrayList<String>> locationModule, int numOfDays) {
		//calc Day light time
				Date date = new Date(System.currentTimeMillis()-24*60*60*1000);
				Date date2 = new Date(System.currentTimeMillis()-(24*60*60*1000)*2);
				double lat = Double.parseDouble(locationModule.get(0).get(2)); 
				double lng = Double.parseDouble(locationModule.get(0).get(1)); 
				SunTimes times = SunTimes.compute().on(date).at(lat, lng).execute();	
				long sunrise = times.getRise().getTime();
				long sunset = times.getSet().getTime();
				if(sunset<sunrise) {
					sunrise =  SunTimes.compute().on(date2).at(lat, lng).execute().getRise().getTime();
				}
				long dayTimeMS = sunset-sunrise; //lightTime in MS
				
				
				//reason facts out of data
				double simplyAVG = 0.0;	
				double differenceAgainstExpectetLight = 0.0;
				long lastMesurement = -1;
				ArrayList<Long> mesureMoments = new ArrayList<Long>();
				for(ArrayList<String> row: lightSensorData) {
					
					
					
					simplyAVG += Double.parseDouble(row.get(0));
					long timeSinceSunrise =0;
					try {
						//calc mesurement frequenzy
						if(lastMesurement != -1) {
							mesureMoments.add(sdf.parse(row.get(1)).getTime()-lastMesurement);
						}
						lastMesurement=sdf.parse(row.get(1)).getTime();
						
						SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
						Date exactMesureMoment = sdf.parse(row.get(1));
						Date dayOfMesurement = sdf2.parse(sdf2.format(exactMesureMoment));
						long timeSunrise = SunTimes.compute().on(dayOfMesurement).at(lat, lng).execute().getRise().getTime();
						//System.out.println("dayOfMesurements: " + sdf.format(dayOfMesurement));
						//System.out.println("exactMesureMoment: " + sdf.format(exactMesureMoment));
						//System.out.println("Sunrise: " + sdf.format(SunTimes.compute().on(dayOfMesurement).at(lat, lng).execute().getRise()));
						//System.out.println("SunSet: " + sdf.format(SunTimes.compute().on(dayOfMesurement).at(lat, lng).execute().getSet()));
						timeSinceSunrise = exactMesureMoment.getTime()-timeSunrise;
						differenceAgainstExpectetLight += Double.parseDouble(row.get(0)) - calcExpectedLightLevel(dayTimeMS, timeSinceSunrise);
						;
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				simplyAVG /= (double) lightSensorData.size();
				differenceAgainstExpectetLight /= (double) lightSensorData.size();
				
				ArrayList<String> lightFacts = new ArrayList<String>();
				
				//calc mesure frequency
				if(mesureMoments.size()>=1) {
					Collections.sort(mesureMoments);
					int medianID = (int)(mesureMoments.size()/2);
					long medianTimespan = mesureMoments.get(medianID);
					int expectetNumberOfMesurements = (int) ((numOfDays*24*60*60*1000)/ medianTimespan);
					double percentOfMosurementsDone = (double) lightSensorData.size()/(double) expectetNumberOfMesurements;
					
					lightFacts.add("percentOfMosurementsDone");
					lightFacts.add(percentOfMosurementsDone+"");
				}
				
				lightFacts.add("simplyAVG");
				lightFacts.add(simplyAVG+"");
				
				lightFacts.add("differenceAgainstExpectetLight");
				lightFacts.add(differenceAgainstExpectetLight+"");
				
				//printLightExpectetFunktion(dayTimeMS);
				
				return lightFacts;
				
				
		
	}


	private void printLightExpectetFunktion(long dayTimeMS) {
		//funktion for set up sigmoid light level. will not called, just in case is saved
		System.out.println(dayTimeMS);
		StringBuilder sb = new StringBuilder();
		for(long i = 0; i<dayTimeMS; i=i+(1000*60*20)) {
			sb.append((calcExpectedLightLevel(dayTimeMS,i)+"").replace('.', ','));
			sb.append(';');
			sb.append(((double)i/(1000*60*60)+"").replace('.', ','));
			sb.append(';');
			sb.append('\n');
			
		}
		
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File("lightFunktionTest.csv"));
			pw.write(sb.toString());
	        pw.close();
			System.out.println("sdsd:" + sb.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private double calcExpectedLightLevel(long dayTime, long momentTime) {
		double expectetValue = 0.0;
		if(momentTime > dayTime || momentTime < 0) {
			return 0.0; 	//nacht expected 0 light
		}
		long midday = dayTime/2;
		if(momentTime<=midday) {
			double momentTimeHour = (double)momentTime/(1000*60*60);
			expectetValue = sigmoid((momentTimeHour*5)-4);
			return expectetValue;
			
		}
		if(momentTime>midday) {
			momentTime = momentTime-((momentTime-midday)*2);
			double momentTimeHour = (double)momentTime/(1000*60*60);
			expectetValue = sigmoid(((momentTimeHour*5)-4));
			return expectetValue;
		}
		return expectetValue;
		
	}
	
	private double sigmoid(double x)
	{
	    return 1 / (1 + Math.exp(-x));
	}
	
}
