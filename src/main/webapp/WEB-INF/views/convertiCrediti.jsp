<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<div id="converti-crediti-container">
	<div class="buono-spesa-list">
		<div id="buono01" class="buono-spesa-item">
			<div class="buono-spesa-img-box">
				<img src="http://targetexecutivesearch.com/sites/default/files/images/client_logo/carrefour.jpg?1327058179">
			</div>
			<div class="buono-spesa-post-box">
				<div class="buono-spesa-post-titolo">BUONO SCONTO 5 &#8364</div>
				<div class="buono-spesa-post-descrizione">
				Il buono &#0232 valido per i prodotti della categoria Alimentari e Prima infanzia.
				<br><br>
				Validit&#0225 15 giorni dall'emissione.
				</div>
				<div class="buono-spesa-post-costo"> Crediti necessari al riscatto: 50</div>
				<div class="addNewItemBtn genericBtn">Richiedi</div>
			</div>
		</div>
		<div id="buono02" class="buono-spesa-item">
			<div class="buono-spesa-img-box">
				<img src="http://www.butac.it/wp-content/uploads/2014/04/coop.jpg">
			</div>
			<div class="buono-spesa-post-box">
				<div class="buono-spesa-post-titolo">BUONO SCONTO 10 &#8364</div>
				<div class="buono-spesa-post-descrizione">
				Il buono &#0232 valido per i prodotti della categoria Tecnologia.
				<br><br>
				Validit&#0225 10 giorni dall'emissione.
				</div>
				<div class="buono-spesa-post-costo"> Crediti necessari al riscatto: 80</div>
				<div class="addNewItemBtn genericBtn">Richiedi</div>
			</div>
		</div>
		<div id="buono03" class="buono-spesa-item">
			<div class="buono-spesa-img-box">
				<img src="http://www.tiendeo.it/galeria/negocio/4021/auchan.jpg">
			</div>
			<div class="buono-spesa-post-box">
				<div class="buono-spesa-post-titolo">BUONO SCONTO 5%</div>
				<div class="buono-spesa-post-descrizione">
				Il buono &#0232 valido per tutti i prodotti.
				<br><br>
				Validit&#0225 7 giorni dall'emissione.
				</div>
				<div class="buono-spesa-post-costo"> Crediti necessari al riscatto: 100</div>
				<div class="addNewItemBtn genericBtn">Richiedi</div>
			</div>
		</div>
		<div id="buono04" class="buono-spesa-item">
			<div class="buono-spesa-img-box">
				<img src="http://www.ligurianotizie.it/wp-content/uploads/2013/11/esselunga-logo-300x203.jpg">
			</div>
			<div class="buono-spesa-post-box">
				<div class="buono-spesa-post-titolo">BUONO SCONTO 50 &#8364</div>
				<div class="buono-spesa-post-descrizione">
				Il buono &#0232 valido per tutti i prodotti dei punti vendita Ipermercato.
				<br><br>
				Validit&#0225 20 giorni dall'emissione.
				</div>
				<div class="buono-spesa-post-costo"> Crediti necessari al riscatto: 500</div>
				<div class="addNewItemBtn genericBtn">Richiedi</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
function richiediBuono() {
	var creditiDisponibili = $("#logoutForm > div > p:last-child").text().split(":")[1].substring(1);
	var costo = $(this).parent().find(".buono-spesa-post-costo").text().split(":")[1].substring(1);
	
	if(Number(creditiDisponibili) < Number(costo)) {
		alert("Crediti insufficienti");
		return;
	}
	
	$.ajax({
    	url:"./richiediBuono", 
        type: 'POST', 
        async: true,
        dataType: "html",
        data: {
        	"id_buono" : $(this).parent().parent().attr("id"),
			"valore_sconto" : $(this).parent().find(".buono-spesa-post-titolo").text(),
			"descrizione" : $(this).parent().find(".buono-spesa-post-descrizione").text(),
			"costo" : costo
        },
        success: function(returnedData) {
        	console.log(returnedData);
        	//$("#"+returnedData).fadeOut();
        }
	});
	
	var creditiResidui = Number(creditiDisponibili) - Number(costo);
	
	$("#logoutForm > div > p:last-child").text("Crediti Acquisiti: "+creditiResidui);
	
	//$("#"+$(this).parent().parent().attr("id")).fadeOut();
};

jQuery(document).ready(function($){
	$(".buono-spesa-item >.buono-spesa-post-box >.addNewItemBtn").click(richiediBuono);
});

</script>