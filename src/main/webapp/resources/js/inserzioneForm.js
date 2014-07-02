/**
 * 
 */

var risposta = "No";
var supermercati_markers = [];
var codici_prodotti = [];
console.log(new Date());
var fromDate = $("#dataInizioInput").datepicker({
	defaultDate: new Date(),
	changeMonth: true,
	numberOfMonths: 1,
	minDate: "-3w",
	maxDate: "+2m",
	onSelect: function(selectedDate) {
		var instance = $(this).data("datepicker");
		var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
		date.setDate(date.getDate()+5);
		toDate.datepicker("option", "minDate", date);
	}
});

var toDate = jQuery("#dataFineInput").datepicker({
	defaultDate: "+1w",
	changeMonth: true,
	numberOfMonths: 1,
	minDate: "-1w",
	maxDate: "+3m",
});

//Select.init({selector : "select#categoriaInput, #sottocategoriaInput, #descrizioneDettaglioInput"});
//Select.init(); 
var categoriaInputSelect = new Select({el: $('#categoriaInput')[0]});


$('#categoriaInput').change(function(){
	path = window.location.pathname + "inserzione/sottocategorie/" + categoriaInputSelect.value;
	$.ajax({
		type:"GET",
		url: path,
		contentType:"application/json"})
		.done(function( data ) {
			var select = $("#sottocategoriaInput");
			var options;
			if(select.prop){
				options = select.prop('options');
			}else{
				options = select.attr('options');
			}
			$('option',select).remove();

			$.each(data,function(val,text){
				options[options.length] = new Option(text, text);
			});
			sottocategoriaInputSelect = new Select({el: $('#sottocategoriaInput')[0]});
		});
});

$("#categoriaInput").trigger("change");
var sottocategoriaInputSelect = new Select({el: $('#sottocategoriaInput')[0]});


var descrizioneDettaglioInputSelect = new Select({el: $('#descrizioneDettaglioInput')[0]});

/* GESTIONE IMMAGINE QUANDO CARICATA DA LOCALE */
$('.fileinputs > input[type=file]').change(function(ev){
	var f = ev.target.files[0];
	var fr = new FileReader();
	var path = $(this).val();

	fr.onload = function(ev2) {
		$('#suggerimentoImgInput').attr("src", ev2.target.result);
	};

	fr.readAsDataURL(f);
	$(this).next().find('input').val(path.split('\\').slice(-1)[0]);

});

var imgIndex = 0;
var imgResults = [];
var typingTimer;
var descrizioneInterval = 2500;

$("#descrizioneInput").keyup(function(){
	// TODO gestire spinner -- avviarlo!

	typingTimer = setTimeout(searchImage, descrizioneInterval);
});

$('#descrizioneInput').keydown(function(){
	clearTimeout(typingTimer);
});

$("#descrizioneInput").autocomplete({
	source: function(request, response){
		var risp = [];
		$.ajax({type:"GET",
			url : window.location.pathname+"inserzione/getSuggerimenti/prodotti",
			contentType : "application/json",
			data : {"term": request.term},
			success : function(data){
				codici_prodotti=data;
			}				
		});
		$.each(codici_prodotti, function(index, value){
			$.each(value,function(index, value){
				risp.push(value);
			});
		});
		response(risp);	
	},
	select:function(event, ui){
		$.each(codici_prodotti,function(index,value){

			$.each(value,function(index,value){
				if(value == ui.item.label){
					$('#codiceBarreInput').val(index);
					return false;
				}
			});			

		});
	}
});

function searchImage(){
	// TODO - nascondere lo spinner

	var imageSearch = new google.search.ImageSearch();
	imageSearch.setRestriction(google.search.ImageSearch.RESTRICT_IMAGESIZE, google.search.ImageSearch.IMAGESIZE_LARGE, google.search.Search.SAFESEARCH_STRICT);


	imageSearch.setSearchCompleteCallback(this, function(){

		var searcher = imageSearch;
		if (searcher.results && searcher.results.length > 0) {
			imgResults = searcher.results;
			$('#suggerimentoImgInput').attr("src", searcher.results[imgIndex].tbUrl);
			//settare visibile il form per la selezione dell'immagine successiva
			$('.prossimaImg > a').unbind("click").click(function(){
				imgIndex++;
				if(imgIndex < 64 && imgIndex <= imgResults.length){
					$('#suggerimentoImgInput').attr("src", imgResults[imgIndex].tbUrl);   
				}else{ console.log(imgResults);
				$(this).html("Suggerimenti esauriti");
				}
			});            
		}
	}, 
	null);

	imageSearch.execute($("#descrizioneInput").val());

};
$("#supermercatoInput").autocomplete({
	source : window.location.pathname+"inserzione/getSuggerimenti/supermercati"
	/*,
    select : function(event, ui){
        var selected = ui.item.label;        
        $('#supermercatoInput').val(selected);
      //  $('#indirizzoInput').trigger("keyup");
        //event.preventDefault();
    }*/
});


