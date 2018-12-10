package servlets;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import arduino.ArduinoConnectionController;
import mailing.NotificationMailer;

/**
 * Servlet implementation class NotificationSettings
 */

@MultipartConfig
public class NotificationSettings extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotificationSettings() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("NotificationSettings.jsp").forward(request, response);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String toDeleteMail[] = request.getParameterValues("toDeleteMail");
		if(toDeleteMail != null) {
			for(String s: toDeleteMail) {
				NotificationMailer.getInstance().deleteNotificationMailList(s);
			}
			
		}
		
		String newMail = request.getParameter("newMail");
		
		//source: https://stackoverflow.com/questions/18463848/how-to-tell-if-a-random-string-is-an-email-address-or-something-else
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(newMail);

        if(mat.matches()){
        	NotificationMailer.getInstance().addNotificationMailList(newMail);
        }else{
        	response.getWriter().append("Mailaddress invalid").append(request.getContextPath());
        }
		
		doGet(request, response);
	}

}
