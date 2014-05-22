/**
 * 
 */
inserzioni = [];

function initialize(){
	var length = inserzioni.length;

	if(navigator.geolocation){
		
		navigator.geolocation.getCurrentPosition(function(position){
			localStorage.setItem("lat", position.coords.latitude);
			localStorage.setItem("lng", position.coords.longitude);
			$.ajax({type:"GET",
				url: window.location.origin+"/valutazione/getInserzioni",
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
						entry.foto = value.foto;
						inserzioni.push(entry);
					});	
					getInserzione();
				}
			});
		}, function(){
			alert('consenti di sapere la tua posizione se vuoi essere localizzato error: ');
			$.ajax({type:"GET",
				url: window.location.origin+"/valutazione/getInserzioni",
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
		
	}else{
		alert("errore inizializzando geolocation")
	}
	for(i = 0; i < length;i++){		
		var inserzione = inserzioni.pop();
		$("<div></div>").attr("class","prodotto").attr("id",inserzione.id);
		
		$("#contenitoreProdotti").append
	}
}

function addProdotto(){
	
}