var currentLocation;
var infowindow ;
var geocoder ;
var supermercato_scelto = false;


function loadMarkers(map,path,latLng){
	console.log("loadMarkers" + latLng);
	console.log(userPosition.coords);
	$.ajax({
		type:"GET",
		url: path+"/getSupermercati",
		contentType:"application/json",
		data:{"lat":userPosition.coords.latitude, "lng":userPosition.coords.longitude},
		success:function(data){
			console.log("data" + data);
			for(var i=0;i < supermercati_markers.length;i++){
				console.log(supermercati_markers);
				console.log(supermercati_markers.length);
				console.log("supermercati_markers");
				supermercati_markers[i].setMap(null);
			}
			$.each(data,function(index,value){
				console.log(index + " - " + value);
				var latLng = new google.maps.LatLng(value.lat,value.lng);
				var marker = new google.maps.Marker({
					map: map,
					position: latLng,
				});	

				google.maps.event.addListener(marker, 'click', function() {
					var strs = $('#nome.infowindow').text();
					$('#supermercatoInput').val(value.nome);
					$('#indirizzoInput').val(value.indirizzo);
					$('#comuneInput').val(value.comune);
					$('#provinciaInput').val(value.provincia);

					/*if(!supermercato_scelto){
						var string = "<a class ='infowindow' id='Si'>Si</a> o <a class ='infowindow' id='No'>No</a>";
						infowindow.setContent("<div class='infowindow 'id='nome'>"+value.nome+"</div>\n<div class='infowindow' id='domanda'> E' questo?\n"+string+"</div>");
						infowindow.open(map,this);
						$('a.infowindow').click(function(){
							if($(this).attr('id')=="Si"){
								var strs = $('#nome.infowindow').text();
								$('#supermercatoInput').val(value.nome);
								$('#indirizzoInput').val(value.indirizzo);
								$('#comuneInput').val(value.comune);
								$('#provinciaInput').val(value.provincia);
								$('#domanda.infowindow').empty();
								$('#nome.infowindow').after("risposta ricevuta");
							}else{
								$('#domanda.infowindow').empty();
								$('#nome.infowindow').after("risposta ricevuta");
							}
						}).css({'cursor':'pointer',
			    			'background-color':'skyblue',
			    			'color':'blue',
			    		});
						supermercato_scelto = true;
					}else{
						infowindow.setContent("<div class='infowindow 'id='nome'>"+value.nome+"</div>");
						infowindow.open(map,this);
					}*/
				});
				supermercati_markers.push(marker);
			});
		}				
	});

}
var mapInitialized = false;
var latLng = null;
function mapInitializer(){
	console.log("mapInitializer");
	console.log("mapInitialized" + mapInitialized);
	infowindow = new google.maps.InfoWindow();
	geocoder = new google.maps.Geocoder();    
	geocoder.geocode({'address':"Italia"},function(results,status){
		if(status == google.maps.GeocoderStatus.OK){
			currentLocation = results[0].geometry.location;
			map = new google.maps.Map(document.getElementById('map-canvas'),{
				center: currentLocation,
				zoom: 15,
			});
			google.maps.event.addListener(map, 'tilesloaded', function(){
				if(!mapInitialized){
					latLng = map.getCenter();
					var path = window.location.pathname;
					if( path[path.length - 1] == '/'){
						path = path.slice(0, path.length - 1);
					}
					path = path+"/inserzione";
					
					console.log(navigator.geolocation);
					if(navigator.geolocation){
						console.log("fdafsdgf"  + navigator.geolocation);
						if(!localStorage.getItem("lat")){
							console.log("fdafsdgf"  + localStorage.getItem("lng"));
							navigator.geolocation.getCurrentPosition(function(position){
								currentLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
								map.setCenter(currentLocation);
								localStorage.setItem("lat", position.coords.latitude);
								localStorage.setItem("lng", position.coords.longitude);
								latLng = map.getCenter();
								loadMarkers(map,path,latLng);									
							}, function(){
								alert('consenti di sapere la tua posizione se vuoi essere localizzato error: ');	
								loadMarkers(map,path,latLng);		
							},null);
						}else{
							currentLocation = new google.maps.LatLng(localStorage.getItem("lat"),localStorage.getItem("lng"));
							map.setCenter(currentLocation);
							loadMarkers(map,path,latLng);	
						}
					}						
					mapInitialized = true;
				}
			});

		}else{
			alert("geocoder non funziona");
		};
	});
};

