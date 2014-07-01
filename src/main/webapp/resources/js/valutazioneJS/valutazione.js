var inviaValutazione = function() {
	console.log("Valutazione: " + $(this).text());
	$.ajax({type : "POST",
			url : window.location.pathname+"valutazione/riceviValutazione",
			data : {
				"idInserzione" : ($(this).parent().parent().attr("id")+"").split("_")[1],
				"valutazione" : $(this).text()
				},
			success : function(data) {
				console.log("Ricevuta valutazione risposta " + data);
				if(data.split("_@_")[0] != "Error-gia-valutato") {
					$("#valutazione_" + data.split("_@_")[0] + " >.valutazione-box >.valutazione-negativa").unbind();
					$("#valutazione_" + data.split("_@_")[0] + " >.valutazione-box >.valutazione-positiva").unbind();
					
					if(data.split("_@_")[1] == "-1")
						$("#valutazione_" + data.split("_@_")[0] + " >.etichetta-votato").css("background-color", "#FF2F2F");
					
					$("#valutazione_" + data.split("_@_")[0] + " >.etichetta-votato").fadeIn();
					$("#valutazione_" + data.split("_@_")[0] + " >.valutazione-box >.valutazione-negativa").css("background-color", "#A59F9F");
					$("#valutazione_" + data.split("_@_")[0] + " >.valutazione-box >.valutazione-positiva").css("background-color", "#A59F9F");
					
					$("#incrementoCrediti").text("+2").fadeIn();
					setTimeout(function() {
						var crediti_pendenti = Number($($("#logContainer > form > div > p")[1]).text().split(": ").pop());
						crediti_pendenti += 2;
						$("#incrementoCrediti").text("").fadeOut();
						$($("#logContainer > form > div > p")[1]).text("Crediti Pendenti: " + crediti_pendenti);
					}, 2000);
					
					return;
				}
				$("#valutazione_" + data.split("_@_")[1] + " >.valutazione-box >.valutazione-negativa").css("background-color", "#A59F9F");
				$("#valutazione_" + data.split("_@_")[1] + " >.valutazione-box >.valutazione-positiva").css("background-color", "#A59F9F");
			}
	});	
};