<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Edit an existing business on this page</title>
	</head>
	<body>
		<h1>You can edit the business shown below</h1>
		<sf:form method="POST" modelAttribute="business">
			<fieldset>
				<table>
					<tr>
						<th><label for="businessName">Business Name:</label></th>
						<td><sf:input path="businessName"/></td>
					</tr>
					<tr>
						<th><label for="phoneNumber">phoneNumber:</label></th>
						<td><sf:input path="phoneNumber"/></td>
					</tr>
					<tr>
						<th><label for="businessEmail">Email:</label></th>
						<td><sf:input path="businessEmail"/></td>
					</tr>
					<tr>
						<th><label for="address">address:</label></th>
						<td><sf:input path="address"/></td>
					</tr>
					<tr>
						<th><label for="postcode">Postcode:</label></th>
						<td><sf:input path="postcode"/></td>
					</tr>
					<tr>
						<th><label for="capacity">Capacity:</label></th>
						<td><sf:input path="capacity"/></td>
					</tr>
					<tr>
						<th><label for="photo">photo:</label></th>
						<td><sf:input path="photo"/></td>
					</tr>																					
					<tr>
						<th><a href="hello.htm"><button>Cancel</button></a></th>
						<!-- This hidden field is required for Hibernate to recognize this Product -->
						<td><sf:hidden path="id"/>
						<td><input type="submit" value="Done"/></td>
					</tr>
				</table>			
			</fieldset>
		</sf:form>
	</body>
</html>
