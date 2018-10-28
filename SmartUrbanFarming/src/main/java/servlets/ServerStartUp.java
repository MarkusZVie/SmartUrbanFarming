package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import arduino.ArduinoConnectionController;
import ruleManagement.RuleManager;

/**
 * Servlet implementation class ServerStartUp
 */
public class ServerStartUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServerStartUp() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException
    {
          System.out.println("DEVELOP MODE In this Class: "+this.getClass() +" Her can we call function Threads by Start Server");
          RuleManager rm = new RuleManager();
          rm.fireRules();
          
    }
    
	

}
