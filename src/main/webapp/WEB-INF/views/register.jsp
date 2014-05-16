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
		<div> 
			<label for="usernamesignup">Your username</label>
	        <div class="input-group">
            	<span class="input-icon"><i class="fa fa-user fa-fw"></i></span>
              	<input id="usernamesignup" name="userName" class="input-control" type="text" placeholder="mysuperusername690" autofocus required></input>
            </div>
		</div>
	    <div> 
	        <label for="emailsignup">Your email</label>
	        <div class="input-group">
            	<span class="input-icon"><i class="fa fa-envelope fa-fw"></i></span>
              	<input id="emailsignup" name="email" class="input-control" autocomplete="off" type="text" placeholder="mysupermail@mail.com" required></input>
            </div> 
	    </div>
	    <div> 
	        <label for="passwordsignup">Your password</label>
	        <div class="input-group">
            	<span class="input-icon"><i class="fa fa-key fa-flip-horizontal fa-fw"></i></span>
              	<input id="passwordsignup" name="password" class="input-control" type="password" placeholder="eg. X8df!90EO" required></input>
            </div> 
	    </div>
	    <div> 
	        <label for="passwordsignup_confirm">Please confirm your password</label>
	    	<div class="input-group">
            	<span class="input-icon"><i class="fa fa-key fa-flip-horizontal fa-fw"></i></span>
              	<input id="passwordsignup_confirm" name="confirmPassword" class="input-control" type="password" placeholder="eg. X8df!90EO" required></input>
            </div>
	    </div>
	</fieldset>
	<fieldset id="registerButtons">
	    <input type="submit" id="signup" class="genericBtn" value="Sign Up">
	    <div>
	        Già Registrato?<br>
	        <a href="javascript:void(0);">Effettua il Log In </a>
	    </div>
	</fieldset>
</form>

<script type="text/javascript" src="<c:url value="resources/js/registrationJS/jquery.balloon.min.js" />"></script>

<script type="text/javascript">
$('#registerButtons').find('a').click(function (){
	$("#overlayPanel").children().hide();
	$("#overlayPanel").hide();	
}); 


$('#signup').click(function() {
	if($('#usernamesignup').val() == "" || $('#emailsignup').val() == "" || $('#passwordsignup').val() == "" || $('#passwordsignup_confirm').val() == "" )
		return;
	
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
			var tooltipPosition = { my: 'center+20 bottom', at: 'center top-5' }; 
     		if(jqXHR.status == 412 ) { 
	           	if(jqXHR.getResponseHeader('userName') != null) {
	           		//showErrorMessage($("#usernamesignup").siblings('label'), jqXHR.getResponseHeader('userName'));
	           		$("#usernamesignup").parent().children().css("border", "1px solid #E00000");
					$("#usernamesignup").parent().children().first().css("border-right", "0");
					$("#usernamesignup").prop('title', jqXHR.getResponseHeader('userName'));
					$("#usernamesignup").tooltip({ position: tooltipPosition });
	           	}
				if(jqXHR.getResponseHeader('email') != null) { 
					//showErrorMessage($("#emailsignup").siblings('label'), jqXHR.getResponseHeader('email')); 
					$("#emailsignup").parent().children().css("border", "1px solid #E00000");
					$("#emailsignup").parent().children().first().css("border-right", "0");
					$("#emailsignup").prop('title', jqXHR.getResponseHeader('email'));
					$("#emailsignup").tooltip({ position: tooltipPosition });
				}
				if(jqXHR.getResponseHeader('password') != null) {  
					//showErrorMessage($("#passwordsignup").siblings('label'), jqXHR.getResponseHeader('password'));
					$("#passwordsignup").parent().children().css("border", "1px solid #E00000");
					$("#passwordsignup").parent().children().first().css("border-right", "0");
					$("#passwordsignup").prop('title', jqXHR.getResponseHeader('password'));
					$("#passwordsignup").tooltip({ position: tooltipPosition });
					$("#passwordsignup_confirm").parent().children().css("border", "1px solid #E00000");
					$("#passwordsignup_confirm").parent().children().first().css("border-right", "0");
					$("#passwordsignup_confirm").prop('title', jqXHR.getResponseHeader('password'));
					$("#passwordsignup_confirm").tooltip({ position: tooltipPosition });
				}
			
           	}
       }
	});
});      
</script>
