package model_parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {
	public static void main(String[] args) {

		try {
			FileInputStream file = new FileInputStream(new File("greenhouse.xml"));
				
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();
			
			Document xmlDocument = builder.parse(file);

			XPath xPath =  XPathFactory.newInstance().newXPath();
			
			ArrayList<String> location = new ArrayList<String>();
			String loc_name="";
			String longitude="";
			String latitude="";
			String direction="";
			String city="";
			String farm_name="";
			String length="";
			String height="";
			String width="";
			String crop_name="";
			String water="";
			String temp="";
			String light="";
			
			//Get all location inside the model
			XPathExpression expr5= xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@class=\"Location\"]/@name");
			NodeList n1 = (NodeList) expr5.evaluate(xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < n1.getLength(); i++) {
			    Node node = n1.item(i);
			    location.add(node.getTextContent());		
			    System.out.println("Location name: " + node.getTextContent());
			}
			
						
			//For all location get the attributes (lat,long,direction and city)
			for(int i=0; i<location.size(); i++) {
				String name = location.get(i);
				loc_name = name; 
				XPathExpression expr6= xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@name='" + name +"']/ATTRIBUTE[@name=\"Latitude\"]");
				NodeList nodel = (NodeList) expr6.evaluate(xmlDocument, XPathConstants.NODESET);
				for (int j = 0; j < nodel.getLength(); j++) {
				    Node node = nodel.item(j);	
				    latitude = node.getTextContent(); 
				    System.out.println("Latitude: " + latitude);
				}
				XPathExpression expr7= xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@name='" + name +"']/ATTRIBUTE[@name=\"Longitude\"]");
				NodeList n2 = (NodeList) expr7.evaluate(xmlDocument, XPathConstants.NODESET);
				for (int j = 0; j < n2.getLength(); j++) {
				    Node node = n2.item(j);
				    longitude = node.getTextContent(); 
				    System.out.println("Longitude: " + longitude);
				}
				XPathExpression expr8= xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@name='" + name +"']/ATTRIBUTE[@name=\"Direction\"]");
				NodeList n3 = (NodeList) expr8.evaluate(xmlDocument, XPathConstants.NODESET);
				for (int j = 0; j < n3.getLength(); j++) {
				    Node node = n3.item(j);	
				    direction = node.getTextContent();
				    System.out.println("Direction: " + direction);
				}
				XPathExpression expr9= xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@name='" + name +"']/ATTRIBUTE[@name=\"City\"]");
				NodeList n4 = (NodeList) expr9.evaluate(xmlDocument, XPathConstants.NODESET);
				for (int j = 0; j < n4.getLength(); j++) {
				    Node node = n4.item(j);	
				    city = node.getTextContent();
				    System.out.println("City: " + city);
				}
				
				String execute = "INSERT INTO location VALUES(\'" + loc_name +"\', \'" +city + "\', \'" + direction + "\', \'"+longitude +"\', \'" +latitude +"\')" ;
				System.out.println(execute);
				try {
					DB_connection.dbAccess(execute);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
		
				//Get all farming modules at this location
				ArrayList<String> module = new ArrayList<String>();
				
				XPathExpression expr10= xPath.compile("/ADOXML/MODELS/MODEL/CONNECTOR[@class=\"In\"]/FROM[@instance=\""+ name +"\"]/../TO/@instance");
				NodeList n12 = (NodeList) expr10.evaluate(xmlDocument, XPathConstants.NODESET);
				for (int k = 0; k < n12.getLength(); k++) {
				    Node node = n12.item(k);
				    module.add(node.getTextContent());		
				    System.out.println("Farming Module name: " + node.getTextContent());
				}
				for(int l=0; l<module.size(); l++) {
					String name1 = module.get(l);
					farm_name= name1;
					XPathExpression expr16= xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@name='" + name1 +"']/ATTRIBUTE[@name=\"Length\"]");
					NodeList n5 = (NodeList) expr16.evaluate(xmlDocument, XPathConstants.NODESET);
					for (int j = 0; j < n5.getLength(); j++) {
					    Node node = n5.item(j);
					    length = node.getTextContent();
					    System.out.println("Lenght: " + length);
					}
					XPathExpression expr17= xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@name='" + name1 +"']/ATTRIBUTE[@name=\"Height\"]");
					NodeList n6 = (NodeList) expr17.evaluate(xmlDocument, XPathConstants.NODESET);
					for (int j = 0; j < n6.getLength(); j++) {
					    Node node = n6.item(j);
					    height = node.getTextContent();
					    System.out.println("Height: " +  height);
					}
					XPathExpression expr18= xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@name='" + name1 +"']/ATTRIBUTE[@name=\"Width\"]");
					NodeList n7 = (NodeList) expr18.evaluate(xmlDocument, XPathConstants.NODESET);
					for (int j = 0; j < n7.getLength(); j++) {
					    Node node = n7.item(j);
					    width = node.getTextContent();
					    System.out.println("Width: " + width);
					}
					String execute1 = "INSERT INTO farming_module VALUES(\'" + farm_name +"\', \'" + length + "\', \'" + height + "\', \'"+ width +"\', \'" + loc_name +"\')" ;
					System.out.println(execute1);
					try {
						DB_connection.dbAccess(execute1);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					//Get all crops in this farming module
					ArrayList<String> crops = new ArrayList<String>();
					XPathExpression expr19= xPath.compile("/ADOXML/MODELS/MODEL/CONNECTOR[@class=\"In\"]/TO[@instance=\""+ name1 +"\"]/../FROM/@instance");
					NodeList n19 = (NodeList) expr19.evaluate(xmlDocument, XPathConstants.NODESET);
					for (int k = 0; k < n19.getLength(); k++) {
					    Node node = n19.item(k);
					    if((!Objects.equals(name,node.getTextContent()))) {
						    crops.add(node.getTextContent());		
						    System.out.println("Crop name: " + node.getTextContent());
					    }
					}
					for(int m=0; m<crops.size(); m++) {
						String name2 = crops.get(m);
						crop_name = name2;
						XPathExpression expr1= xPath.compile("/ADOXML/MODELS/MODEL/CONNECTOR[@class=\"Requires\"]/FROM[@instance=\""+name2+"\"]/../TO/@instance");
						NodeList nl1 = (NodeList) expr1.evaluate(xmlDocument, XPathConstants.NODESET);
							for (int j = 0; j < nl1.getLength(); j++) {
								Node node = nl1.item(j);
								String needs = node.getTextContent().split("-")[0];
								System.out.print(name2 + " needs " + needs);
									
								XPathExpression expr2 =  xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@name=\""+node.getTextContent()+"\"]/ATTRIBUTE[@name=\"Amount\"]");
								String evalu = (String) expr2.evaluate(xmlDocument, XPathConstants.STRING);
								System.out.println(": " + evalu);
								if(needs.equals("Water")) {
									water = evalu;
								}
								if(needs.equals("Temperature")) {
								temp = evalu;
								}
								if(needs.equals("Light")) {
								light = evalu;
								}							
						}
						String execute2 = "INSERT INTO crop VALUES(\'" + crop_name +"\', \'" + water + "\', \'" + temp + "\', \'"+ light +"\', \'" + farm_name +"\')" ;
						System.out.println(execute2);
						try {
							DB_connection.dbAccess(execute2);
						} catch (SQLException e) {
							e.printStackTrace();
						}		
							
					}	
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}		
		

}
}