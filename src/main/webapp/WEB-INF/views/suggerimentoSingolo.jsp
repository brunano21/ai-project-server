<%@ page import="hibernate.ListaDesideri" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Set" %>
<%@ page import="dati.Dati" %>
<%@ page import="hibernate.Inserzione" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<script type="text/javascript">
var lista_desideri_suggerimento_singolo_selezionata = null;

//calcola l'hashcode di una stringa
String.prototype.hashCode = function() {
    var hash = 0, i, chr, len;
    if (this.length == 0) return hash;
    for (i = 0, len = this.length; i < len; i++) {
        chr   = this.charCodeAt(i);
        hash  = ((hash << 5) - hash) + chr;
        hash |= 0; // Convert to 32bit integer
    }
    return hash;
};

function getHashCode() {
    return new Date().getTime().toString().hashCode();
};

var inviaValutazione_suggerimento = function() {
	console.log("inviaValutazione_suggerimento");
	
	console.log("Valutazione SUGGERIMENTO: " + $(this).text());
	$.ajax({type : "POST",
			url : window.location.pathname+"valutazione/riceviValutazione",
			data : {
				"idInserzione" : ($(this).parent().parent().attr("id")+"").split("_")[2],
				"valutazione" : $(this).text()
				},
			success : function(data) {
				if(data != "Error-gia-valutato") {
					$(".valutazione-box >.valutazione-negativa").unbind();
					$(".valutazione-box >.valutazione-positiva").unbind();
					
					if((data+"").split("_@_")[1] == "-1")
						$(".etichetta-votato").css("background-color", "#FF2F2F");
					
					$(".etichetta-votato").fadeIn();
				}
				$(".valutazione-box >.valutazione-negativa").css("background-color", "#A59F9F");
				$(".valutazione-box >.valutazione-positiva").css("background-color", "#A59F9F");
				
				setTimeout(function() {
					$("#overlayPanel").children().hide();
					$("#overlayPanel").hide();
				}, 2000);
			}
	});	
};

var invia_nuovo_elemento = function() {
	
	if(lista_desideri_suggerimento_singolo_selezionata == null)
		return;
	
    var jsonData = {
        'cmd' : 'nuovoElemento',
        'idListaDesideri' : (lista_desideri_suggerimento_singolo_selezionata+"").split("_")[2],
        'idElemento' : getHashCode(),
        'descrizione' : $("#suggerimento-singolo-box .slider-item > h6").text(),
        'quantita' : 1,
        'acquistato' : false,
        'latitudine' : "",
        'longitudine' : ""
    };
	
	$.ajax({
    	url: document.URL+"todolist",
        type: "post",
        data: jsonData,
        dataType: "html",
        success : function(data) {
        	var data = JSON.parse(data);
        	
        	setTimeout(function() {
				$("#overlayPanel").children().hide();
				$("#overlayPanel").hide();
			}, 2000);
        	
        	jsonData = {};
        	jsonData = {
                    'cmd' : 'aggiungiIDInserzione',
                    'idListaDesideri' : data['infoElemento']['ID_ListaDesideri'],
                    'idElemento' : data['infoElemento']['ID_Elemento'],
                    'idInserzione' : ($("#suggerimento-singolo-box .slider-item").attr("id")+"").split("_")[2]
                };
        	$.ajax({
            	url: document.URL+"todolist",
                type: "post",
                data: jsonData,
                dataType: "html"
        	});
		}
    });
    
};

$(document).ready( function() {
	$("#lista-desideri-selettore").selectmenu({ width: 163, style: "popup", change: function( event, ui ) {
		lista_desideri_suggerimento_singolo_selezionata = ui.item.value;
	}});
	
	$(".valutazione-box >.valutazione-negativa").click(inviaValutazione_suggerimento);
	$(".valutazione-box >.valutazione-positiva").click(inviaValutazione_suggerimento);
	$(".addNewItemBtn").click(invia_nuovo_elemento);
});
</script>


<div id="suggerimento-singolo-box">
		<%
		if(!"evaluation".equals(request.getParameter("tipoSuggerimento"))) {
		%>
			<h6>VUOI AGGIUNGERLO ALLA TUA LISTA?</h6>
		<%
		} else {
		%>
			<h6>VUOI VALUTARE IL SUGGERIMENTO?</h6>
			<span>Puoi guadagnare 2 crediti :)</span>
		<%
        }
        %>
		<%
		Inserzione inserzione = Dati.getInstance().getInserzioni().get(Integer.parseInt(request.getParameter("idInserzione")));
		String supermercato = inserzione.getSupermercato().getNome() + ", " + inserzione.getSupermercato().getIndirizzo() + ", " + inserzione.getSupermercato().getComune();
		String idContainer = ("evaluation".equals(request.getParameter("tipoSuggerimento")) == true) ? "suggerimento_valutazione_" + inserzione.getIdInserzione() : "suggerimento_scadenza_" + inserzione.getIdInserzione();
		%>
		<div id=<%= idContainer %> class="slider-item">
			<h6>
				<a href="#"></a><%= inserzione.getProdotto().getDescrizione() %>
			</h6>
			<div class="featured-image">
				<img class="featured-image-img"
					src="<%= inserzione.getFoto() %>" />
			</div>
			<div class="data-periodo">
				<div class="data-inizio">
					<i class="fa fa-clock-o"></i><%= inserzione.getDataInizio() %>
				</div>
				<div class="data-fine">
					<i class="fa fa-clock-o"></i><%= inserzione.getDataFine() %>
				</div>
			</div>
			<div class="supermercato-box">
				<i class="fa fa-map-marker"></i><%= supermercato %>
			</div>
			<div class="prezzo-box">
				<i class="fa fa-eur"></i><%= inserzione.getPrezzo() %>
			</div>
			
			<%
			if(!"evaluation".equals(request.getParameter("tipoSuggerimento"))) {
				// Se si tratta di qualcosa diverso dalla valutazione  aggiungo la lista per scegliere a quale lista dei desideri collegare l'elemento
			%>
			<select name="lista-desideri-selettore" id="lista-desideri-selettore">
				<option value="default-option" class="lista-desideri-option" selected="selected">- Seleziona -</option>
	          <%
	          Set listaDesiderisUtente = Dati.getInstance().getUtenti().get(request.getUserPrincipal().getName()).getListaDesideris();
	          ListaDesideri ld = null;
	          for(Iterator<ListaDesideri> lduIter = listaDesiderisUtente.iterator(); lduIter.hasNext();) {
	        	  ld = lduIter.next();
        	  %>
        	  	<option value=<%= "lista_desideri_" + ld.getIdListaDesideri() %> class="lista-desideri-option"><%= ld.getNomeListaDesideri() %></option>
        	  <%
	          }
	          %>
		   </select>
		   <div class='addNewItemBtn genericBtn'>Aggiungi</div>
			<%
			} else {
			%>
			<div class="valutazione-box">
				<button type="button" class="valutazione-negativa">-1</button>
				<button type="button" class="valutazione-positiva">+1</button>
			</div>
			<div class="etichetta-votato">VOTATO</div>
			<%
			}
			%>
		</div>
		
</div>



