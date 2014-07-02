<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<form id="logoutForm" action="javascript:void(0);"
	onsubmit="sendLogout();">
	<h4>Bentornato, ${username}!</h4>
	<div>
		<p>Reputazione: ${reputazione}%</p>
		<p>Crediti Pendenti: ${creditiPendenti}</p><p id="incrementoCrediti" style="color: red; font-size: medium;"></p>
		<p>Crediti Acquisiti: ${creditiAcquisiti}</p>
	</div>
	<input type="submit" id="logout" class="genericBtn" value="Logout">
</form>

<script type="text/javascript">
$("#logout").click(function(){
	$.ajax({url:"./j_spring_security_logout", 
        type: 'POST', 
        async: false, 
        data: { }, 
        success: function(returnedData) {         
        	window.location.replace(document.URL);
        }    
    });
});
</script>

