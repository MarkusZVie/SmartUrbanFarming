package model_parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class dbToCsv {
	static Statement stmt=null;
	static ResultSet rs = null;
	 
    public static void readData(){
        try{
    			String dbUrl = "jdbc:derby:farming;create=false";
    			Connection conn = DriverManager.getConnection(dbUrl);
            String query="SELECT * FROM sensordata";
             stmt=conn.createStatement();
             rs=stmt.executeQuery(query);
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
             }
        }catch(SQLException s){
            s.printStackTrace();
        }
    }
    
   /* ResultSet resultSet = getData();
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    int columnCount = resultSetMetaData.getColumnCount();

    while (resultSet.next()) {
        Object[] values = new Object[columnCount];
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            values[i - 1] = resultSet.getObject(i);
        }
        String []model.addRow(values);
    }*/
	
}
