<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="inserzione" class="inserzioneContainer" >
    <h2>INSERZIONE</h2>
    <form id="inserzioneForm" class="inserzioneForm">
        <div id="prodottoBox">
            <div class="col-3-5">
                <div class="descrizione">
                    <label for="descrizioneInput">Descrizione Prodotto</label>
                    <div class="input-group">
                        <span class="input-icon"><i class="fa fa-shopping-cart fa-fw"></i></span>
                        <input id="descrizioneInput"class="input-control" type="text" placeholder="Descrizione Prodotto">
                    </div>
                </div>
                
                <div class="codiceBarre">
                    <label for="codiceBarreInput">Codice a Barre</label>
                    <div class="input-group">
                        <span class="input-icon"><i class="fa fa-barcode fa-fw"></i></span>
                        <input id="codiceBarreInput"class="input-control" type="text" placeholder="Bar code">
                    </div>
                </div>
                <div class="categoria">
                    <label for="categoriaInput">Categoria</label>
                    <div>
                        <select id="categoriaInput">
							<c:forEach items="${categoria}" var="cat">
						        <option>${cat}</option>
							</c:forEach>	                            
                        </select>
                    </div>
                </div>    
                <div class="sottocategoria">    
                    <label for="sottocategoriaInput">Sottocategoria</label>
                    <div>
                        <select id="sottocategoriaInput">
                            <option value="1">Uno</option>
                            <option value="2">Due</option>
                            <option value="3">Tre</option>
                            <option value="4">Quattro</option>
                            <option value="5">Cinque</option>
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
                    <select id="descrizioneDettaglioInput">
                        <option value="volvo">prezzo/litro</option>
                        <option value="saab">prezzo/kg</option>
                        <option value="opel">prezzo/unità</option>
                        <option value="audi">prezzo</option>
                    </select>
                </div>
            </div>
            <div class="col-1-2 quantitadettaglio">
                <label for="quantitaDettaglioInput">Quantità</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-plus fa-fw"></i></span>
                    <input id="quantitaDettaglioInput" class="input-control" type="text" placeholder="Quantità">
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
                    <input id="dataInizioInput" class="input-control" type="text" placeholder="Inizio offerta">
                </div>
            </div>
            <div class="col-1-3 dataFine">
                <label for="dataFineInput">Data Fine</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-calendar fa-fw"></i></span>
                    <input id="dataFineInput" class="input-control" type="text" placeholder="Fine offerta">
                </div>
            </div>
            <div class="col-1-3 prezzo">
                <label for="prezzoInput">Prezzo</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-eur fa-fw"></i></span>
                    <input id="prezzoInput" class="input-control" type="text" placeholder="Prezzo">
                </div>
            </div>
        </div>

        <div class="clear"></div>
        <hr>
        
        <div id="supermercatoBox">
            <div class="col-1-4 supermercato">
                <label for="cittaInput">Supermercato</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-map-marker fa-fw"></i></span>
                    <input id="supermercatoInput" class="input-control" type="text" placeholder="Supermercato">
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
	var sottocategoriaInputSelect;
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
                    if(imgIndex < 64 && imgIndex <= imgResults.length)
                        $('#suggerimentoImgInput').attr("src", imgResults[imgIndex].tbUrl);   
                    else console.log(imgResults);
                });         
            
                    
            
            }
        
        }, 
        null);

        imageSearch.execute($("#descrizioneInput").val());

    };
    
</script>

