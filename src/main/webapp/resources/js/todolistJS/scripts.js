/*
Command:
    - nuovaListaDesideri
    - modificaNomeListaDesideri
    - eliminaListaDesideri
    - newItem
    - modificaDescrizioneElemento
    - modificaQuantitaElemento
    - modificaFlagAcquistatoElemento
    - eliminaElemento
*/

/*
    todoMap['todoList1']['Nome'] === nome della todoList
    todoMap['todoList1']['ID_ListaDB'] === hash generato dal tempo
    todoMap['todoList1']['Elementi'] = {}; === dizionario contenente tutti gli elementi
    todoMap['todoList1']['ID_ProssimoElemento']; === numero di elementi presenti.

    todoMap['todoList1']['Elementi']['todoList1-item0'] = {
        'ID_Elemento': //hashcode()
        'testo': // descrizione
        'quantita': // numero
        'acquistato': //booleano
    };
*/

var todoMap = {};
var userPosition = null;

function prepareList() {

    // ritorna la dimensione di un oggetto.
    Object.size = function(obj) {
        var size = 0, key;
        for (key in obj)
            if (obj.hasOwnProperty(key))
            	size++;
        
        return size;
    };
    
    // calcola l'hashcode di una stringa
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

    // aggiunge una linea sul testo
    function StrikeThrough(index, $divId) {
        var text = $divId.text();
        if (index >= text.length)
            return false;
        var sToStrike = text.substr(0, index + 1);
        var sAfter = (index < (text.length - 1)) ? text.substr(index + 1, text.length - index) : "";
        $divId.html("<strike>" + sToStrike + "</strike>" + sAfter);
        setTimeout(function() {
            StrikeThrough(index + 1, $divId);
        }, 25);
    };
    
    /********************************************* PRIVATE FUNCTIONS ***********************************************/
    
    var _attachTooltipster = function(values) {
		$("#"+values['idElemento']+"> .todoElemName").text(values['descrizione']);
		$("#"+values['idElemento']+"> .todoElemName").css('color', 'blue');
		$("#"+values['idElemento']+"> .todoElemName").tooltipster({
			content: $("<div class=\"featured-image\">" +
                    "<img class=\"featured-image-img\" src=\"" + values['foto'] + "\">" +
                    "</div>" +
					"<h6><a href=\"#\">" + values['descrizione'] + "</a></h6>" +
                    "<div><i class=\"fa fa-map-marker\"></i>" + values['supermercato'] + "</div>" +
                    "<div><i class=\"fa fa-clock-o\"></i>" + values['dataFine'] + "</div>" +
                    "<div><i class=\"fa fa-eur\"></i>" + values['prezzo'] + "</div>"),
                    theme: 'tooltipster-light',
                    maxWidth: 300,
                    interactive: true
			});
	};
    
    /************************************************* CALLBACKS ***************************************************/
    
	// Callback per espandere o comprimere la todolist
	var collapse_expande = function(event) {
        if (this == event.target) {
            $(this).toggleClass('expanded');
            $(this).children().not('span').not('.todoListNameInput').toggle('drop');
        }
        return false;
    };
    
    // Callback richiamata quando si clicca un pulsante con classe connect-item.
    var connectItem = function() {
    	console.log($(this).html());
		sendToServer("aggiungiIDInserzione", ($(this).parents('.carousel-item-mini').attr("id")).split("_"));
	};
	
	// Callback chiamata quando si clicca sul nome della lista per editarlo, fa mostrare l'input settando il testo.
	var modificaNomeLista = function () {
        if($(this).parent().hasClass('expanded')) {
            $(this).hide();
            $(this).parent().find(".todoListNameInput").show().val($(this).text()).focus();
        }      
    };
    
    // Callback relativa alla modifica del nome lista, viene richiamata quando si perde il focus, dopo la modifica.
    var focusInputNomeLista = function() {
        $(this).hide();  
        $(this).parent().find(".todoListName").show().text($(this).val());

        if (todoMap[$(this).parent().attr('id')]['Nome'] != $(this).val()) {
	        todoMap[$(this).parent().attr('id')]['Nome'] = $(this).val();
	        localStorage.setItem('todoMap', JSON.stringify(todoMap));
	        
	        console.log("modificaNomeListaDesideri");
	        
	        // inviare richiesta al server per rinominare la lista
	        sendToServer('modificaNomeListaDesideri', [todoMap[$(this).parent().attr('id')]['Nome'], todoMap[$(this).parent().attr('id')]['ID_ListaDB']]);
        }
    };
    
    // Callback relativa all'eliminazione di una lista.
    var eliminaLista = function() {
        $(this).parent().fadeOut(function() { 
            var ID_ListaDB = todoMap[$(this).attr('id')]['ID_ListaDB']; 
            
            // Inviare richiesta al server di eliminazione della lista.
            sendToServer('eliminaListaDesideri', [ID_ListaDB]);
            
            delete todoMap[$(this).attr('id')];
            localStorage.setItem("todoMap", JSON.stringify(todoMap));
            $(this).next('.listSeparator').remove();
            $(this).remove();
        });
    };
    
    // Callback richiamata quando il pulsante di conferma acquisto viene cliccato.
    var acquistatoToggle = function () {
        var $item = $(this).parent().parent();
        var $itemId = $item.attr('id');
        if($item.hasClass('completed')) {
            $item.children('.todoElemName').text($item.children('.todoElemName').text());
            $item.children('.todoElemQuantity').text($item.children('.todoElemQuantity').text());
            todoMap[$item.parent().parent().attr('id')]['Elementi'][$itemId]['acquistato'] = false;
            $item.removeClass('completed');
        } else { 
            StrikeThrough(0, $item.children('.todoElemName'));
            StrikeThrough(0, $item.children('.todoElemQuantity'));
            todoMap[$item.parent().parent().attr('id')]['Elementi'][$itemId]['acquistato'] = true;
            $item.addClass('completed');
        }

        // Salvataggio di acquistato nella mappa
        localStorage.setItem('todoMap', JSON.stringify(todoMap));
        // Inviare una richiesta al server di 'completato'
        sendToServer('modificaFlagAcquistatoElemento', [todoMap[$item.parent().parent().attr('id')]['ID_ListaDB'], todoMap[$item.parent().parent().attr('id')]['Elementi'][$itemId]['ID_Elemento'], todoMap[$item.parent().parent().attr('id')]['Elementi'][$itemId]['acquistato']]);
    };
    
    // Callback relativa all'eliminazione di un elemento della lista.
    var eliminaElemento = function() {
        $(this).parent().parent().fadeOut(function() { 
        	var idElemento = todoMap[$(this).parent().parent().attr('id')]['Elementi'][$(this).attr('id')]["ID_Elemento"];
            console.log(idElemento);
            
            // Inviare richiesta al server di eliminazione dell'elemento.
            sendToServer('eliminaElemento', [todoMap[$(this).parent().parent().attr('id')]['ID_ListaDB'], idElemento]);
            
            delete todoMap[$(this).parent().parent().attr('id')]['Elementi'][$(this).attr('id')];
            localStorage.setItem("todoMap", JSON.stringify(todoMap));
            
            // Elimino tutti i suggerimenti per quell'elemento
            var stringToMatch = ($(this).parents('.singleTodoList').attr('id')).split("_")[1] + "_" + idElemento;
            var suggerimentiTiles = $('.carousel-item-mini');
            for (var i = 0; i < suggerimentiTiles.length; i++)
				if(($(suggerimentiTiles[i]).attr("id")).indexOf(stringToMatch) != -1)
					$(suggerimentiTiles[i]).fadeOut( "slow", function() { $(this).remove(); });
            
            $(this).remove();
        });
    };
    
    // Callback chiamata quando si clicca sul nome di un elemento della lista per editarlo, fa mostrare l'input settando il testo.
    var modificaNomeElemento = function() {
        $(this).hide();
        $(this).parent().find(".todoElemNameInput").show().val($(this).text()).focus();
    };
    
    // Callback relativa alla modifica del nome dell'elemento di una lista, viene richiamata quando si perde il focus, dopo la modifica.
    var focusInputNomeElemento = function() {
        $(this).hide();  
        $(this).parent().find(".todoElemName").show().text($(this).val());
        
        if (todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['testo'] != $(this).val()) {
	        todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['testo'] = $(this).val();
	        localStorage.setItem('todoMap', JSON.stringify(todoMap));
	
	        // inviare richiesta al server per modificare il nome dell'elemento
	        sendToServer('modificaDescrizioneElemento', [todoMap[$(this).parent().parent().parent().attr('id')]["ID_ListaDB"], todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['ID_Elemento'], $(this).val()]);
        }
    };
    
    // Callback chiamata quando si clicca sul nome di un elemento della lista per editarlo, fa mostrare l'input settando il testo.
    var modificaQuantitaElemento = function() {
        $(this).hide();
        $(this).parent().find(".todoElemQuantityInput").show().val($(this).text()).focus();
    };

    // Callback relativa alla modifica del nome dell'elemento di una lista, viene richiamata quando si perde il focus, dopo la modifica.
    var focusInputQuantitaElemento = function() {
        $(this).hide();  
        $(this).parent().find(".todoElemQuantity").show().text($(this).val());

        if (todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['quantita'] != $(this).val()) {
	        todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['quantita'] = $(this).val();
	        localStorage.setItem('todoMap', JSON.stringify(todoMap));
	
	        // inviare richiesta al server per modificare la quantità dell'elemento
	        sendToServer('modificaQuantitaElemento', [todoMap[$(this).parent().parent().parent().attr('id')]["ID_ListaDB"], todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['ID_Elemento'], $(this).val()]);
        }
    };
    
    // Callback richiamata quando viene aggiunto un nuovo elemento ad una lista dei desideri.
    var addNewItem = function() {
        if(($(this).siblings('.itemText').val() == "") || ($(this).siblings('.itemQuantity').val() == ""))
            return;
        
        var itemDBId = getHashCode();
        var todoListId = $(this).parent().parent().attr('id');
        var itemId = "item_"+itemDBId;
        var itemText = $(this).parent().children('.itemText').val();
        var itemQuantity = $(this).parent().children('.itemQuantity').val();
        
        todoMap[todoListId]['Elementi'][itemId] = {
                'ID_Elemento': itemDBId,
                'testo': itemText,
                'quantita': itemQuantity,
                'acquistato': false,
                'ID_Inserzione': null
        };
        
        // creo l'item e lo appendo alla UL di appartenenza.
        $("#" + todoListId + " > ul").append(
            "<li id='" + itemId + "' class='singleTodoItem'>" +
                "<span class='todoElemName'>" + itemText + "</span> - " +
                "<input type='text' class='todoElemNameInput' style='display:none' />" +
                "<span class='todoElemQuantity'>" + itemQuantity + "</span>" +
                "<input type='text' class='todoElemQuantityInput' style='display:none' />" +
                "<div class='itemControls'>" + 
                    "<div class='okImg'></div>" +
                    "<div class='delImg'></div>" +
                "</div>" + 
            "</li>"
        );

        // se l'elemento va oltre lo spazio a disposizione, quando aggiungo un nuovo elemento, il div si sposta verso basso in modo da mostrarlo
        //$("#expList").scrollTop($("#expList")[0].scrollHeight);

        // listeners per editare il testo dell'elemento
        $("#"+ itemId + "> .todoElemName").click(modificaNomeElemento);
        $("#"+ itemId + "> .todoElemNameInput").focusout(focusInputNomeElemento);

        // listeners per editare la quantità dell'elemento
        $("#"+ itemId + "> .todoElemQuantity").click(modificaQuantitaElemento);
        $("#"+ itemId + "> .todoElemQuantityInput").focusout(focusInputQuantitaElemento);

        // listener per indicare l'elemento come 'acquistato' o meno
        $("#"+ itemId + "> .itemControls > .okImg").click(acquistatoToggle);

        // listener per eliminare l'elemento
        $("#"+ itemId + "> .itemControls > .delImg").click(eliminaElemento);

        // mostro il nuovo item creato, con un'animazione.
        $("#"+itemId)
            .css('display', 'none')
            .fadeIn("drop");

        // svuoto il campo di input
        $(this).parent().children('.itemText').val("");
        $(this).parent().children('.itemQuantity').val("");

        //todoMap[todoListId]['ID_ProssimoElemento'] += 1;
        localStorage.setItem("todoMap", JSON.stringify(todoMap));

        // inviare richiesta al server per salvare il nuovo elemento creato
        sendToServer('nuovoElemento', [todoMap[todoListId]['ID_ListaDB'], todoMap[todoListId]['Elementi'][itemId]]);
    };
    
    // Callback richiamata quando viene aggiunta una nuova lista dei desideri.
    var addNewList = function(todoListName) {
    	console.log("Click new todo list " + todoListName);
    	// creazione della nuova lista nella todoMap
    	var todoListDBId;
    	var todoListId;
    	
    	if (todoListName == null) {
    		// Elemento nuovo
	        todoListDBId = getHashCode();
	    	todoListId = "list_"+todoListDBId;
	    	
	    	todoMap[todoListId] = {};
	        todoMap[todoListId]['Nome'] = "Nuova Lista";
	        todoMap[todoListId]['ID_ListaDB'] = todoListDBId;
	        todoMap[todoListId]['Elementi'] = {};
    	} else {
    		// Elemento dal localstorage
    		todoListDBId = todoListName.split("_")[1];
	    	todoListId = todoListName;
    	}
    		
        $('#expList').append(
            "<li id='" + todoListId + "' class='singleTodoList collapsed expanded'>" +
                "<span class='todoListName'>" + todoMap[todoListId]['Nome'] + "</span>" +
                "<input type='text' class='todoListNameInput' style='display:none' />" + 
                "<div class='delImg'></div>" +
                "<div class='newItemContainer input-group'>" +
                	"<span class='input-icon'><i class='fa fa-shopping-cart fa-fw'></i></span>" +
                    "<input class='itemText input-control' type='text'> " +
                    "<span class='input-icon' style='border-radius: 4px 0px 0px 4px;'><i class='fa fa-plus fa-fw'></i></span>" +
                    "<input class='itemQuantity input-control' type='text'> " + 
                    "<div class='addNewItemBtn genericBtn'>Aggiungi</div>" +
                "</div>" +  
                "<ul class='show-items'></ul>" +
            "</li>" +
            "<div class='listSeparator'></div>"
        );
        
        // se la lista delle todo list va oltre lo spazio a disposizione, quando aggiungo una nuova todolist, il div si sposta verso basso in modo da mostrare la todolist appena creata
        $("#expList").scrollTop($("#expList")[0].scrollHeight);

        // aggiungo il listener per aprire e chiudere la nuova todo list
        $("#" + todoListId).click(collapse_expande);
        // listeners per quando si clicca sul nome della lista in modo da editarlo
        $("#" + todoListId + " > .todoListName").click(modificaNomeLista);
        $("#" + todoListId + " > .todoListNameInput").focusout(focusInputNomeLista);
        // listener per eliminare l'intera lista
        $("#" + todoListId + " > .delImg").click(eliminaLista);
        // listener per l'aggiunta di un nuovo elemento
        $("#" + todoListId + " > .newItemContainer >.addNewItemBtn").on('click', addNewItem);
        
        if (todoListName == null) {
	        // salvataggio della todoMap e del currentListId nel localStorage
	        localStorage.setItem('todoMap', JSON.stringify(todoMap));
	        // inviare una richiesta al sever contenente le informazioni della nuova lista
	        sendToServer("nuovaListaDesideri", [todoMap[todoListId]['Nome'], todoMap[todoListId]['ID_ListaDB']]);
        }
        
	    // appendo gli elementi alla lista in questione
        if (todoListName != null)
	        for (var k in todoMap[todoListId]['Elementi']) {
	        	console.log(todoMap[todoListId]['Elementi']);
	        	console.log("\t\tElementi: " + k + "\t testo: " + todoMap[todoListId]['Elementi'][k]['testo']);
	            var acquistato = todoMap[todoListId]['Elementi'][k]['acquistato'];
	            
	            $("#"+todoListId + " > ul").append(
	                "<li id='" + k + "' class='singleTodoItem" + ((acquistato) ? " completed" : "")  + "' >" +
	                    "<span class='todoElemName'>" + ((acquistato) ? ("<strike>" + todoMap[todoListId]['Elementi'][k]['testo'] + "</strike>") : (todoMap[todoListId]['Elementi'][k]['testo'])) + "</span> - " +
	                    "<input type='text' class='todoElemNameInput' style='display:none' />" +
	                    "<span class='todoElemQuantity'>" + ((acquistato) ? ("<strike>" + todoMap[todoListId]['Elementi'][k]['quantita'] + "</strike>") : (todoMap[todoListId]['Elementi'][k]['quantita'])) + "</span>" +
	                    "<input type='text' class='todoElemQuantityInput' style='display:none' />" +
	                    "<div class='itemControls'>" + 
	                        "<div class='okImg'></div>" +
	                        "<div class='delImg'></div>" +
	                    "</div>" + 
	                "</li>"
	            );
	
	            // listeners per editare il testo dell'elemento
	            $("#"+ k + "> .todoElemName").click(modificaNomeElemento);
	            $("#"+ k + "> .todoElemNameInput").focusout(focusInputNomeElemento);
	            // listeners per editare la quantità dell'elemento
	            $("#"+ k + "> .todoElemQuantity").click(modificaQuantitaElemento);
	            $("#"+ k + "> .todoElemQuantityInput").focusout(focusInputQuantitaElemento);
	            // listener per indicare l'elemento come 'acquistato' o meno
	            $("#"+ k + "> .itemControls > .okImg").click(acquistatoToggle);
	            // listener per eliminare l'elemento
	            $("#"+ k + "> .itemControls > .delImg").click(eliminaElemento);
	            console.log(todoMap[todoListId]['Elementi'][k]['ID_Inserzione']);
	            
	            // check se è stata associata un'inserzione
	            if(todoMap[todoListId]['Elementi'][k]['ID_Inserzione'] != null) {
	            	var data = {"idListaDesideri" : todoListId,
	            				"idElemento" : k,
	            				"idInserzione" : todoMap[todoListId]['Elementi'][k]['ID_Inserzione']};
	            	sendToServer("recuperaInserzione", data);
	            }
	        }
    };
    
    
    
	
    /*********************************************** MESSAGE ROUTER ************************************************/
    
    var sendToServer = function(command, data) {
        jsonData = {};

        switch(command) {
            case "nuovaListaDesideri":
                jsonData = {
                    'cmd' : "nuovaListaDesideri",
                    'nomeListaDesideri' : data[0],
                    'idListaDesideri' : data[1]
                };
                break;

            case "modificaNomeListaDesideri":
                jsonData = {
                    'cmd' : 'modificaNomeListaDesideri',
                    'nuovoNomeListaDesideri' : data[0],
                    'idListaDesideri' : data[1]
                };
                break;

            case "eliminaListaDesideri":
                jsonData = {
                    'cmd' : 'eliminaListaDesideri',
                    'idListaDesideri' : data[0]
                };
                break;

            case "nuovoElemento":
                jsonData = {
                    'cmd' : 'nuovoElemento',
                    'idListaDesideri' : data[0],
                    'idElemento' : data[1]['ID_Elemento'],
                    'descrizione' : data[1]['testo'],
                    'quantita' : data[1]['quantita'],
                    'acquistato' : false,
                    'latitudine' : userPosition.coords.latitude,
                    'longitudine' : userPosition.coords.longitude
                };
                break;

            case "modificaDescrizioneElemento":
                jsonData = {
                    'cmd' : 'modificaDescrizioneElemento',
                    'idListaDesideri' : data[0],
                    'idElemento' : data[1],
                    'descrizione' : data[2]
                };
                break;

            case "modificaQuantitaElemento":
                jsonData = {
                    'cmd' : 'modificaQuantitaElemento',
                    'idListaDesideri' : data[0],
                    'idElemento' : data[1],
                    'quantita' : data[2]
                };
                break;

            case "modificaFlagAcquistatoElemento":
                jsonData = {
                    'cmd' : 'modificaFlagAcquistatoElemento',
                    'idListaDesideri' : data[0],
                    'idElemento' : data[1],
                    'acquistato' : data[2]
                };
                break;
            
            case "aggiungiIDInserzione":
                jsonData = {
                    'cmd' : 'aggiungiIDInserzione',
                    'idListaDesideri' : data[1],
                    'idElemento' : data[2],
                    'idInserzione' : data[3]
                };
                break;
                
            case "eliminaElemento":
                jsonData = {
                    'cmd' : 'eliminaElemento',
                    'idListaDesideri' : data[0],
                    'idElemento' : data[1]
                };
                break;
                
            case "recuperaInserzione":
            	jsonData = {
                    'cmd' : 'recuperaInserzione',
                    'idListaDesideri' : data['idListaDesideri'],
                    'idElemento' : data['idElemento'],
                    'idInserzione' : data['idInserzione']
                };
            	break;

            default:
                console.log("Comando sconosciuto: " + command);
            	break;
        }

        var request;
        request = $.ajax({
        	url: document.URL+"todolist",
            type: "post",
            data: jsonData,
            dataType: "html"
        });
        
        request.done(function (response, textStatus, jqXHR) {
        	var jsonData = JSON.parse(response);
        	switch (jsonData['cmd']) {
        	
				case "nuovoElemento_Response":	    		
		    		for (var suggerimento in jsonData['suggerimenti']) {
						var idElementoSuggerito = "elem_" + jsonData['infoElemento']['ID_ListaDesideri'] + "_" + jsonData['infoElemento']['ID_Elemento'] + "_" + jsonData['suggerimenti'][suggerimento]['ID_Inserzione'];
			    		$('.carousel-list-mini').append(
				    			"<div id=\"" + idElementoSuggerito + "\" class=\"carousel-item-mini\" style=\"display:none\">" +
				                    
				                    "<div class=\"flip-container featured-image\" ontouchstart=\"this.classList.toggle('hover');\">" +
						    			"<div class=\"flipper\">" +
						    			  "<div class=\"front\">" +
						    			    "<img src=\"" + jsonData['suggerimenti'][suggerimento]['foto'] + "\">" +
						    			  "</div>" +
						    			  "<div class=\"back\">" +
						    			    "<div class=\"\">Clicca per collegare</div>" +
						    			  "</div>" +
						    			"</div>" +
					    			"</div>" +
				                    
				                    "<div class=\"post-margin\">" +
				                        "<h6><a href=\"#\">" + jsonData['suggerimenti'][suggerimento]['descrizione'] + "</a></h6>" +
				                        "<div><i class=\"fa fa-map-marker\"></i>" + jsonData['suggerimenti'][suggerimento]['supermercato'] + "</div>" +
				                        "<div><i class=\"fa fa-clock-o\"></i>" + jsonData['suggerimenti'][suggerimento]['dataFine'] + "</div>" +
				                        "<div><i class=\"fa fa-eur\"></i>" + jsonData['suggerimenti'][suggerimento]['prezzo'] + "</div>" +
				                    "</div>" +
				                "</div>"				                
				    		);
			    		
			    		//TODO Il back risulta come non cliccabile, anche se il listener viene attaccato correttamente, tuttavia non si riesce a cliccare
			    		//Io penso sia per il fatto che quando si gira non risulta visibile, la cosa strana è che prima mi funzionava, BAH, indagherò.
			    		//$("#"+idElementoSuggerito).find('.back').click(connectItem);
			    		//$("#"+idElementoSuggerito).find('.front').click(connectItem);
			    		$("#"+idElementoSuggerito).find('.flip-container').click(connectItem);
			    		$("#"+idElementoSuggerito).show("slow");
		    		}
					break;
					
				case "aggiungiIDInserzione_Response":
					var suggerimentiTiles = $('.carousel-item-mini');
					var idElementoSuggerito = "elem_"+jsonData['idListaDesideri']+"_"+jsonData['idElemento']+"_"+jsonData['idInserzione'];
					var values = {
							'idElemento': "item_"+jsonData['idElemento'],
							'descrizione': $("#"+idElementoSuggerito+"> .post-margin > h6").text(),
							'foto': $("#"+idElementoSuggerito+"> .featured-image > .flipper > .front > img").attr('src'),
							'supermercato': $($("#"+idElementoSuggerito+"> .post-margin > div")[0]).text(),
							'dataFine': $($("#"+idElementoSuggerito+"> .post-margin > div")[1]).text(),
							'prezzo': $($("#"+idElementoSuggerito+"> .post-margin > div")[2]).text()
							};
					_attachTooltipster(values);
					for (var i = 0; i < suggerimentiTiles.length; i++)
						if(($(suggerimentiTiles[i]).attr("id")).indexOf(jsonData['idListaDesideri']+"_"+jsonData['idElemento']) != -1)
							$(suggerimentiTiles[i]).fadeOut( "slow", function() { $(this).remove(); });
					break;
					
				case "recuperaInserzione_Response":
					_attachTooltipster(jsonData);
					break;
	
				default:
		            console.log("Hooray, it worked! But, I don't understand the command code! o.O");
					break;
			}
        });
        
        request.fail(function (jqXHR, textStatus, errorThrown){
            // log the error to the console
            console.error("The following error occured: " +  textStatus + " - " + errorThrown);
        }); 
        
    };
    
    
    /*********************************************** INIT FUNCTION ************************************************/

    /* Start Carousel */		
    /* 
    $('#carousel-list').carouFredSel({
        //auto                : true,
        //circular: true,
        items : {
            visible : 3,
            width : 217, 
            height : 234 
        },
        direction           : "up",
        //height: 740,
        scroll : {
            items           : 1,
            easing          : "scroll",
            duration        : 1000,                         
            pauseOnHover    : true
        }                   
    });
    */
    
    /*
    $('#carousel-list').carouFredSel({
        //auto                : true,
        //circular: true,
        items               : 3,
        direction           : "up",
        //height: 740,
        scroll : {
            items           : 1,
            easing          : "elastic",
            duration        : 1000,                         
            pauseOnHover    : true
        }                   
    });
    */
	/* End Carousel */
	
	/* Start Orbit Slider */
	/*
    $(window).load(function() {
		$('.post-gallery').orbit({
            animation: 'fade',
        });
    });
    */
    /* End Orbit Slider */
    
    
    // Carica la/le todo list dal local storage
    var loadFromLocalStorage = function() {
        if (localStorage.getItem("todoMap") !== null) {
            todoMap = JSON.parse(localStorage.getItem('todoMap'));
            console.log(todoMap);
            
            for (var index in todoMap) { // index == todoList0, todoList1, todoList2, ... 
                // append the current todo list.
            	console.log("Index: " + index + "\t Nome: " + todoMap[index]['Nome']);
            	addNewList(index);
            }
        }
    }; // end loadFromLocalStorage
    
    $('#expList > li').
        click(collapse_expande).
        addClass('collapsed').
        children().not('span').not('.todoListNameInput').hide();

    // Create the button funtionality
    $('#expandList')
        .unbind('click')
        .click( function() {
            $('.collapsed').addClass('expanded');
            $('.collapsed').children().not('span').not('.todoListNameInput').show('medium');
    });
    
    $('#collapseList')
        .unbind('click')
        .click( function() {
            $('.collapsed').removeClass('expanded');
            $('.collapsed').children().not('span').not('.todoListNameInput').hide('medium');
    });
    
    // aggiunta di una nuova TodoList
    $('#newTodoListBtn').on('click', function() { addNewList(null); });
    // aggiunta nuovo elemento alla TodoList
    $(".addNewItemBtn").on('click', addNewItem);
    
    $("#listContainer").show();
    (function init() { loadFromLocalStorage(); })();
    
}; //end prepareList


