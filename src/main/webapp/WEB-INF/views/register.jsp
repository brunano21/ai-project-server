<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
</head>
<body>
<form:form method="post" action="register.html" commandName="registration">
	<% String error = (String) request.getAttribute("error");
	if(null != error ) {%>
		<div id="hibernate.error" class="errors"><%= error %></div>
	<%} %>
	<table>
	<tr>
	<td>Username<font color="red"><form:errors path="userName"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="userName"/></td>
	</tr>
	<tr>
	<td>Password<font color="red"><form:errors path="password"></form:errors></font>
	</td>
	</tr>
	<tr>
	<td><form:password path="password"/></td>
	</tr>
	<tr>
	<td>Confirm Password<font color="red"><form:errors path="confirmPassword"></form:errors></font>
	</td>
	</tr>
	<tr>
	<td><form:password path="confirmPassword"/></td>
	</tr>
	<tr>
	<td>Email<font color="red"><form:errors path="email"></form:errors></font></td>
	</tr>
	<tr>
	<td><form:input path="email"/></td>
	</tr>
	<tr>
	
	<td><input type="submit" value="Submit">
	</td>
	</tr>
	
	</table>
</form:form>

</body>
</html>-->



<div id="registrationSuccessBox" style="display:none;">
Registrazione avvenuta con successo!\n Fra pochi istanti dovresti ricevere una email per confermare la tua registrazione.
</div>
<form  id="registerForm"> 
       <h1> Sign up </h1> 
       <fieldset id="registerInputs" >
           <p> 
               <label for="usernamesignup">Your username</label>
               <input id="usernamesignup" name="userName" required type="text" placeholder="mysuperusername690" />
           </p>
           <p> 
               <label for="emailsignup">Your email</label>
               <input id="emailsignup" name="email" required type="email" autocomplete="off" placeholder="mysupermail@mail.com"/> 
           </p>
           <p> 
               <label for="passwordsignup">Your password</label>
               <input id="passwordsignup" name="password" required type="password" placeholder="eg. X8df!90EO"/>
           </p>
           <p> 
               <label for="passwordsignup_confirm">Please confirm your password</label>
               <input id="passwordsignup_confirm" name="confirmPassword" required type="password" placeholder="eg. X8df!90EO"/>
           </p>
       </fieldset>
       <fieldset id="registerButtons">
           <input type="submit" id="signup" value="Sign Up">
           <p>
               Già Registrato?<br>
               <a href="javascript:void(0);"">Effettua il Log In </a>
           </p>
       </fieldset>
 </form>
 
 <script type="text/javascript">
$('#registerButtons').find('a').click(function (){
	$("#overlayPanel").children().hide();
	$("#overlayPanel").hide();	
}); 

$('#signup').click(function() { 
	var userData = $("#registerForm :input").serializeArray();
	$.ajax({
	   	url:"./register", 
	       type: 'POST', 
	       async: false, 
	       data :userData, 
	       success: function(returnedData, textStatus, jqXHR) {         
	       	console.log("done!");
			$("#registerForm").fadeOut();
			$("#registrationSuccessBox").fadeIn();
			setInterval(
				function() {
					$("#overlayPanel").children().hide();
					$("#overlayPanel").hide();	
				}, 
				3000);
	           
	       },

	       error: function(jqXHR, textStatus, errorThrown) { 
	       	if(jqXHR.status == 412 ) { 
               if(jqXHR.getResponseHeader('userName') != null)
                   console.log("userName - error");
				if(jqXHR.getResponseHeader('email') != null)
					console.log("email - error");
				if(jqXHR.getResponseHeader('password') != null)
					console.log("password - error");
	           }
	       }
	});
});      
</script>
