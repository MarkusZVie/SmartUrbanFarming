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
				farm_name = dbRead("select * from farming_module");
			}
			String input = "insert into sensordata values('" + farm_name + "','" + timestamp + "','" + humidity +"','" + temp + "','" + light + "','" + hygro + "')";
			dbExecute(input);
			System.out.println("Entered sensordata successfully!");
			} 
		catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	public static String dbRead(String statement) throws SQLException {
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
}