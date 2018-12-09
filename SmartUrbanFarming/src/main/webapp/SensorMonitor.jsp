    <%@page import="java.text.SimpleDateFormat"%>
<%@page import="model_parser.DB_connection"%>
    <%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
    <%@ page import="java.util.*" %>
    <%@ page import="com.google.gson.Gson"%>
    <%@ page import="com.google.gson.JsonObject"%>
     
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
    
    ArrayList<String[]> dbValue = DB_connection.getSensorData();
    
    for(int i = 1; i<dbValue.size();i++){
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
     
    <!DOCTYPE HTML>
<html>
<head>  
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
</head>
<body>
<div id="chartContainer" style="height: 370px; width: 100%;"></div>
<div id="chartContainer2" style="height: 370px; width: 100%;"></div>
<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
</body>
</html>