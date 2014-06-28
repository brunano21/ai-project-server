<%@ page import="java.util.ArrayList" %>
<%@ page import="hibernate.Inserzione" %>
<%@ page import="dati.Dati" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<script type="text/javascript">

function getSuggerimentoSingolo(_this) {
	console.log("ID: " + ($(_this).parent().parent().parent().attr("id")+"").split("_")[1]);
	console.log("TIPO: " + ($(_this).parent().parent().parent().parent().attr("id")+"").split("-")[2]);
	$.ajax({
   		url:"./suggerimentoSingolo", 
      	type: 'POST', 
      	async: false, 
      	data : {
      		"idInserzione" : ($(_this).parent().parent().parent().attr("id")+"").split("_")[1],
      		"tipoSuggerimento" : ($(_this).parent().parent().parent().parent().attr("id")+"").split("-")[2]
      	}, 
      	success: function(returnedData) {         
		   	console.log("suggerimentosingolo ricevuto!");
			
		   	$("#overlayPanel").show();
		   	$("#suggerimento-singolo-container").html(returnedData).show();
       }
	});
}
</script>

<div id="carousel-user-container">
	<div id="carousel-user-evaluation-container">
		<h5>DA VALUTARE</h5>
		<div id="carousel-list-evaluation" class="widget-container">
			<%
			 List inserzioniDaValutareIDsList = null;
			 Inserzione inserzioneDaValutare = null;
			 List inserzioniDaSuggerireIDsList = null;
			 Inserzione inserzioneDaSuggerire = null;
			 String supermercato = null;
			 
			 //TODO Aggiungere else nel caso non ci siano le coordinate
			 if (request.getParameter("latitudine") != null || request.getParameter("longitudine") != null)
				inserzioniDaValutareIDsList = Dati.getInstance().getInserzioniDaValutare(request.getUserPrincipal().getName(), request.getParameter("latitudine"), request.getParameter("longitudine"));
			 
			 inserzioniDaValutareIDsList = Dati.getInstance().getInserzioniValide();
			 
			 for (Object index : inserzioniDaValutareIDsList) {
				inserzioneDaValutare = Dati.getInstance().getInserzioni().get((Integer)index);
			 	supermercato = inserzioneDaValutare.getSupermercato().getNome() + ", " + inserzioneDaValutare.getSupermercato().getIndirizzo() + ", " + inserzioneDaValutare.getSupermercato().getComune();
			 %>
				<div id=<%= "suggerimento_" + inserzioneDaValutare.getIdInserzione() %> class="carousel-item">
					<div class="post-margin">
						<h6>
							<a href="javascript:void(0);" onclick="getSuggerimentoSingolo(this);"><%= inserzioneDaValutare.getProdotto().getDescrizione() %></a>
						</h6>
						<span><i class="fa fa-clock-o"></i><%= inserzioneDaValutare.getDataFine() %></span>
					</div>
					<div class="featured-image">
						<img class="featured-image-img" src="<%= inserzioneDaValutare.getFoto() %>" />
					</div>
					<div class="post-margin">
						<i class="fa fa-map-marker"></i><%= supermercato %></div>
					<div class="post-margin">
						<i class="fa fa-eur"></i><%= inserzioneDaValutare.getPrezzo() %></div>
				</div>
			<%
			 }
			 %>
		</div>
	</div>
	<div id="carousel-user-suggestion-container">
		<h5>IN SCADENZA</h5>
		<div id="carousel-list-suggestion" class="widget-container">
			<%
			 //TODO Aggiungere else nel caso non ci siano le coordinate
			 if (request.getParameter("latitudine") != null || request.getParameter("longitudine") != null)
				inserzioniDaSuggerireIDsList = Dati.getInstance().getInserzioniInScadenza(request.getUserPrincipal().getName(), request.getParameter("latitudine"), request.getParameter("longitudine"));
			
			 inserzioniDaSuggerireIDsList = Dati.getInstance().getInserzioniValide();
			 
			 for (Object index : inserzioniDaSuggerireIDsList) {
				inserzioneDaSuggerire = Dati.getInstance().getInserzioni().get((Integer)index);
			 	supermercato = inserzioneDaSuggerire.getSupermercato().getNome() + ", " + inserzioneDaSuggerire.getSupermercato().getIndirizzo() + ", " + inserzioneDaSuggerire.getSupermercato().getComune();
			 %>
				<div id=<%= "suggerimento_" + inserzioneDaSuggerire.getIdInserzione() %> class="carousel-item">
					<div class="post-margin">
						<h6>
							<a href="javascript:void(0);" onclick="getSuggerimentoSingolo(this);"><%= inserzioneDaSuggerire.getProdotto().getDescrizione() %></a>
						</h6>
						<span><i class="fa fa-clock-o"></i><%= inserzioneDaSuggerire.getDataFine() %></span>
					</div>
					<div class="featured-image">
						<img class="featured-image-img" src="<%= inserzioneDaSuggerire.getFoto() %>" />
					</div>
					<div class="post-margin">
						<i class="fa fa-map-marker"></i><%= supermercato %></div>
					<div class="post-margin">
						<i class="fa fa-eur"></i><%= inserzioneDaSuggerire.getPrezzo() %></div>
				</div>
			<%
			 }
			 %>
		</div>
	</div>
</div>

<script type="text/javascript">
/*
 *Indicates which easing function to use for the transition. jQuery defaults: "linear" and "swing", built in: "quadratic", "cubic" and "elastic". 
 
 */
	var startCarousels = function() {
		
		$('#carousel-list-evaluation').carouFredSel({
	        items               : 1,
	        direction           : "up",
	        scroll : {
	            items           : 1,
	            easing          : "cubic",
	            duration        : 1000,                         
	            pauseOnHover    : true
	        }                   
	    });
		
		$('#carousel-list-suggestion').carouFredSel({
	        items               : 2,
	        direction           : "up",
	        scroll : {
	            items           : 1,
	            easing          : "cubic",
	            duration        : 1000,                         
	            pauseOnHover    : true
	        }                   
	    });
		
		$("#carousel-user-suggestion-container >.caroufredsel_wrapper").css("height", "531px");
	};
 </script>
