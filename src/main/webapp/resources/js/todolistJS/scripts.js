/*
Command:
    - newTodoList
    - editListName
    - deleteList
    - newItem
    - editItemName
    - editItemQuantity
    - editItemBoughtFlag
    - deleteItem
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
function prepareList() {

    // ritorna la dimensione di un oggetto.
    Object.size = function(obj) {
        var size = 0, key;
        for (key in obj) {
            if (obj.hasOwnProperty(key)) size++;
        }
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
    
    /*

    var sendToServer = function(command, data) {
        jsonData = {};

        switch(command) {
            case "newTodoList":
                jsonData = {
                    'cmd' : "nuovaListaDesideri",
                    'nomeListaDesideri' : data[0],
                    'idListaDesideri' : data[1]
                };
                break;

            case "editListName":
                jsonData = {
                    'cmd' : 'modificaNomeListaDesideri',
                    'nuovoNomeListaDesideri' : data[0],
                    'idListaDesideri' : data[1]
                };
                break;

            case "deleteList":
                jsonData = {
                    'cmd' : 'eliminaListaDesideri',
                    'idListaDesideri' : data[0]
                };
                break;

            case "newItem":
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

            case "editItemName":
                jsonData = {
                    'cmd' : 'modificaDescrizioneElemento',
                    'idListaDesideri' : data[0],
                    'idElemento' : data[1],
                    'descrizione' : data[2]
                };
                break;

            case "editItemQuantity":
                jsonData = {
                    'cmd' : 'modificaQuantitaElemento',
                    'idListaDesideri' : data[0],
                    'idElemento' : data[1],
                    'quantita' : data[2]
                };
                break;

            case "editItemBoughtFlag":
                jsonData = {
                    'cmd' : 'modificaFlagAcquistatoElemento',
                    'idListaDesideri' : data[0],
                    'idElemento' : data[1],
                    'acquistato' : data[2]
                };
                break;
            case "deleteItem":
                jsonData = {
                    'cmd' : 'eliminaElemento',
                    'idListaDesideri' : data[0],
                    'idElemento' : data[1]
                };
                break;

            default:
                console.log("Comando sconosciuto: " + command);

        }

        var request;
        request = $.ajax({
            url: document.URL, // TODO: sistemare l'url!!
            type: "post",
            data: jsonData,
            dataType: "html"
        
        });
        
        request.done(function (response, textStatus, jqXHR) {
    		var inserzioniDaSuggerireMap = JSON.stringify(response);
    		
    		console.log(inserzioniDaSuggerireMap['ID_ListaDesideri'] + " - " + inserzioniDaSuggerireMap['ID_Elemento']);
    		
    		
    		// GESTIRE L'OGGETTO RITORNATO.........................................TODO
    		
        	
        	// log a message to the console
            console.log("Hooray, it worked!");
        });
        
        request.fail(function (jqXHR, textStatus, errorThrown){
            // log the error to the console
            console.error("The following error occured: " +  textStatus + " - " + errorThrown);
        }); 
        
    };

    var modificaNomeLista = function () {
        if($(this).parent().hasClass('expanded'))
        {
            $(this).hide();
            $(this).parent().find(".todoListNameInput").show().val($(this).text()).focus();
        }      
    };

    var focusInputNomeLista = function() {
        // TODO controllare se il nome è diverso dal precendente.
        $(this).hide();  
        $(this).parent().find(".todoListName").show().text($(this).val());

        todoMap[$(this).parent().attr('id')]['Nome'] = $(this).val();
        localStorage.setItem('todoMap', JSON.stringify(todoMap));

        // TODO 
        // inviare richiesta al server per rinominare la lista
        sendToServer('editListName', [todoMap[$(this).parent().attr('id')]['Nome'], todoMap[$(this).parent().attr('id')]['ID_ListaDB']]);
    };

    var eliminaLista = function() {
        $(this).parent().fadeOut(function() { 
            var ID_ListaDB = todoMap[$(this).attr('id')]['ID_ListaDB']; 
            
            // TODO!!!
            // Inviare richiesta al server di eliminazione della lista.
            sendToServer('deleteList', [ID_ListaDB]);
            
            delete todoMap[$(this).attr('id')];
            localStorage.setItem("todoMap", JSON.stringify(todoMap));
            $(this).remove();
        });
    };

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
        // TODO!!
        // Inviare una richiesta al server di 'completato'
        sendToServer('editItemBoughtFlag', [todoMap[$item.parent().parent().attr('id')]['ID_ListaDB'], todoMap[$item.parent().parent().attr('id')]['Elementi'][$itemId]['ID_Elemento'], todoMap[$item.parent().parent().attr('id')]['Elementi'][$itemId]['acquistato']]);
    };

    var eliminaElemento = function() {
        $(this).parent().parent().fadeOut(function() { 
            
        	var idElemento = todoMap[$(this).parent().parent().attr('id')]['Elementi'][$(this).attr('id')]["ID_Elemento"];
            console.log(idElemento);
        	// TODO!!!
            // Inviare richiesta al server di eliminazione dell'elemento.
            sendToServer('deleteItem', [todoMap[$(this).parent().parent().attr('id')]['ID_ListaDB'], idElemento]);
            
            delete todoMap[$(this).parent().parent().attr('id')]['Elementi'][$(this).attr('id')];
            localStorage.setItem("todoMap", JSON.stringify(todoMap));
            $(this).remove();
            
        });
    };

    var modificaNomeElemento = function() {
        $(this).hide();
        $(this).parent().find(".todoElemNameInput").show().val($(this).text()).focus();
    };
    
    var focusInputNomeElemento = function() {
        $(this).hide();  
        $(this).parent().find(".todoElemName").show().text($(this).val());
        
        todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['testo'] = $(this).val();
        localStorage.setItem('todoMap', JSON.stringify(todoMap));

        // TODO
        // inviare richiesta al server per modificare il nome dell'elemento
        sendToServer('editItemName', [todoMap[$(this).parent().parent().parent().attr('id')]["ID_ListaDB"], todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['ID_Elemento'], $(this).val()]);
    };

    var modificaQuantitaElemento = function() {
        $(this).hide();
        $(this).parent().find(".todoElemQuantityInput").show().val($(this).text()).focus();
    };

    var focusInputQuantitaElemento = function() {
        $(this).hide();  
        $(this).parent().find(".todoElemQuantity").show().text($(this).val());

        todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['quantita'] = $(this).val();
        localStorage.setItem('todoMap', JSON.stringify(todoMap));

        // TODO
        // inviare richiesta al server per modificare la quantità dell'elemento
        sendToServer('editItemName', [todoMap[$(this).parent().parent().parent().attr('id')]["ID_ListaDB"], todoMap[$(this).parent().parent().parent().attr('id')]['Elementi'][$(this).parent().attr('id')]['ID_Elemento'], $(this).val()]);
    };
    
    
    
    */
    
    

    // Carica la/le todo list dal local storage
    var loadFromLocalStorage = function() {
        if (localStorage.getItem("todoMap") !== null) {
            
            todoMap = JSON.parse(localStorage.getItem('todoMap'));
            console.log(todoMap);
            
            for(var index in todoMap) { // index == todoList0, todoList1, todoList2, ... 
                // append the current todo list.
            	
            	console.log("Index: " + index + "\t Nome: " + todoMap[index]['Nome']);
            	
                $('#expList').append(
                    "<li id='" + index + "' class='singleTodoList collapsed'>" +
                        "<span class='todoListName'>" + todoMap[index]['Nome'] + "</span>" +
                        "<input type='text' class='todoListNameInput' style='display:none' />" + 
                        "<div class='delImg'></div>" +
                        "<div class='newItemContainer'>" +
                            "<input class='itemText' type='text'> " +
                            "<input class='itemQuantity' type='text'> " + 
                            "<div class='addNewItemBtn'>Add to List</div>" +
                        "</div>" +  
                        "<ul class='show-items'></ul>" +
                    "</li>"
                );
                
                // listeners per quando si clicca sul nome della lista in modo da editarlo
                $("#"+index + "> .todoListName").click(modificaNomeLista);

                $("#"+index + "> .todoListNameInput").focusout(focusInputNomeLista);
                
                // listener per eliminare l'intera lista
                $("#"+index + "> .delImg").click(eliminaLista);
                

                // appendo gli elenti alla lista in questione
                for(var k in todoMap[index]['Elementi']) {
                	console.log("\t\tElementi: " + k + "\t testo: " + todoMap[index]['Elementi'][k]['testo']);
                    var acquistato = todoMap[index]['Elementi'][k]['acquistato'];
                    
                    $("#"+index + " > ul").append(
                        "<li id='" + k + "' class='singleTodoItem" + ((acquistato) ? " completed" : "")  + "' >" +
                            "<span class='todoElemName'>" + ((acquistato) ? ("<strike>" + todoMap[index]['Elementi'][k]['testo'] + "</strike>") : (todoMap[index]['Elementi'][k]['testo'])) + "</span> - " +
                            "<input type='text' class='todoElemNameInput' style='display:none' />" +
                            "<span class='todoElemQuantity'>" + ((acquistato) ? ("<strike>" + todoMap[index]['Elementi'][k]['quantita'] + "</strike>") : (todoMap[index]['Elementi'][k]['quantita'])) + "</span>" +
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
                    
                }
                
            }
        }
    };
    
    $("#listContainer").show();
    (function init() {
        loadFromLocalStorage();       
    })();

    /*
    (function init() {
        setTimeout(function (){
            loadFromLocalStorage();
            $("#loaderGif").slideUp();
            $("#listContainer").show();
        }
            ,3000);
        
    })(); */
    

    var collapse_expande = function(event) {
        if (this == event.target) {
            $(this).toggleClass('expanded');
            $(this).children().not('span').not('.todoListNameInput').toggle('drop');
        }
        return false;
    };
    
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
    $('#newTodoListBtn').on('click', function() {
       /*
        var todoListId;
        if(localStorage.getItem('currentListId') !== null) 
            todoListId = "todoList" + localStorage.getItem('currentListId');
        else { 
            todoListId = "todoList0";
            localStorage.setItem('currentListId', 0);
        }
        */
    	
    	// creazione della nuova lista nella todoMap
        var todoListDBId = getHashCode();
    	var todoListId = "list_"+todoListDBId;
        todoMap[todoListId] = {};
        todoMap[todoListId]['Nome'] = "New TodoList";
        todoMap[todoListId]['ID_ListaDB'] = todoListDBId;
        todoMap[todoListId]['Elementi'] = {}; 
    	
    	
        $('#expList').append(
            "<li id='" + todoListId + "' class='singleTodoList collapsed expanded'>" +
                "<span class='todoListName'>New TodoList</span>" +
                "<input type='text' class='todoListNameInput' style='display:none' />" + 
                "<div class='delImg'></div>" +
                "<div class='newItemContainer'>" +
                    "<input class='itemText' type='text'> " +
                    "<input class='itemQuantity' type='text'> " + 
                    "<div class='addNewItemBtn'>Add to List</div>" +
                "</div>" +  
                "<ul class='show-items'></ul>" +
            "</li>"
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

        
        
        // salvataggio della todoMap e del currentListId nel localStorage
        localStorage.setItem('todoMap', JSON.stringify(todoMap));
        //localStorage.setItem('currentListId', parseInt(localStorage.getItem('currentListId')) + 1);

        // TODO
        // inviare una richiesta al sever contenente le informazioni della nuova lista
        sendToServer("newTodoList", [todoMap[todoListId]['Nome'], todoMap[todoListId]['ID_ListaDB']]);

    });

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
                'acquistato': false
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
        
        /*todoMap[todoListId]['Elementi'][itemId] = {
            'ID_Elemento': getHashCode(),
            'testo': itemText,
            'quantita': itemQuantity,
            'acquistato': false
        };*/

        //todoMap[todoListId]['ID_ProssimoElemento'] += 1;
        localStorage.setItem("todoMap", JSON.stringify(todoMap));

        // TODO 
        // inviare richiesta al server per salvare il nuovo elemento creato
        sendToServer('newItem', [todoMap[todoListId]['ID_ListaDB'], todoMap[todoListId]['Elementi'][itemId]]);
    };


    
    $(".addNewItemBtn").on('click', addNewItem);

   

};

var userPosition = null;
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
	/*
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

