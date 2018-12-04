package model_parser;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class dbToCsv {
	static Statement stmt=null;
	static ResultSet rs = null;
	static String record = null;

    public static void readData() throws IOException, SQLException{
    		FileWriter f1 = new FileWriter("hallo.csv");

        try{
    			String dbUrl = "jdbc:derby:farming;create=false";
    			Connection conn = DriverManager.getConnection(dbUrl);
            String query="SELECT * FROM sensordata";
             stmt=conn.createStatement();
             rs=stmt.executeQuery(query);
             ResultSetMetaData metaData = rs.getMetaData();
             int columns = metaData.getColumnCount();
             
             
             while (rs.next()) {
                 String s = rs.getString("farm_name");
                 Timestamp n = rs.getTimestamp("time_");
                 String h = rs.getString("humidity");
                 String t = rs.getString("temp");
                 String l = rs.getString("light");
                 String hy = rs.getString("hygro");
                 String name = rs.getString(3);
                 System.out.println(name);
                 System.out.println(s + "   " + n + "   " + h + "   " + t + "   " + l + "   " + hy);
                 for (int i = 1; i <= columns; i++) 
                 {
                  record = rs.getString(i);
                  f1.append(rs.getString(i));
                  f1.append(',');
                 }
                 f1.append('\n');
             	}
             } 
             finally 
             {
                 f1.flush();
                 rs.close();
                 stmt.close();
                 f1.close();
             }
    }
}    

    
  