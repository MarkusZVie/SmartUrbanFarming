package xmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
			FileInputStream file = new FileInputStream(new File("/Users/Beate/Desktop/greenhouse.xml"));
				
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();
			
			Document xmlDocument = builder.parse(file);

			XPath xPath =  XPathFactory.newInstance().newXPath();
			
			ArrayList<String> crops = new ArrayList<String>();
			
			XPathExpression expr= xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@class=\"Crop\"]/@name");
			NodeList nl = (NodeList) expr.evaluate(xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
			    Node node = nl.item(i);
			    crops.add(node.getTextContent());		
			}

			for(int i=0; i<crops.size(); i++) {
			String name = crops.get(i);
			System.out.println(name);
			
			XPathExpression expr1= xPath.compile("/ADOXML/MODELS/MODEL/CONNECTOR[@class=\"Requires\"]/FROM[@instance=\""+name+"\"]/../TO/@instance");
			NodeList nl1 = (NodeList) expr1.evaluate(xmlDocument, XPathConstants.NODESET);
				for (int j = 0; j < nl1.getLength(); j++) {
					Node node = nl1.item(j);
					String needs = node.getTextContent().split("-")[0];
					System.out.println(needs);
						
					XPathExpression expr2 =  xPath.compile("/ADOXML/MODELS/MODEL/INSTANCE[@name=\""+node.getTextContent()+"\"]/ATTRIBUTE[@name=\"Amount\"]");
					String evalu = (String) expr2.evaluate(xmlDocument, XPathConstants.STRING);
					System.out.println(evalu);
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