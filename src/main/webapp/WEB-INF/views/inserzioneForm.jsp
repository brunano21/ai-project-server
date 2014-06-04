<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="https://www.google.com/jsapi" type="text/javascript"></script>
<script type="text/javascript">    
    google.load('search', '1');
    </script>

<div id="inserzione" class="inserzioneContainer" >
    <h2>INSERZIONE</h2>
    <form id="inserzioneForm" class="inserzioneForm" name="inserzioneForm" enctype="multipart/form-data">
        <div id="prodottoBox">
            <div class="col-3-5">
                <div class="descrizione">
                    <label for="descrizioneInput">Descrizione Prodotto</label>
                    <div class="input-group">
                        <span class="input-icon"><i class="fa fa-shopping-cart fa-fw"></i></span>
                        <input id="descrizioneInput"class="input-control" name="descrizione" type="text" placeholder="Descrizione Prodotto">
                    </div>
                </div>
                
                <div class="codiceBarre">
                    <label for="codiceBarreInput">Codice a Barre</label>
                    <div class="input-group">
                        <span class="input-icon"><i class="fa fa-barcode fa-fw"></i></span>
                        <input id="codiceBarreInput"class="input-control" name="codiceBarre" type="text" placeholder="Bar code">
                    </div>
                </div>
                <div class="categoria">
                    <label for="categoriaInput">Categoria</label>
                    <div>
                        <select id="categoriaInput" name="categoria">
							<c:forEach items="${categoria}" var="cat">
						        <option>${cat}</option>
							</c:forEach>	                            
                        </select>
                    </div>
                </div>    
                <div class="sottocategoria">    
                    <label for="sottocategoriaInput">Sottocategoria</label>
                    <div>
                        <select id="sottocategoriaInput" name="sottoCategoria">
                            <option value="Scegli">Seleziona la categoria</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="col-2-5 inserzioneImgBox" >
                <div class="suggerimentoImg">
                    <img id="suggerimentoImgInput"></img>
                </div>
                <div class="prossimaImg">
                    Immagine trovata? <a>Successiva</a>
                </div>
                <div class="caricaImg">
                    
                    <label for="imgInput">No? Caricala tu!</label>
                    <div id="imgInput" class="fileinputs">
                        <input type="file" class="file" />
                        <div class="fakefile">
                            <span><i class="uploadIcon fa fa-upload"></i></span>
                            <input disabled/>
                        </div>
                </div>
                    
                </div>
            </div>
        </div>

        <div class="clear"></div>
        <hr>
        <div id="dettaglioBox">
            <div class="col-1-2 descrizionedettaglio">
                <label for="descrizioneDettaglioInput">Dettaglio</label>
                <div>
                    <select id="descrizioneDettaglioInput" name="argomento[0]" class="argomenti">                    
                   		<c:forEach items="${argomenti}" var="arg">
					        <option>${arg}</option>
						</c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-1-2 quantitadettaglio">
                <label for="quantitaDettaglioInput">Quantit�</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-plus fa-fw"></i></span>
                    <input id="quantitaDettaglioInput" name="arg_corpo[0]" class="input-control" type="text" placeholder="Quantit�">
                </div>
            </div>
        </div>

        <div class="clear"></div>
        <hr>
        
        <div id="offertaBox">
            <div class="col-1-3 dataInizio">
                <label for="dataInizioInput">Data Inizio</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-calendar fa-fw"></i></span>
                    <input id="dataInizioInput" name="dataInizio" class="input-control" type="text" placeholder="Inizio offerta">
                </div>
            </div>
            <div class="col-1-3 dataFine">
                <label for="dataFineInput">Data Fine</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-calendar fa-fw"></i></span>
                    <input id="dataFineInput" name="dataFine" class="input-control" type="text" placeholder="Fine offerta">
                </div>
            </div>
            <div class="col-1-3 prezzo">
                <label for="prezzoInput">Prezzo</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-eur fa-fw"></i></span>
                    <input id="prezzoInput" name="prezzo" class="input-control" type="text" placeholder="Prezzo">
                </div>
            </div>
        </div>

        <div class="clear"></div>
        <hr>
        
        <div id="supermercatoBox">
            <div class="col-1-4 supermercato">
                <label for="supermercatoInput">Supermercato</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-map-marker fa-fw"></i></span>
                    <input id="supermercatoInput" name="supermercato" class="input-control" type="text" placeholder="Supermercato">
                </div>
                <label for="indirizzoInput">Indirizzo</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-map-marker fa-fw"></i></span>
                    <input id="indirizzoInput" name="indirizzo" class="input-control" type="text" placeholder="Indirizzo">
                </div>
            </div>
            <div class="col-3-4" id="map-canvas"></div>
        </div>

        <div class="clear"></div>
        <hr>

        <div id="controlliBox">                  
            <input class="genericBtn inviaInserzione" type="submit" value="Invia" />
            <input class="genericBtn resetInserzione" type="reset" value="reset" />
        </div>

        <div class="clear"></div>
    </form>
