<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    
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
                            <option value="a">Uno</option>
                            <option value="b">Due</option>
                            <option value="c">Tre</option>
                            <option value="d">Quattro</option>
                            <option value="e">Cinque</option>
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
            <input class="inserisciInserzione"type="submit" value="Invia" />
            <input class="resetInserzione" type="reset" value="reset" />
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

	$("#overlayPanel").on('click', hideOverlayPanel);
	
	//Select.init({selector : "select#categoriaInput, #sottocategoriaInput, #descrizioneDettaglioInput"});
	Select.init(); 

	$('.fileinputs > input[type=file]').change(function(){
		$(this).next().find('input').val($(this).val().split('\\').slice(-1)[0]);
	});
</script>