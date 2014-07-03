<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<script type="text/javascript">
console.log($("#logContainer").html());
if($("#logContainer").html().indexOf("Bentornato") > -1){
	console.log("sessione scaduta");
	$.ajax({
    	url:"./loginForm", 
        type: 'GET', 
        async: false, 
        data: { }, 
        success: function(data){
        	$("#logContainer").html(data);
        }
   	});
}
$("#logContainer").effect("shake");
</script>
