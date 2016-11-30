<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<link href="resources/css/style.css" rel="stylesheet">
</head>
<body>

	<h1>Below is the scheduled pickup for device ${address } and type ${type }</h1>

<table>
<tr><td>Day</td><td>Time</td><td>Type</td></tr>
<tr><td>21 December 2016</td><td>8:00 am</td><td>${type }</td></tr>
</table>

</body>
</html>