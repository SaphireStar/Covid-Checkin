<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upload Files Here</title>
	</head>
	<body>
		<h1>Hi, you can upload files here</h1>
			<form action="photo" method="post" enctype="multipart/form-data">  
			Select File: <input type="file" name="photo"/>  
			<input type="submit" value="Upload Photo"/>  
			</form>  
	</body>
</html>