package model_parser;

import java.io.IOException;
import java.sql.SQLException;

public class test_db{
	
	public static void main(String[] args) throws IOException, SQLException{
		String name = "";
		System.out.println(name);
		DB_connection.dbSensors(name, 4748+"", 49494+"", 4949+"", 4944+"");
		DB_connection.printDb("SELECT * FROM crop");
	
	}
}