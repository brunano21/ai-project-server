<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>


<form id="logoutForm" action="javascript:void(0);" onsubmit="sendLogout();">
	<h4>Bentornato, ${username}! </h4>
	<div>
		<p>Reputazione: ${reputazione} </p>
		<p>Crediti Pendenti: ${creditiPendenti}</p>
		<p>Crediti Acquisiti: ${creditiAcquisiti}</p>
	</div>
	<input type="submit" id="logout" value="Logout"> 
</form>


