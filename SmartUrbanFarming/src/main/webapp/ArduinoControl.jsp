<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" charset="ISO-8859-1" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	 Please Write what ever the Arduino should display <br />
      <form action = "ArduinoControl" method = "post" enctype = "multipart/form-data">
         <input type="text" name="content"><br>
         <br />
         <input type = "submit" value = "upload text" />
         <input type="button" value="Refresh Log" onClick="window.location.reload()">
      </form>
      
      <div>
      <%String name = (String)request.getAttribute("log"); %>
	  <%= name%>
      </div>
</body>
</html>