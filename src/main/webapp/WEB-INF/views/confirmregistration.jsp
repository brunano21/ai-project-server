<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="refresh" content="3;url=http://localhost:8080/supermarket/" />
<title>Registration Confirmed</title>
</head>
<body>
	<% 
	String noUserError = (String) request.getAttribute("noUser");
	if(noUserError != null) { %>
		<h1><font color="red"><%= noUserError %></font></h1>
	<%} 
	else { 
		String alreadyConfimedError = (String) request.getAttribute("alreadyConfirmed");
		if(alreadyConfimedError != null) { %>
			<h1><font color="red"><%= alreadyConfimedError %></font></h1>
			<% }else{%>
				<h1>Registrazione Confermata</h1>
				<h2>Verrai rediretto alla homepage in 3 secondi</h2>
		<%} 
	}%>
	
	
</body>
</html>