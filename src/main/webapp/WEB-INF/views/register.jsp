<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
	        <a href="javascript:void(0);">Effettua il Log In </a>
	    </p>
	</fieldset>
</form>

<script type="text/javascript" src="<c:url value="resources/js/registrationJS/jquery.balloon.min.js" />"></script>

<script type="text/javascript">
$('#registerButtons').find('a').click(function (){
	$("#overlayPanel").children().hide();
	$("#overlayPanel").hide();	
}); 

function showErrorMessage(div,msg) {
	div.showBalloon({
		contents : ""+msg,
		minLifeTime: 5000,
		showDuration: 500,	
		hideDuration: 500,
		offsetX: 10,
        position : "right",
		tipSize: 0,
        css : {
        	border: '1px solid rgb(255, 0, 0)',
            backgroundColor: 'rgb(255, 255, 255)',
            color: 'rgb(111, 111, 111)',
            boxShadow : "none",
			borderRadius: "2px",
			textAlign: "right"
            
        }
	});
}

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
	           		showErrorMessage($("#usernamesignup").siblings('label'), jqXHR.getResponseHeader('userName'));
				if(jqXHR.getResponseHeader('email') != null) 
					showErrorMessage($("#emailsignup").siblings('label'), jqXHR.getResponseHeader('email'));
				if(jqXHR.getResponseHeader('password') != null) 
					showErrorMessage($("#passwordsignup").siblings('label'), jqXHR.getResponseHeader('password'));
			
           	}
       }
	});
});      
</script>
