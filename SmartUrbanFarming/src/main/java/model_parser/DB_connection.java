package model_parser;

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
	
	public static String dbRead(String statement) throws SQLException {
		String name = "";
		String dbUrl = "jdbc:derby:farming;create=false";
		conn = DriverManager.getConnection(dbUrl);
		Statement stmt = conn.createStatement();
		try {
			ResultSet rs = stmt.executeQuery(statement);
		
			while (rs.next()) 
			{
				name = rs.getString(1);
				System.out.println(name);

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