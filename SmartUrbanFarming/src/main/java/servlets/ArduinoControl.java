package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import arduino.ArduinoConnectionController;

/**
 * Servlet implementation class ArduinoControl
 */

@MultipartConfig
public class ArduinoControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArduinoControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("log", ArduinoConnectionController.getInstance().getWebLog()); // Will be available as ${products} in JSP
        request.getRequestDispatcher("ArduinoControl.jsp").forward(request, response);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String content = request.getParameter("content");
		ArduinoConnectionController.getInstance().writeWithIPAdress(content);
		System.out.println(content);
		request.setAttribute("log", ArduinoConnectionController.getInstance().getWebLog()); // Will be available as ${products} in JSP
        request.getRequestDispatcher("ArduinoControl.jsp").forward(request, response);		

	}

}
