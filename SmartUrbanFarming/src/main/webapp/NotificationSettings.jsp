<%@page import="mailing.NotificationMailer"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Notification Settings</title>
<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #dddddd;
}
</style>
</head>
<body>

<form action = "NotificationSettings" method = "post" enctype = "multipart/form-data">
<table>
  <tr>
    <th>Mail</th>
    <th>Delete Mail</th>
  </tr>
  
  <%
  
  ArrayList<String> mailList = NotificationMailer.getInstance().getNotificationMailList();
  for(int i =0; i<mailList.size() ; i++){
	  out.print("<tr><td>" + mailList.get(i) + "<td><input type='checkbox' name='toDeleteMail' value='"+mailList.get(i)+"'></td></tr>");
  }
  
  %>
  <tr>
    <td><input type="mail" name="newMail"></td>
    <td><input type="submit"></td>
  </tr>
</table>

</form> 
</body>
</html>