(function loadScript() {
	if (window.google !== undefined && google.maps !== undefined) {
		delete google.maps;
		$('script').each(function () {
			if (this.src.indexOf('googleapis.com/maps') >= 0
					|| this.src.indexOf('maps.gstatic.com') >= 0
					|| this.src.indexOf('earthbuilder.googleapis.com') >= 0) {
				// console.log('removed', this.src);
				$(this).remove();
			}
		});
	}
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true&libraries=places&' +
	'callback=mapInitializer';
	document.body.appendChild(script);
})();

var typingTimer = undefined;
var doneTypingInterval = 5000;

$('#indirizzoInput').keyup(function(){
	if(typingTimer == undefined)
		typingTimer = setTimeout(cercaSupermercato, doneTypingInterval);
});

$('#indirizzoInput').keydown(function(){
	clearTimeout(typingTimer);
	typingTimer = undefined;
});

$('#comuneInput').keyup(function(){
	if(typingTimer == undefined)
		typingTimer = setTimeout(cercaSupermercato, doneTypingInterval);	
});

$('#comuneInput').keydown(function(){
	clearTimeout(typingTimer);
	typingTimer = undefined;
});

$('#provinciaInput').keyup(function(){
	if(typingTimer == undefined)
		typingTimer = setTimeout(cercaSupermercato, doneTypingInterval);
});

$('#provinciaInput').keydown(function(){
	clearTimeout(typingTimer);
	typingTimer = undefined;
});

var markers = [];
var map;
function cercaSupermercato(){
	var indirizzo = $("#indirizzoInput").val();
	var comune = $("#comuneInput").val();
	var provincia = $("#provinciaInput").val();
	if(indirizzo != "" && comune != "" && provincia != ""){
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode({'address':indirizzo+" "+comune+" "+provincia},function(results,status){
			if(status == google.maps.GeocoderStatus.OK){
				for(var i=0;i<markers.length;i++){
					markers[i].setMap(null);
				}
				var marker = new google.maps.Marker({
					map: map,
					position: results[0].geometry.location,
				});
				map.setCenter(marker.getPosition());
				infowindow.setContent($('#supermercatoInput').val()+" "+indirizzo);
				infowindow.open(map, marker);
				markers.push(marker);
			}else{
				alert("errore nell'indirizzo");
			}
		});
	}
}

//TODO ricerca del codice a barre una volta immesso

$("#inserzioneForm").submit(function (event) {
	event.preventDefault();
	$('#overlayPanel').show();
	$('#popupConfermaInserzioneContainer').children().hide();
	$('#popupConfermaInserzioneContainer').show();
	$('#confermaInserzioneBox').show();
});

$("#annullaInserzioneBtn").click(function() {
	$('#overlayPanel').children().hide();
	$('#overlayPanel').hide();
});

