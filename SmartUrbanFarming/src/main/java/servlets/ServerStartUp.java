package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import arduino.ArduinoConnectionController;
import mailing.NotificationMailer;
import model_parser.DB_connection;
import ruleManagement.PersistenceFactReasoningThread;
import ruleManagement.PersistenceRBSThread;
import ruleManagement.RuleManager;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.servlet.http.*;
import javax.servlet.*;

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
          PersistenceRBSThread t = new PersistenceRBSThread();
          t.start();
          PersistenceFactReasoningThread t2 = new PersistenceFactReasoningThread();
          t2.start();
          
          
          //Arduino give him IP
          ArduinoConnectionController.getInstance().writeWithIPAdress(" Server has started");
          
    }
    
	

}
