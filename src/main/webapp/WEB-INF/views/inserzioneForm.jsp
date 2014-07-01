<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div id="inserzione" class="inserzioneContainer" >
    <h2>INSERZIONE</h2>
    <div id="dialog">    	
    </div>
    <form id="inserzioneForm" class="inserzioneForm" name="inserzioneForm" enctype="multipart/form-data">
        <div id="prodottoBox">
            <div class="col-3-5">
                <div class="descrizione">
                    <label for="descrizioneInput">Descrizione Prodotto</label>
                    <div class="input-group">
                        <span class="input-icon"><i class="fa fa-shopping-cart fa-fw"></i></span>
                        <input id="descrizioneInput"class="input-control" name="descrizione" type="text" placeholder="Descrizione Prodotto" required>
                    </div>
                </div>
                
                <div class="codiceBarre">
                    <label for="codiceBarreInput">Codice a Barre</label>
                    <div class="input-group">
                        <span class="input-icon"><i class="fa fa-barcode fa-fw"></i></span>
                        <input id="codiceBarreInput"class="input-control" name="codiceBarre" type="text" placeholder="Bar code" required>
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
                    <img id="suggerimentoImgInput">
                </div>
                <div class="prossimaImg">
                    <span class="col-full">Immagine trovata? </span><a id="successiv" class="col-1-2">Successiva</a>
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
                <label for="quantitaDettaglioInput">Quantità</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-plus fa-fw"></i></span>
                    <input id="quantitaDettaglioInput" name="arg_corpo[0]" class="input-control" type="text" placeholder="Quantità">
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
                    <input id="dataInizioInput" name="dataInizio" class="input-control" type="text" placeholder="Inizio offerta" required>
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
                    <input id="prezzoInput" name="prezzo" class="input-control" type="text" placeholder="Prezzo" required>
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
                    <input id="supermercatoInput" name="supermercato" class="input-control" type="text" placeholder="Supermercato" required>
                </div>
                <label for="indirizzoInput">Indirizzo</label>
                <div class="input-group">
                    <span class="input-icon"><i class="fa fa-map-marker fa-fw"></i></span>
                    <input id="indirizzoInput" name="indirizzo" class="input-control" type="text" placeholder="Indirizzo" required>
                </div>
                <label for="comuneInput">Comune</label>
                <div class="input-group">
                	<span class="input-icon"><i class="fa fa-map-marker fa-fw"></i></span>
                	<input id="comuneInput" name="comune" class="input-control" type="text" placeholder="Comune" required>
                </div>
				<label for="provinciaInput">Provincia</label>
				<div class="input-group">
					<span class="input-icon"><i class="fa fa-map-marker fa-fw"></i></span>
					<input id="provinciaInput" name="provincia" class="input-control" type="text" placeholder="Provincia" required>
				</div>
            </div>
            <div class="col-3-4" id="map-canvas"></div>
        </div>

        <div class="clear"></div>
        <hr>

        <div id="controlliBox">                  
            <input class="genericBtn inviaInserzione" type="submit" value="Invia" />
            <input class="genericBtn resetInserzione" type="reset" value="Resetta" />
        </div>

        <div class="clear"></div>
    </form>
</div>

<script type="text/javascript" src="resources/js/inserzioneForm.js"></script>