<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Add a new Business on this page</title>
	</head>
	<body>
		<h1>Hi, you can add a new business on this page</h1>
		<form action="add" enctype="multipart/form-data" method="post">
			Name: <input type="text" name="businessName"/>
			Phone: <input type="text" name="phoneNumber"/>
			Email: <input type="text" name="businessEmail"/>
			Address: <input type="text" name="address"/>
			postcode: <input type="text" name="postcode"/>
			capacity: <input type="text" name="capacity"/>
			<input type="submit" value="Add"/>
		</form>
	</body>
</html>