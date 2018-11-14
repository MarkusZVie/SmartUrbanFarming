package model_parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_connection {
	Connection conn;

	public static void main(String[] args) throws SQLException {
		DB_connection app = new DB_connection();

		app.connectionToDerby();
		app.dbExecute();
		}
	
	public void connectionToDerby() throws SQLException {
		String dbUrl = "jdbc:derby:farming;create=true";
		conn = DriverManager.getConnection(dbUrl);
		}


	
	public void	dbExecute() throws SQLException {
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("Create table users (id int primary key, name varchar(30))");


		stmt.executeUpdate("insert into users values (1,'test')");
		stmt.executeUpdate("insert into users values (2,'test2')");

		ResultSet rs = stmt.executeQuery("SELECT * FROM users");
		
		while (rs.next()) { 
			System.out.printf("%d\t%s\n", rs.getInt("id"), rs.getString("name"));;
		}	
}
}