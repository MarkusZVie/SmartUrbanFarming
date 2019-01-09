package model_parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DB_connection {
	static Connection conn;
		
	public static void dbExecute(String statement) throws SQLException {
		String dbUrl = "jdbc:derby:farming;create=false";
		conn = DriverManager.getConnection(dbUrl);
		Statement stmt = conn.createStatement();		
		stmt.executeUpdate(statement);
		stmt.close();
		System.out.println("Insert statement sucessfull!");
	}
	
	public static void dbSensors(String farm_name, String humidity, String temp, String light, String hygro) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {	
			if(farm_name== null | farm_name.isEmpty()){
				farm_name = printDb("select * from farming_module");
			}
			String input = "insert into sensordata values('" + farm_name + "','" + timestamp + "','" + humidity +"','" + temp + "','" + light + "','" + hygro + "')";
			dbExecute(input);
			System.out.println("Entered sensordata successfully!");
			} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String[]> getSensorData(){
		ArrayList<String[]> returnList = new ArrayList<String[]>();
		
		try {
			String dbUrl = "jdbc:derby:farming;create=false";
			conn = DriverManager.getConnection(dbUrl);
			Statement stmt = conn.createStatement();		
		
			ResultSet rs = stmt.executeQuery("SELECT * FROM sensordata");
			int columnsNumber = rs.getMetaData().getColumnCount();
			
			
			//create header
			String[] header = new String[columnsNumber];
			for (int i = 0; i < columnsNumber; i++) {
				header[i] = rs.getMetaData().getColumnName(i+1);
			}
			returnList.add(header);
			
			//create data
			while (rs.next()) {
				String[] valueI = new String[columnsNumber];
				for (int i = 0; i < columnsNumber; i++) {
					valueI[i] = rs.getString(i+1);
				}
				returnList.add(valueI); 
			}
			rs.close();
			stmt.close();	
			}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return returnList;
		
	}
	
	public static void exportToCSV (String statement, String path) throws SQLException, FileNotFoundException{
		//source https://stackoverflow.com/questions/30073980/java-writing-strings-to-a-csv-file
		String dbUrl = "jdbc:derby:farming;create=false";
		conn = DriverManager.getConnection(dbUrl);
		Statement stmt = conn.createStatement();
		char seperator =';';
		boolean isGermanCSV = true;
		StringBuilder sb = new StringBuilder();
		
		try {
			ResultSet rs = stmt.executeQuery(statement);
			int columnsNumber = rs.getMetaData().getColumnCount();
			
			
			//create header
			for (int i = 1; i <= columnsNumber; i++) {
				sb.append(rs.getMetaData().getColumnName(i) + seperator);
			}
			sb.append('\n');
			
			
			while (rs.next()) {
			       for (int i = 1; i <= columnsNumber; i++) {
			           if (i > 1) System.out.print(",  ");
			           String columnValue = rs.getString(i);
			           if(isGermanCSV) {
			        	   columnValue=columnValue.replace('.', ',');
			           }
			           sb.append(columnValue + seperator);
			       }
			       sb.append('\n');
			   }
			
			rs.close();
			stmt.close();	
			}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(path.equals("")) {
			path = "csvExport.csv";
		}
		
		PrintWriter pw = new PrintWriter(new File(path));
		pw.write(sb.toString());
        pw.close();
		
	}
	
	public static ArrayList<ArrayList<String>> readDB(String statement) throws SQLException {
		ArrayList<ArrayList<String>> mainList = new ArrayList<ArrayList<String>>();
		String dbUrl = "jdbc:derby:farming;create=false";
		conn = DriverManager.getConnection(dbUrl);
		Statement stmt = conn.createStatement();
		try {
			ResultSet rs = stmt.executeQuery(statement);
			int columnsNumber = rs.getMetaData().getColumnCount();

			ArrayList<String> header= new ArrayList<String>();
			//create header
			for (int i = 1; i <= columnsNumber; i++) {
				header.add(rs.getMetaData().getColumnName(i));
			}
			
			
			while (rs.next()) {
				ArrayList<String> row = new ArrayList<String>();
			       for (int i = 1; i <= columnsNumber; i++) {
			    	   row.add(rs.getString(i));
			       }
			       mainList.add(row);
			   }
			
			rs.close();
			stmt.close();	
			}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return mainList;
	}
	
	
	public static String printDb(String statement) throws SQLException {
		String name = "";
		String dbUrl = "jdbc:derby:farming;create=false";
		conn = DriverManager.getConnection(dbUrl);
		Statement stmt = conn.createStatement();
		try {
			ResultSet rs = stmt.executeQuery(statement);
			int columnsNumber = rs.getMetaData().getColumnCount();

			while (rs.next()) {
			       for (int i = 1; i <= columnsNumber; i++) {
			           if (i > 1) System.out.print(",  ");
			           String columnValue = rs.getString(i);
			           System.out.print(rs.getMetaData().getColumnName(i)+": " + columnValue);
			       }
			       System.out.println("");
			   }
			
			rs.close();
			stmt.close();	
			}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public static void deleteSensorData() {
		try {
			String dbUrl = "jdbc:derby:farming;create=false";
			conn = DriverManager.getConnection(dbUrl);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DELETE FROM sensordata");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
}