function getUserGeoloc() {
	console.log("getUserGeoloc called");
	
	navigator.geolocation.getCurrentPosition(
		    gotPosition,
		    errorGettingPosition,
		    {'enableHighAccuracy':true,'timeout':10000,'maximumAge':0}
		);
};

function gotPosition(pos) {
	userPosition = pos;
	console.log( "latitude:"+ pos.coords.latitude +"\n"+ "longitude:"+ pos.coords.longitude);
	/*
	var jsonData = {
            'latitudine' : pos.coords.latitude,
            'longitudine' : pos.coords.longitude
    };

	$.ajax({type:"POST",
		url: window.location.pathname+"todolist/userGeoloc",
		data: jsonData,
		dataType: "html",
		success:function(data){
			console.log(data);
		}
	});
	*/
	
	/* ESEMPIO VALORI
    var outputStr =
        "latitude:"+ pos.coords.latitude +"\n"+
        "longitude:"+ pos.coords.longitude +"\n"+
        "accuracy:"+ pos.coords.accuracy +"\n"+

        "altitude:"+ pos.coords.altitude +"\n"+
        "altitudeAccuracy:"+ pos.coords.altitudeAccuracy +"\n"+
        "heading:"+ pos.coords.heading +"\n"+
        "speed:"+ pos.coords.speed +"";
    */
};

function errorGettingPosition(err) {
	switch (err.code) {
		case 1:
			console.log("L'utente non ha autorizzato la geolocalizzazione");
			break;
		case 2:
			console.log("Posizione non disponibile");
			break;
		case 3:
			console.log("Timeout");
			break;
		default:
			console.log("ERRORE:" + err.message);
			break;
	}
};

/*********************************************** SUB INIT - MAIN ************************************************/

/**************************************************************/
/* Functions to execute on loading the document               */
/**************************************************************/
$(document).ready( function() {
	
	$.ajax({type:"GET",
		url: window.location.pathname+"todolist/getTodoList",
		contentType:"application/json",
		success:function(data){
			console.log(data);
			console.log(JSON.stringify(data));
			localStorage.setItem("todoMap", JSON.stringify(data));
			prepareList();
			getUserGeoloc();
		}
	});
	
});

