package model_parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_connection {
	Connection conn;

	public static void dbAccess(String arg) throws SQLException {
		DB_connection app = new DB_connection();

		app.connectionToDerby();
		app.dbExecute(arg);
		}
	
	public void connectionToDerby() throws SQLException {
		String dbUrl = "jdbc:derby:farming;create=true";
		conn = DriverManager.getConnection(dbUrl);
		}

	
	public void	dbExecute(String statement) throws SQLException {
		Statement stmt = conn.createStatement();		
		stmt.executeUpdate(statement);	
}
	
	public void	dbRead(String statement) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(statement);
		
		while (rs.next()) { 
			System.out.printf("%d\t%s\n", rs.getInt("id"), rs.getString("name"));;
		}	
}
}