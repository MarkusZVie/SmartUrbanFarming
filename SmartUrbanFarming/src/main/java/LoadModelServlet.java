

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class LoadModelServlet
 */

@WebServlet("/upload")
@MultipartConfig
public class LoadModelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadModelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//https://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet
		String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
		String fileName = getSubmittedFileName(filePart);
		InputStream isFileContent = filePart.getInputStream();
		Scanner scanner = new Scanner(isFileContent);
		scanner = scanner.useDelimiter("\\A");
		String fileContent = scanner.hasNext() ? scanner.next() : "";
		scanner.close();
		
		response.getWriter().append("Filename2: " + fileName+ "\n").append(request.getContextPath());
		response.getWriter().append("FileCOntent: " + fileContent + "\n").append(request.getContextPath());
		response.getWriter().append("description: " + description+ "\n").append(request.getContextPath());
		
		
	}
	
	private static String getSubmittedFileName(Part part) {
		//https://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}

}
