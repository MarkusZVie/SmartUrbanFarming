<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Messages.Message"%>
<%@page import="Messages.NotificationHandler"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ruleManagement.RuleManager"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="model_parser.DB_connection"%>
<%@ page import="java.util.*" %>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>

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


 <%
 	 
    //source: https://canvasjs.com/jsp-charts/performance-demo-chart/
    Gson gsonObj = new Gson();
    List<Map<Object,Object>> list1 = new ArrayList<Map<Object,Object>>();
    Map<Object,Object> map1 = null;
    List<Map<Object,Object>> list2 = new ArrayList<Map<Object,Object>>();  
    Map<Object,Object> map2 = null;
    List<Map<Object,Object>> list3 = new ArrayList<Map<Object,Object>>(); 
    Map<Object,Object> map3 = null;
    List<Map<Object,Object>> list4 = new ArrayList<Map<Object,Object>>();
    Map<Object,Object> map4 = null;
    
    ArrayList<String[]> dbValue = DB_connection.getSensorData(request.getParameter("begin"),request.getParameter("end"),request.getParameter("quickSelect"));
    Date beginDate= new Date();
    Date endDate = new Date();
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    SimpleDateFormat sdfDateOnly = new SimpleDateFormat("yyyy-MM-dd");
    
    for(int i = 1; i<dbValue.size();i++){
    	if(i==1){
    		beginDate = sdf.parse(dbValue.get(i)[1]);
    	}
    	if(i==(dbValue.size()-1)){
    		endDate = sdf.parse(dbValue.get(i)[1]);
    	}
    	map1 = new HashMap<Object,Object>(); 
    	map1.put("label", dbValue.get(i)[1]);
    	map1.put("y", Float.parseFloat(dbValue.get(i)[2])); 
    	list1.add(map1);
    	
    	map2 = new HashMap<Object,Object>(); 
    	map2.put("label", dbValue.get(i)[1]);
    	map2.put("y", Float.parseFloat(dbValue.get(i)[3])); 
    	list2.add(map2);
    	
    	map3 = new HashMap<Object,Object>(); 
    	map3.put("label", dbValue.get(i)[1]);
    	map3.put("y", Float.parseFloat(dbValue.get(i)[4])); 
    	list3.add(map3);
    	
    	map4 = new HashMap<Object,Object>(); 
    	map4.put("label", dbValue.get(i)[1]);
    	map4.put("y", Float.parseFloat(dbValue.get(i)[5])); 
    	list4.add(map4);
    }
    
    String dataPoints1 = gsonObj.toJson(list1);
    String dataPoints2 = gsonObj.toJson(list2);
    String dataPoints3 = gsonObj.toJson(list3);
    String dataPoints4 = gsonObj.toJson(list4);
    %>

<script>
window.onload = function () {

CanvasJS.addColorSet("one",
	        [//colorSet Array
	        "#0066ff",
	        "#ffcc00",
	        "#339933"
	        ]);
	
var chart = new CanvasJS.Chart("chartContainer", {
	colorSet: "one",
	title: {
		text: "Sensor Output"
	},
	axisX: {
		valueFormatString: "MMM YYYY"
	},
	axisY2: {
		title: "Percent",
		prefix: "",
		suffix: "%"
	},
	toolTip: {
		shared: true
	},
	legend: {
		cursor: "pointer",
		verticalAlign: "top",
		horizontalAlign: "center",
		dockInsidePlotArea: true,
		itemclick: toogleDataSeries
	},
	data: [{
		type:"line",
		axisYType: "secondary",
		name: "Humidity",
		showInLegend: true,
		markerSize: 0,
		yValueFormatString: "#0.0# %",
		dataPoints: <%out.print(dataPoints1);%>
	},
	{
		type: "line",
		axisYType: "secondary",
		name: "Light",
		showInLegend: true,
		markerSize: 0,
		yValueFormatString: "##0.0# %",
		dataPoints: <%out.print(dataPoints3);%>
	},
	{
		type: "line",
		axisYType: "secondary",
		name: "Hygrometer",
		showInLegend: true,
		markerSize: 0,
		yValueFormatString: "#0.0# %",
		dataPoints: <%out.print(dataPoints4);%>
	}]
});
chart.render();

CanvasJS.addColorSet("red",
        [//colorSet Array
        "#cc0000"          
        ]);

var chart2 = new CanvasJS.Chart("chartContainer2", {
	title: {
		text: " "
	},
	colorSet: "red",
	axisX: {
		valueFormatString: "MMM YYYY"
	},
	axisY2: {
		title: "Grad Celsius",
		prefix: "",
		suffix: "°C"
	},
	toolTip: {
		shared: true
	},
	legend: {
		cursor: "pointer",
		verticalAlign: "top",
		horizontalAlign: "center",
		dockInsidePlotArea: true,
		itemclick: toogleDataSeries
	},
	data: [{
		type: "line",
		axisYType: "secondary",
		name: "Temperature",
		showInLegend: true,
		markerSize: 0,
		yValueFormatString: "#0.0# °C",
		dataPoints: <%out.print(dataPoints2);%>
	}]
});
chart2.render();

function toogleDataSeries(e){
	if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
		e.dataSeries.visible = false;
	} else{
		e.dataSeries.visible = true;
	}
	chart.render();
}

}
</script>

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
				<a href="#filter" class="w3-bar-item w3-button">Filter</a>
				<a href="#sensorDatea" class="w3-bar-item w3-button">SensorData</a>
			</div>
		</div>
	</div>

	<!-- Header -->
	<header class="w3-display-container w3-white w3-content w3-wide" style="max-width: 1200px; min-width: 500px" id="home">
		<br>
		<br>
		    <h1 class="w3-xxlarge w3-padding">Sensor Output</h1>
		
	</header>

   



	<!-- Page content -->
	<div class="w3-content w3-white" style="max-width: 1200px">

		<div class="w3-row" id="filter">
			<div class="w3-margin-64" id="Notifications">
				<button type="button" class="btn btn-info w3-margin" data-toggle="collapse" data-target="#collapseableFilter" style="width: calc(100% - 32px);">Show Filter</button>
				<div id="collapseableFilter" class="collapse w3-margin">
					
					<form action="SensorMonitorAdjustable.jsp">
						Sensor-Data from: <input id="begin" name="begin" type='date' min='<% out.print(sdfDateOnly.format(beginDate)); %>' max='<% out.print(sdfDateOnly.format(endDate)); %>'></input><br>
						Sensor-Data to:   <input id="end" name="end" type='date' min='<% out.print(sdfDateOnly.format(beginDate)); %>' max='<% out.print(sdfDateOnly.format(endDate)); %>'></input><br>
						<hr>
						<input type="radio" name="quickSelect" value="day">Select Sensor-Data today<br>
						<input type="radio" name="quickSelect" value="week"> Select Sensor-Data this week<br>
						<input type="radio" name="quickSelect" value="month"> Select Sensor-Data this month<br>
						<input type="radio" name="quickSelect" value="all" checked> Select Sensor-Data all the time<br>
						<hr>
						<input type="submit">
					</form>
				</div>
			</div>
		</div>
			
		<hr>
		<div class="w3-row" id="sensorDatea">
			<div id="chartContainer" style="height: 370px; width: 100%;"></div>
			<div id="chartContainer2" style="height: 370px; width: 100%;"></div>
			<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
		
		</div>
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




     