inserzioni = [];

function getInserzioni(){
	inserzioni = [];
	if(localStorage.getItem("lat")){
		$.ajax({type:"GET",
			url: window.location.pathname+"/getInserzioni",
			contentType:"application/json",
			data:{"lat":localStorage.getItem("lat"),"lng":localStorage.getItem("lng")},
			success:function(data){
								
				$.each(data,function(index,value){
					var entry = new Object();
					entry.id = value.id;
					entry.descrizione = value.descrizione;
					entry.numerovalutazioni = value.numerovalutazioni;
					entry.prezzo = value.prezzo;
					entry.totalevoti = value.totalevoti;
					entry.supermercato = value.supermercato;
					entry.argomenti = value.argomenti;
					inserzioni.push(entry);
				});	
				getInserzione();
			}
		});
	}else{
		if(navigator.geolocation){
			
			navigator.geolocation.getCurrentPosition(function(position){
				localStorage.setItem("lat", position.coords.latitude);
				localStorage.setItem("lng", position.coords.longitude);
				$.ajax({type:"GET",
					url: window.location.pathname+"/getInserzioni",
					contentType:"application/json",
					data:{"lat":localStorage.getItem("lat"),"lng":localStorage.getItem("lng")},
					success:function(data){
						$.each(data,function(index,value){
							var entry = new Object();
							entry.id = value.id;
							entry.descrizione = value.descrizione;
							entry.numerovalutazioni = value.numerovalutazioni;
							entry.prezzo = value.prezzo;
							entry.totalevoti = value.totalevoti;
							entry.supermercato = value.supermercato;
							entry.argomenti = value.argomenti;
							inserzioni.push(entry);
						});	
						getInserzione();
					}
				});
			}, function(){
				alert('consenti di sapere la tua posizione se vuoi essere localizzato error: ');
				$.ajax({type:"GET",
					url: window.location.pathname+"/getInserzioni",
					contentType:"application/json",
					data:{"lat":0,"lng":0},
					success:function(data){
						$.each(data,function(index,value){
							var entry = new Object();
							entry.id = value.id;
							entry.descrizione = value.descrizione;
							entry.numerovalutazioni = value.numerovalutazioni;
							entry.prezzo = value.prezzo;
							entry.totalevoti = value.totalevoti;
							entry.supermercato = value.supermercato;
							entry.argomenti = value.argomenti;
							inserzioni.push(entry);
						});	
						getInserzione();
					}
				});
			},null);
			
		}
	}
}

function getInserzione(){
	if(inserzioni.length > 0){
		var inserzione = inserzioni.pop();
		$("#descrizione_corpo").html(inserzione.descrizione);
		$("#numerovalutazione_corpo").html(inserzione.numerovalutazione);
		$("#prezzo_corpo").html(inserzione.prezzo);
		$("#totalevoti_corpo").html(inserzione.totalevoti);
		$("#supermercato_corpo").html(inserzione.supermercato);
		$(".argomenti").remove();
		$.each(inserzione.argomenti,function(index,value){
			$.each(value,function(index,value){
				$("#supermercato").parent().parent().after('<tr>'+
				'<td><div id="'+index+'" class="argomenti">'+index+'</div></td>'+
				'<td><div id="'+index+'_corpo" class="argomenti">'+value+'</div>'+	
			'</tr>');
			});
		});
		$("#picture").attr("src",window.location.pathname+"/pictures/"+inserzione.id);
		$("div").width(300);
		$("#pic").parent().attr("id",inserzione.id);
	}else{
		$("#descrizione_corpo").remove();
		$("#numerovalutazione_corpo").remove();
		$("#prezzo_corpo").remove();
		$("#totalevoti_corpo").remove();
		$("#supermercato_corpo").remove();
		$("#descrizione").remove();
		$("#numerovalutazione").remove();
		$("#prezzo").remove();
		$("#totalevoti").remove();
		$("#supermercato").remove();
		$(".argomenti").remove();
		$("#pic").html("nessuna inserzione disponibile");
	}
}

function initialize(){
	$("#pic").parent().before('<td><div id="progressBar"></div></td>');
	$("#progressBar").height("20").width("200").progressbar({
		value:false
	});
	getInserzioni();
	$("#progressBar").parent().remove();
	$("#corretta").click(function(){
		alert(window.location.pathname);
		$.ajax({type:"POST",
			url: window.location.pathname+"/riceviValutazione",
			contentType:"application/json",
			data:{"valutazione":"corretta","idInserzione":$("#pic").parent().attr("id")},
			success:function(data){	
				if(data == "ok"){
					getInserzione();
				}else{
					alert("errore nella richiesta");
				}				
			},
			error:function(){
				alert("errore nella richiesta");
			}
		});		
	});
	$("#errata").click(function(){
		$.ajax({type:"POST",
			url: window.location.pathname+"/riceviValutazione",
			contentType:"application/json",
			data:{"valutazione":"errata","idInserzione":$("#pic").parent().attr("id")},
			success:function(data){				
				if(data == "ok"){
					getInserzione();
				}else{
					alert("errore nella richiesta");
				}	
			},
			error:function(){
				alert("errore nella richiesta");
			}
		});	
	});
}

initialize();