<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<link href="resources/css/style.css" rel="stylesheet">
</head>
<body>

	<h1>History of scheduled pickups </h1>

<table>

<c:forEach var="entry" items="${time}" varStatus="status">
								<tr><td>${entry }</td><td>${type[status.index] }</td></tr>
							</c:forEach>
</table>

</body>
</html>