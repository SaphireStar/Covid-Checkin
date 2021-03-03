<%@ include file="/WEB-INF/views/include.jsp"%>
<html>
	<head>
		<title><fmt:message key="title" /></title>
	</head>
	<body>
		<h1>
			<fmt:message key="heading" />
		</h1>
		<p>
			<fmt:message key="greeting" />
			<c:out value="${model.now}" />
		</p>
		<h3>Businesses</h3>
		<table>
		<tr>
			<th>Name</th>
			<th>address</th>
			<th>postcode</th>
			<th>phoneNumber</th>
			<th>email</th>
			<th>capacity</th>
			<th>options</th>
		</tr>
		<c:forEach items="${model.businesses}" var="biz">
			<tr>
				<td><c:out value="${biz.businessName}" /></td>
				<td><i><c:out value="${biz.address}" /></i></td>
				<td><i><c:out value="${biz.postcode}" /></i></td>
				<td><i><c:out value="${biz.phoneNumber}" /></i></td>
				<td><i><c:out value="${biz.businessEmail}" /></i></td>
				<td><i><c:out value="${biz.capacity}" /></i></td>
				<td><a href="business/edit/${biz.id }">edit</a>
				<a href="business/delete/${biz.id }">delete</a></td>
				<br>
				<br>
			<tr>
		</c:forEach>
		</table>
	</body>
</html>