$("#inviaInserzioneBtn").unbind().click(function(event) {
	$('#confermaInserzioneBox').hide();
	$('#caricamentoDatiInCorsoBox').show();

	var vuoto = false;
//	var optional_inputs = ["quantitaDettaglioInput","dataFineInput",""];
	event.preventDefault();
//	$("#inserzioneForm > input").each(function(){
//	if($(this).attr("id").val() in optional_inputs)
//	if($(this).val() == "")
//	vuoto = true;
//	});		
	if(!vuoto){
		/*
		if(risposta=="No"){
			$('#suggerimentoImgInput').attr("src","");
		}
		 */
//		$("select.argomenti").prop('disabled',false);
//		$("input.argomenti").prop('disabled',false);
//		$('#supermercato').val($('#supermercato').val()+" - "+$('#indirizzo').val());
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode({'address':$("#indirizzoInput").val()+" "+$("#comuneInput").val()+" "+$("provinciaInput").val()},function(results,status){
			console.log("FOTO1 : " + $('#suggerimentoImgInput').attr("src"));
			if(status == google.maps.GeocoderStatus.OK) {
				console.log("FOTO2 : " + $('#suggerimentoImgInput').attr("src"));
				var form = new FormData();
				$.each($("form").serializeArray(),function(index,value){				
					form.append(value.name,value.value);
				});
				form.append("lat",results[0].geometry.location.lat());
				form.append("lng",results[0].geometry.location.lng());


				if($("#imgInput > input.file")[0].files[0] != undefined){
					console.log("FOTO3 : " + $("#imgInput > input.file")[0].files[0]);
					var extension = $("#imgInput > input.file")[0].files[0].name.split(".").pop();
					if(extension == "jpg" || extension == "png"){
						form.append("file", $("#imgInput > input.file")[0].files[0]);
					}else{
						alert("Tipo del file caricato non valido, sono validi soltanto i file png e jpg");
					}
				} else
					if($('#suggerimentoImgInput').attr("src") != "")
						form.append("foto",$('#suggerimentoImgInput').attr("src"));

				$.ajax({
					type: 'POST',
					url: window.location.pathname+"inserzione",
					dataType: 'text',
					contentType: false,
					processData: false,
					data: form,
					success: function(response) {
						var data = undefined;
						if(!(response == "SUCCESS")) {
							try{
								data = $.parseJSON(response);
							}catch(err){
								console.log("ERROR nel parsing del JSON: " + err);
							}
							$('#popupConfermaInserzioneContainer').children().hide();
							$('#caricamentoDatiConErrori').show();
							
							if(data.hasOwnProperty("exception")){
								$("#dialog").html(response.exception+"\n riprova ad inserire un'altra inserzione");
								$("#dialog").dialog({									
									height: 300,
									width: 500,
									modal: true,
									buttons: {
										OK: function() {
											$(this).dialog("close");
										}
									},
									close: function() {									
										}
								}
								);
							}else{
								if(data.hasOwnProperty("errors")){
									$.each(data.errors,function(index,value){
										var tooltipPosition = { my: 'center+20 bottom', at: 'center top-5' }; 
										$.each(value,function(i,v){
											$("#"+i+"Input").parent().children().css("border", "1px solid #E00000");
											$("#"+i+"Input").parent().children().first().css("border-right", "0");
											$("#"+i+"Input").prop("title", v);
											$("#"+i+"Input").tooltip({ position: tooltipPosition }).tooltip("open");

										});
									});
								}
							}
							$('#popupConfermaInserzioneContainer').children().hide();
							$('#caricamentoDatiConErrori').show();
							setTimeout(function() {
								$('#overlayPanel').children().hide();
								$('#overlayPanel').hide();
							}, 2500);
							
						} else {
							// inserzione aggiunta con successo
							$('#popupConfermaInserzioneContainer').children().hide();
							$("#caricamentoDatiConSuccesso").show();

							setTimeout(function() {
								$("#caricamentoDatiConSuccesso").hide();
								$('#overlayPanel').children().hide();
								$('#overlayPanel').hide();

								risposta = "No";
								i = 0;
								delete codici_prodotti; //= [];
								mapInitialized = false;
								prodotto_selected="";
								delete infowindow; //infowindow = new google.maps.InfoWindow();
								delete geocoder; //geocoder = new google.maps.Geocoder();
								delete supermercati_markers; // = [];
								doneTypingInterval = 5000;
								descrizioneInterval= 5000;
								delete markers;

								getInserzionePage();
							}, 2500);
							
							// Aggiorno visivamente i crediti pendenti
							$("#incrementoCrediti").text("+10").fadeIn();
							setTimeout(function() {
								var crediti_pendenti = Number($($("#logContainer > form > div > p")[1]).text().split(": ").pop());
								crediti_pendenti += 10;
								$("#incrementoCrediti").text("").fadeOut();
								$($("#logContainer > form > div > p")[1]).text("Crediti Pendenti: " + crediti_pendenti);
							}, 2000);
						}
					}
				});
			}else{
				alert("L'indirizzo immesso non è corretto");
			}
		});
	}else{
		alert("impossibile avviare il submit, il campo del dettaglio precedente e' vuoto");
	}

}); 
