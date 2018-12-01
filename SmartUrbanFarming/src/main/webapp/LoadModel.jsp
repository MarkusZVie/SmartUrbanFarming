<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
   <head>
      <title>Model XML File Uploading</title>
   </head>
   <body>
     Please Select Model XML File <br />
      <form action = "UploadFileHandler" method = "post" enctype = "multipart/form-data">
         <input type = "file" name = "file" size = "50" />
         <br />
         <input type = "submit" value = "Upload File" />
      </form>
   <a href="index.jsp"> Back to Start Page </a>   
   </body>
   
</html>