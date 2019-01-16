<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Messages.Message"%>
<%@page import="Messages.NotificationHandler"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ruleManagement.RuleManager"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Plant Status</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<style>
body {
	font-family: "Times New Roman", Georgia, Serif;
}

h1, h2, h3, h4, h5, h6 {
	font-family: "Playfair Display";
	letter-spacing: 5px;
}

.floatingBox {
  float: left;
  height: 75px;
  margin: 10px;
  border: 3px dashed #000000;  
}

</style>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

</head>
<body class="w3-light-grey">

	<!-- Navbar (sit on top) -->
	<div class="w3-top">
		<div class="w3-bar w3-white w3-padding w3-card"	style="letter-spacing: 4px;">
			<a href="../SmartUrbanFarming" class="w3-bar-item w3-button">Home</a>
			<!-- Right-sided navbar links. Hide them on small screens -->
			<div class="w3-right w3-hide-small">
				<a href="#home" class="w3-bar-item w3-button">Top</a>
				<a href="#facts" class="w3-bar-item w3-button">Facts</a>
				<a href="#Notifications" class="w3-bar-item w3-button">Notifications</a>
			</div>
		</div>
	</div>

	<!-- Header -->
	<header class="w3-display-container w3-white w3-content w3-wide" style="max-width: 1200px; min-width: 500px" id="home">
		<br>
		<br>
		    <h1 class="w3-xxlarge w3-padding">Plant Status</h1>
		
	</header>

	<!-- Page content -->
	<div class="w3-content w3-white" style="max-width: 1200px">

		<div class="w3-row w3-padding-64" id="facts">
		
			<%
	  		
			RuleManager ru = RuleManager.getInstance();
			ArrayList<String> factList = ru.getFactList();
			for(String s: factList){
				 out.print("<div class='floatingBox'>");
				 out.print("<div class='w3-center w3-padding'>");
				 out.print(s + "<br>");
				out.print(ru.getFact(s));
				 out.print("</div>");
				 out.print("</div>");
			}
			 
			%>
		
		</div>
			
		<hr>
		
		<%
		NotificationHandler nh = NotificationHandler.getInstance();
		ArrayList<Message> messages = nh.getActiveMessages();
		int messageid=0;
		for(Message m: messages){
		%>
		<div class="w3-row" id="Notifications">
			<div class="w3-margin-64" id="Notifications">
				<button type="button" class="btn btn-info w3-margin-right w3-margin-bottom w3-margin-left" data-toggle="collapse" data-target="#collapseable<% out.print(messageid); %>" style="width: calc(100% - 32px);"><% out.print(m.getSubject()); %></button>
				<div id="collapseable<% out.print(messageid++); %>" class="collapse">
					<div class="w3-margin">
					<%
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					out.print("Creation Date:<br> " + sdf.format(m.getCreationDate()) + "<br><br>");
					out.print("Related Fact:<br> " + m.getRelatedFact() + "<br><br>");
					out.print("Message:<br>" + m.getMessage() + "<br><br>");
					
					
					%>				
					</div>
				</div>
			</div>
		</div>
		
		<%
		} 
		%>
		<hr>


		<!-- End page content -->
	</div>

	<!-- Footer -->
	<footer class="w3-center w3-light-grey w3-padding-32">
		<p>
			Powered by <a href="https://www.w3schools.com/w3css/default.asp" title="W3.CSS" target="_blank" class="w3-hover-text-green">w3.css</a>
		</p>
	</footer>

</body>
</html>
