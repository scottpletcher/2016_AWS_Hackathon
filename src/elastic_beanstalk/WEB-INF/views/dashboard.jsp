<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="resources/css/style.css" rel="stylesheet">
<title>City of Whoville: DPW</title>
</head>
<body>
	</div><h1>Welcome to the City of Whoville DPW on demand waste pickup
		system.</h1>
	<table>
		<tr>
			<tr><td>Please select your address from the dropdown below.</td></tr>
			<td><form:form method="POST"
					name="dropDown" action="schedule">
					<table>
						<select name="address">
							<option value="-" />--Select Address--</option>
							<c:forEach var="address" items="${addresses}" varStatus="status">
								<option value="${devices[status.index] }">"${address }"</option>>
							</c:forEach>
						</select>
						<select name="type">
							<option value="-">--SELECT Waste Type--</option>
							<option value="garbage">Trash</option>
							<option value="recycling">Recycling</option>
							<option value="composting">Compost</option>
						</select>
						<tr>
							<tr><td><input name="schedule" type="submit" value="Schedule Pickup" /></td></tr>
							<tr><td><input name="schedule" type="submit" value="Get History" /></td></tr>
						</tr>
					</table>
				</form:form></td>
		</tr>
	</table>
</body>
</html>