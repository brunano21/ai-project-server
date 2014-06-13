<%@ page import="java.util.ArrayList" %>
<%@ page import="hibernate.Inserzione" %>
<%@ page import="dati.Dati" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<form id="logoutForm" action="javascript:void(0);"
	onsubmit="sendLogout();">
	<h4>Bentornato, ${username}!</h4>
	<div>
		<p>Reputazione: ${reputazione}</p>
		<p>Crediti Pendenti: ${creditiPendenti}</p>
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
        	window.location.replace("http://localhost:8080/supermarket/");
        }    
    });
});
</script>

<!-- PER IL MOMENTO VENGONO GENERATI I DUE CAROUSEL, MA NON RIESCO AD ACCEDERE ALLA REQUEST QUINDI NON RIESCO AD AVERE LONGITUDINE E LATITUDINE
INFATTI ORA VENGONO RITORNATI VALORI A CAZZO TANTO PER CAPIRE SE FUNZIONAVA. LA RICHIESTA PER OTTENERE I CAROUSEL CMQ A MIO PARERE VA SPLITTATA PERCHè ALTRIMENTI 
NE RISENTE TROPPO LA VELOCITà DI RISPOSTA. QUINDI LA SPOSTERò IN SEGUITO -->
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
			 //if (request.getParameter("latitudine") != null || request.getParameter("longitudine") != null)
			//	inserzioniDaValutareIDsList = Dati.getInstance().getInserzioniDaValutare(request.getUserPrincipal().getName(), request.getParameter("latitudine"), request.getParameter("longitudine"));
			 
			 inserzioniDaValutareIDsList = Dati.getInstance().getInserzioniValide();
			 
			 for (Object index : inserzioniDaValutareIDsList) {
				inserzioneDaValutare = Dati.getInstance().getInserzioni().get((Integer)index);
			 	supermercato = inserzioneDaValutare.getSupermercato().getNome() + ", " + inserzioneDaValutare.getSupermercato().getIndirizzo() + ", " + inserzioneDaValutare.getSupermercato().getComune();
			 %>
				<div class="carousel-item">
					<div class="post-margin">
						<h6>
							<a href="#"><%= inserzioneDaValutare.getProdotto().getDescrizione() %></a>
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
	//		 if (request.getParameter("latitudine") != null || request.getParameter("longitudine") != null)
	//			inserzioniDaSuggerireIDsList = Dati.getInstance().getInserzioniInScadenza(request.getUserPrincipal().getName(), request.getParameter("latitudine"), request.getParameter("longitudine"));
			
			 inserzioniDaSuggerireIDsList = Dati.getInstance().getInserzioniValide();
			 
			 for (Object index : inserzioniDaSuggerireIDsList) {
				inserzioneDaSuggerire = Dati.getInstance().getInserzioni().get((Integer)index);
			 	supermercato = inserzioneDaSuggerire.getSupermercato().getNome() + ", " + inserzioneDaSuggerire.getSupermercato().getIndirizzo() + ", " + inserzioneDaSuggerire.getSupermercato().getComune();
			 %>
				<div class="carousel-item">
					<div class="post-margin">
						<h6>
							<a href="#"><%= inserzioneDaSuggerire.getProdotto().getDescrizione() %></a>
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
var startCarousels = function() {
	
	$('#carousel-list-evaluation').carouFredSel({
        //auto                : true,
        //circular: true,
        items               : 1,
        direction           : "up",
        //height: 740,
        scroll : {
            items           : 1,
            easing          : "elastic",
            duration        : 1000,                         
            pauseOnHover    : true
        }                   
    });
	
	$('#carousel-list-suggestion').carouFredSel({
        //auto                : true,
        //circular: true,
        items               : 2,
        direction           : "up",
        //height: 740,
        scroll : {
            items           : 1,
            easing          : "elastic",
            duration        : 1000,                         
            pauseOnHover    : true
        }                   
    });
	
};
 </script>
