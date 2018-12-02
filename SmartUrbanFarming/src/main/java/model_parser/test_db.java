package model_parser;

public class test_db{
	
	public static void main(String[] args){
		String name = "";
		System.out.println(name);
		DB_connection.dbSensors(name, 4748+"", 49494+"", 4949+"", 4944+"");
		dbToCsv.readData();
	
	}
}