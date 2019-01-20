<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Model XML File Uploading</title>
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

</head>
<body class="w3-light-grey">

	<!-- Navbar (sit on top) -->
	<div class="w3-top">
		<div class="w3-bar w3-white w3-padding w3-card"	style="letter-spacing: 4px;">
			<a href="../SmartUrbanFarming" class="w3-bar-item w3-button">Home</a>
			<!-- Right-sided navbar links. Hide them on small screens -->
			<div class="w3-right w3-hide-small">
				<a href="#content" class="w3-bar-item w3-button">Upload XML</a>
			</div>
		</div>
	</div>

	<!-- Header -->
	<header class="w3-display-container w3-white w3-content w3-wide" style="max-width: 1200px; min-width: 500px" id="home">
		<br>
		<br>
		    <h1 class="w3-xxlarge w3-padding">Upload Model XML File</h1>
		
	</header>

	<!-- Page content -->
	<div class="w3-content w3-white" style="max-width: 1200px">
		<div class="w3-row w3-margin w3-padding-16" id="content">
			
			 Please Select Model XML File <br />
		      <form action = "UploadFileHandler" method = "post" enctype = "multipart/form-data">
		         <input type = "file" name = "file" size = "50" />
		         <br />
		         <input type = "submit" value = "Upload File" />
		      </form>
		</div>
	</div>

	<!-- Footer -->
	<footer class="w3-center w3-light-grey w3-padding-32">
		<p>
			Powered by <a href="https://www.w3schools.com/w3css/default.asp" title="W3.CSS" target="_blank" class="w3-hover-text-green">w3.css</a>
		</p>
	</footer>

</body>
</html>



