<%@ page import="hibernate.Inserzione" %>
<%@ page import="java.util.List" %>
<%@ page import="java.security.Principal" %>
<%@ page import="dati.Dati" %>
<%@ page import="javax.servlet.ServletContext" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="<c:url value="resources/js/valutazioneJS/valutazione.js" />"></script>

<div class="valutazioneContainer">
	<div class="valutazioneBox">

		<h2>VALUTAZIONI PROPOSTE</h2>
		<div class="valutazioniProposte">
			<div class="bx-wrapper">
				<div class="bx-viewport">
					<div class="slider1">
						<%
						//List<Integer> valutazioniIDs = Dati.getInstance().getInserzioniDaValutare(request.getUserPrincipal().getName(), request.getParameter("latitudine"), request.getParameter("longitudine"));
						List<Integer> valutazioniIDs = Dati.getInstance().getInserzioniDaValutareProposte(request.getUserPrincipal().getName());
						Inserzione valutazione = null;
						String supermercato = null;
						
						for(Integer index : valutazioniIDs) {
							valutazione = Dati.getInstance().getInserzioni().get(index);
						 	supermercato = valutazione.getSupermercato().getNome() + ", " + valutazione.getSupermercato().getIndirizzo() + ", " + valutazione.getSupermercato().getComune();
						%>
						<div class="slide bx-clone">
							<div id="valutazione_<%= valutazione.getIdInserzione() %>" class="slider-item">
								<h6>
									<a href="#"></a><%= valutazione.getProdotto().getDescrizione() %>
								</h6>
								<div class="featured-image">
									<img class="featured-image-img"
										src="<%= valutazione.getFoto() %>" />
								</div>
								<div class="data-periodo">
									<div class="data-inizio">
										<i class="fa fa-clock-o"></i><%= valutazione.getDataInizio() %>
									</div>
									<div class="data-fine">
										<i class="fa fa-clock-o"></i><%= valutazione.getDataFine() %>
									</div>
								</div>
								<div class="supermercato-box">
									<i class="fa fa-map-marker"></i><%= supermercato %>
								</div>
								<div class="prezzo-box">
									<i class="fa fa-eur"></i><%= valutazione.getPrezzo() %>
								</div>
								<div class="valutazione-box">
									<button type="button" class="valutazione-negativa">-1</button>
									<button type="button" class="valutazione-positiva">+1</button>
								</div>
								<div class="etichetta-votato">VOTATO</div>
							</div>
						</div>
						<%
						}
						%>
					</div>
				</div>
			</div>
		</div>

		<h2>VALUTAZIONI SUGGERITE</h2>
			<div class="valutazioniSuggerite">
				<div class="bx-wrapper">
				<div class="bx-viewport">
					<div class="slider2">
					<%
					valutazioniIDs = Dati.getInstance().getInserzioniDaValutareSuggerite(request.getUserPrincipal().getName(), request.getParameter("latitudine"), request.getParameter("longitudine"));
					
						for(Integer index : valutazioniIDs) {
							valutazione = Dati.getInstance().getInserzioni().get(index);
						 	supermercato = valutazione.getSupermercato().getNome() + ", " + valutazione.getSupermercato().getIndirizzo() + ", " + valutazione.getSupermercato().getComune();
						%>
						<div class="slide bx-clone">
							<div id="valutazione_<%= valutazione.getIdInserzione() %>" class="slider-item">
								<h6>
									<a href="#"></a><%= valutazione.getProdotto().getDescrizione() %>
								</h6>
								<div class="featured-image">
									<img class="featured-image-img"
										src="<%= valutazione.getFoto() %>" />
								</div>
								<div class="data-periodo">
									<div class="data-inizio">
										<i class="fa fa-clock-o"></i><%= valutazione.getDataInizio() %>
									</div>
									<div class="data-fine">
										<i class="fa fa-clock-o"></i><%= valutazione.getDataFine() %>
									</div>
								</div>
								<div class="supermercato-box">
									<i class="fa fa-map-marker"></i><%= supermercato %>
								</div>
								<div class="prezzo-box">
									<i class="fa fa-eur"></i><%= valutazione.getPrezzo() %>
								</div>
								<div class="valutazione-box">
									<button type="button" class="valutazione-negativa">-1</button>
									<button type="button" class="valutazione-positiva">+1</button>
								</div>
								<div class="etichetta-votato">VOTATO</div>
							</div>
						</div>
						<%
						}
						%>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			$(document).ready(function() {
				$('.slider2').bxSlider({
					slideWidth : 280,
					minSlides : 2,
					maxSlides : 2,
					moveSlides : 1,
					slideMargin : 5
				});
			});
		</script>
		
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		slider1 = $('.slider1').bxSlider({
			slideWidth : 280,
			minSlides : 2,
			maxSlides : 2,
			moveSlides : 1,
			slideMargin : 5
		});
		
		slider2 = $('.slider2').bxSlider({
			slideWidth : 280,
			minSlides : 2,
			maxSlides : 2,
			moveSlides : 1,
			slideMargin : 5
		});
		
		$(".slider-item >.valutazione-box >.valutazione-negativa").click(inviaValutazione);
		$(".slider-item >.valutazione-box >.valutazione-positiva").click(inviaValutazione);
	});
</script>