</div>

<script type="text/javascript">
	var supermercati_markers = [];
    var fromDate = $("#dataInizioInput").datepicker($.datepicker.regional[ "it" ], {
        defaultDate: "+1w",
        changeMonth: true,
        numberOfMonths: 1,
        minDate: new Date(),
        onSelect: function(selectedDate) {
            var instance = $(this).data("datepicker");
            var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
            date.setDate(date.getDate()+5);
            toDate.datepicker("option", "minDate", date);
        }
    });
    
    var toDate = jQuery("#dataFineInput").datepicker($.datepicker.regional[ "it" ], {
        defaultDate: "+1w",
        changeMonth: true,
        numberOfMonths: 1
    });

    //Select.init({selector : "select#categoriaInput, #sottocategoriaInput, #descrizioneDettaglioInput"});
    //Select.init(); 
	var sottocategoriaInputSelect = new Select({el: $('#sottocategoriaInput')[0]});
    var categoriaInputSelect = new Select({el: $('#categoriaInput')[0]});

    $('#categoriaInput').change(function(){
    	path = window.location.pathname + "inserzione/sottocategorie/" + categoriaInputSelect.value;
		console.log(path);
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
        source : window.location.pathname+"inserzione/getSuggerimenti/supermercati",
        select : function(event, ui){
            var selected = ui.item.label;
            var strs = selected.split(/\s-\s/);
            $('#supermercatoInput').val(strs[0]);
            $('#indirizzoInput').val(strs[1]);
            //TODO to check!!
            $('#indirizzoInput').trigger("keyup");
            //event.preventDefault();
        }
    });


    var currentLocation;
    var infowindow ;
    var geocoder ;

    function loadMarkers(map,path,latLng){
    	$.ajax({type:"GET",
			url: path+"/getSupermercati",
			contentType:"application/json",
			data:{"lat":latLng.lat(),"lng":latLng.lng()},
			success:function(data){
				for(var i=0;i<supermercati_markers.length;i++){
					supermercati_markers[i].setMap(null);
				}
				$.each(data,function(index,value){
					var latLng = new google.maps.LatLng(value.lat,value.lng);
					var marker = new google.maps.Marker({
					    map: map,
					    position: latLng,
					});	
					
					google.maps.event.addListener(marker, 'click', function() {
						var string = "<a class ='infowindow' id='Si'>Si</a> o <a class ='infowindow' id='No'>No</a>";
						infowindow.setContent("<div class='infowindow 'id='nome'>"+value.nome+"</div>\n<div class='infowindow' id='domanda'> E' questo?\n"+string+"</div>");
						infowindow.open(map,this);
						$('a.infowindow').click(function(){
							if($(this).attr('id')=="Si"){
								var strs = $('#nome.infowindow').text().split(/\s-\s/);
								$('#supermercato').val(strs[0]);
								$('#indirizzo').val(strs[1]);
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
					});
					supermercati_markers.push(marker);
				});
			}				
		});

	}
    var mapInitialized = false;
    var latLng = null;
    function mapInitializer(){
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
						//TODO sistemare qu�
						if( path[path.length - 1] == '/'){
							path = path.slice(0, path.length - 1);
						}
						path = path+"/inserzione";
						if(navigator.geolocation){
							if(!localStorage.getItem("lat")){
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
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true&libraries=places&' +
		    'callback=mapInitializer';
		document.body.appendChild(script);
   	})();
    var typingTimer;
    var doneTypingInterval = 5000;

    $('#indirizzoInput').keyup(function(){
		typingTimer = setTimeout(cercaSupermercato(), doneTypingInterval);
	});

	$('#indirizzoInput').keydown(function(){
		clearTimeout(typingTimer);
	});
   	
	
    var markers = [];
	var map;
    function cercaSupermercato(){
    	var geocoder = new google.maps.Geocoder();
    	geocoder.geocode({'address':$("#indirizzoInput").val()},function(results,status){
    		if(status == google.maps.GeocoderStatus.OK){
    			for(var i=0;i<markers.length;i++){
    				markers[i].setMap(null);
    			}
    			var marker = new google.maps.Marker({
    			    map: map,
    			    position: results[0].geometry.location,
    			  });
    			map.setCenter(marker.getPosition());
    			infowindow.setContent($('#supermercatoInput').val()+" "+$('#indirizzoInput').val());
    		    infowindow.open(map, marker);
    			markers.push(marker);
    		}else{
    			alert("errore nell'indirizzo");
    		}
    	});
    }

</script>