<%@ page import="hibernate.Inserzione" %>
<%@ page import="dati.Dati" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html lang="en-US">
<!--[if lt IE 7 ]><html class="ie ie6" lang="en"> <![endif]-->
<!--[if IE 7 ]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!--><html lang="en"> <!--<![endif]-->
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<title>Clever Buy Homepage</title>

<link rel="stylesheet" id="reset-css" href="<c:url value="resources/css/reset.css" />" type="text/css" media="all" >
<link rel="stylesheet" id="zerogrid-css" href="<c:url value="resources/css/zerogrid.css" />" type="text/css" media="screen">
<link rel="stylesheet" id="superfish-css" href="<c:url value="resources/css/superfish.css" />" type="text/css" media="all">
<link rel="stylesheet" id="fontawsome-css" href="<c:url value="resources/css/font-awsome/css/font-awesome.css" />" type="text/css" media="all">
<link rel="stylesheet" id="responsive-css" href="<c:url value="resources/css/responsive.css" />" type="text/css"  media="screen">
<link rel="stylesheet" id="mainstyle-css" href="<c:url value="resources/css/indexStyle.css" />" type="text/css" media="all">
<link rel="stylesheet" id="select-css" href="<c:url value="resources/css/select-theme-default.css" />" >
<link rel="stylesheet" id="color-scheme-css" href="<c:url value="resources/css/color/green.css" />" media="all">
<link rel="stylesheet" id="jquery-ui-css" href="<c:url value="resources/css/ui-lightness/jquery-ui-1.10.4.custom.min.css" />" media="all">
<link rel="stylesheet" id="jquery-ui-css-min" href="<c:url value="resources/css/jquery-ui-1.11.0/jquery-ui.min.css" />" media="all">
<link rel="stylesheet" id="todolistStyle-css" href="<c:url value="resources/css/todolistStyle.css" />">
<link rel="stylesheet" id="tooltipster-css" href="<c:url value="resources/css/tooltipster/tooltipster.css" />">
<link rel="stylesheet" id="tooltipster-light-css" href="<c:url value="resources/css/tooltipster/themes/tooltipster-light.css" />">
<link rel="stylesheet" id="bxslider-css" href="<c:url value="resources/css/bxslider/jquery.bxslider.css" />">
<link rel="stylesheet" id="valutazioneStyle-css" href="<c:url value="resources/css/valutazioneStyle.css" />">
<link rel="stylesheet" id="convertiCreditiStyle-css" href="<c:url value="resources/css/convertiCreditiStyle.css" />">

<script type="text/javascript" src="<c:url value="resources/js/jquery-1.11.1.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery-migrate.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/jquery-ui-1.11.0/jquery-ui.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery.ui.datepicker-it.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery.color-2.1.2.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery.carouFredSel-6.2.1-packed.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/hoverIntent.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/superfish.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/select.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/valutazioneJS/bxslider/jquery.bxslider.min.js" />"></script>

<script type="text/javascript" src="<c:url value="resources/js/valutazioneJS/bxslider/jquery.bxslider.min.js" />"></script>
<script type="text/javascript" src="<c:url value="resources/js/valutazioneJS/valutazione.js" />"></script>

<script src="https://www.google.com/jsapi" type="text/javascript"></script>
<script type="text/javascript">
    google.load('search', '1');
</script>

<!-- <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true&libraries=places"></script> -->

<!-- <link rel='stylesheet' id='orbit-css-css'  href='css/orbit.css' type='text/css' media='all' /> -->
<!--<script type='text/javascript' src='js/orbit.min.js'></script>-->
<!--<script src="js/css3-mediaqueries.js"></script>-->

<script type="text/javascript">

    function showRegisterForm() {
        $("#overlayPanel").show();
        $("#registerContainer").show();
    };
    function showForgotPwdForm() {
        $("#overlayPanel").show();
        $("#forgotPwdContainer").show();
    };

    function hideOverlayPanel(e) {
        if ( e.target.id != 'overlayPanel')
            return false;
        $(this).children().hide();
        $(this).hide();
    };
    
    function getHomePage() {
    	$("#header-nav-container").find(".current-menu-item").removeClass("current-menu-item");
    	$("#home-menu-item").addClass("current-menu-item");
    	
    	$.ajax({
        	url:"./home", 
            type: 'GET', 
                async: true,
                success: function(returnedData) {
                	$(".post").children().hide();
    				$(".post").html(returnedData);
                }
        });
    }

    function sendLogin() {
        $.ajax({
        	url:"./j_spring_security_check", 
            type: 'POST', 
            async: false, 
            data: {
	            j_username: $("#username").val(), 
	            j_password: $("#password").val(),
	            'latitudine' : userPosition.coords.latitude,
                'longitudine' : userPosition.coords.longitude
	        }, 
            success: function(returnedData, textStatus, jqXHR) {         
				if(jqXHR.getResponseHeader("loginFailed") != null) {
					$("#executeJS").html(returnedData);
					console.log("loginFailed");
				} else {
					$("#loginForm").hide();
					$("#logContainer").html(returnedData);

					getUserCustomization_Index();

			        localStorage.removeItem("lat");					
	            }
            }      
        });
    };

    function sendLogout() {
	   	// qui posso far apparire un scritta con contacting server
        $.ajax({url:"./j_spring_security_logout", 
            type: 'POST', 
                async: false, 
                data: { }, 
                success: function(returnedData) {         
                	console.log("done!");
                }
        });
    };
    
    function getUserCustomization_Index() {
    	$.ajax({
        	url:"./custom-index", 
            type: 'GET', 
                async: true,
                context: document.body, 
                data: { 
                	'latitudine' : userPosition.coords.latitude,
                    'longitudine' : userPosition.coords.longitude
                    }, 
                success: function(returnedData, textStatus, jqXHR) {
					$(".right-side-box").children().hide();
					$(".right-side-box").html(returnedData);
					
					$(".homeContainer").css("height", "902px");
					
					startCarousels();
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log("Error msg " + xhr.status + " " + ajaxOptions);
				}
        });
    };
    	       
   	function getRegisterForm() {
		if ($("#registerContainer").children().length != 0)
			showRegisterForm();
		else
	   		$.ajax({
	        	url:"./register", 
	            type: 'GET', 
	                async: false,
	                context: document.body, 
	                data: { }, 
	                success: function(returnedData, textStatus, jqXHR) {         
						$("#registerContainer").html(returnedData);
						//$("#registerContainer").find("script").each(function(i) { eval($(this).text()); });
	                	$("#overlayPanel").show();
	                    $("#registerContainer").show();
	                }
	        });
    };

    function getInserzionePage() {
    	$("#header-nav-container").find(".current-menu-item").removeClass("current-menu-item");
    	$("#prodotti-menu-item").addClass("current-menu-item");
    	
		// controllare se l'inserzioneContainer non è gia presente. Se fosse cosi, devo solo mostralo e non fare la GET.
    	$.ajax({
        	url:"./inserzione", 
            type: 'GET', 
            async: true, 
            success: function(returnedData, textStatus, jqXHR) {         
				$(".post").children().hide();
				$(".post").html(returnedData);
            }
        });
    };

    function getValutaPage() {
    	$("#header-nav-container").find(".current-menu-item").removeClass("current-menu-item");
    	$("#prodotti-menu-item").addClass("current-menu-item");
    	
    	$.ajax({
        	url:"./valutazione", 
            type: 'GET', 
            async: true,
            data: {
            	'latitudine' : userPosition.coords.latitude,
                'longitudine' : userPosition.coords.longitude
            },
            success: function(returnedData, textStatus, jqXHR) {
				$(".post").children().hide();
				$(".post").html(returnedData);
            }      
        });
    };

    function getLeTueListePage() {
    	$("#header-nav-container").find(".current-menu-item").removeClass("current-menu-item");
    	$("#liste-menu-item").addClass("current-menu-item");
    	
    	$.ajax({
        	url:"./todolist", 
            type: 'GET', 
            async: true, 
            success: function(returnedData, textStatus, jqXHR) {
            	$('#garbage').html(returnedData);
            	$(".post").children().hide();
            	$('#garbage >.todolistContainer').appendTo('.post');
            }
        });
    };
    
    //function getInScadenzaPage() {};
    //function getMiglioriAffariPage() {};
    
    function getRiscattaCreditiPage() {
    	$("#header-nav-container").find(".current-menu-item").removeClass("current-menu-item");
    	$("#account-menu-item").addClass("current-menu-item");
    	
    	$.ajax({
        	url:"./convertiCrediti", 
            type: 'GET', 
            async: true, 
            success: function(returnedData, textStatus, jqXHR) {
            	console.log(returnedData);
				$(".post").children().hide();
				$(".post").html(returnedData);
            }
    	});
    }
    
    //function getModificaAccountPage() {};
    //function getStatistichePage() {};
    //function getPremiumPage() {};

    function getAboutPage() {
    	$("#header-nav-container").find(".current-menu-item").removeClass("current-menu-item");
    	$("#about-menu-item").addClass("current-menu-item");
    	
        $.ajax({
	    	url:"./about", 
	        type: 'GET', 
	        async: true, 
	        success: function(returnedData, textStatus, jqXHR) {
	        	$(".post").children().hide();
				$(".post").html(returnedData);
	        }      
    });
        };
</script>

<script type="text/javascript">
	$(function() {
        
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
            
            
        jQuery(document).ready(function($){
        	
        	getUserGeoloc();
            
            /* Start Super fish */
            $('ul.sf-menu').superfish({
                delay : 100,
                speed : 'fast',
                speedOut : 'fast'
            });
		    /* End Of Super fish */
            
            $('#carousel-list').carouFredSel({
                items               : 3,
                direction           : "up",
                scroll : {
                    items           : 1,
                    easing          : "cubic",
                    duration        : 1500,                         
                    pauseOnHover    : true
                },
                wrapper : {
                	element : "div",
                	classname : "caroufredsel_wrapper"
                }
            });

            $("#overlayPanel").on('click', hideOverlayPanel);
            
            // Fixa il problema della lista troppo lunga di circa 20 pixel 
            $(".caroufredsel_wrapper").css("height", "798px");
        });
			
	});
</script>

</head>

<body class="home blog">
	<div id="executeJS"></div> 
    <div id="overlayPanel">
        <div id="registerContainer"></div>
        <div id="forgotPwdContainer">
            <form  id="forgotPwdForm" action="javascript:void(0);" onsubmit="sendForgotPassword();" autocomplete="on"> 
                <h1> Forgot Password </h1> 
                <fieldset id="forgotPwdInputs" >
                    <p> 
                        <label for="emailInput">Your email</label>
                        <input id="emailInput" name="emailforgot" required="required" type="text" placeholder="mysupermail@mail.com" autocomplete="off" />
                    </p>
                </fieldset>
                <fieldset id="forgotPwdButtons">
                    <input type="submit" id="forgotpwd" value="Forgot Password">
                </fieldset>
            </form>
        </div>
        <div id="suggerimento-singolo-container"></div>
        <div id="popupConfermaInserzioneContainer">
        	<div id="caricamentoDatiInCorsoBox">
				<div class="fa fa-spinner fa-spin fa-4x"></div>
				<div><h5>Caricamento dati in corso...</h5></div>
			</div>
  			<div id="confermaInserzioneBox">
	  			<div><h5>Inviare inserzione?</h5></div>
	  			<div id="confermaInserzioneButtonsContainer">
	  				<input type="button" id="annullaInserzioneBtn" class="genericBtn" value="Annulla" />
	  				<input type="button" id="inviaInserzioneBtn" class="genericBtn" value="Conferma" />
  				</div>
			</div>
			<div id="caricamentoDatiConErrori">
				<div class="fa fa-exclamation-triangle fa-4x"></div>
				<div><h5>Ops, ci sono stati<br>degli errori.</h5></div>	
			</div>
			<div id="caricamentoDatiConSuccesso">
				<div class="fa fa-smile-o fa-4x"></div>
				<div><h5>Inserzione aggiunta<br>con successo!</h5></div>	
			</div>
    	</div>
   	</div>

	<!-- Start Logo/Login Header -->
    <div class="container zerogrid">
        <div class="col-3-5">
            <div class="wrap-col">
               <!--<div id="header-nav-container">-->
                    <div id="headerLogoContainer">
                    <!-- logo -->
                        <a href="#"></a>
                        <div class="verticalLine"> </div>
                        <h5>POLITECNICO DI TORINO</h5>                 
                        <h1>CLEVER BUY</h1>
                    <div class="clear"></div>

                </div>
            </div>
        </div>
        
        <div class="col-2-5">
            <div class="wrap-col">
                <div id="logContainer">
                	
                    <!-- login -->
                    <form id="loginForm" action="javascript:void(0);" onsubmit="sendLogin();">
                         <fieldset id="inputs">
                            <div class="input-group">
                                <span class="input-icon"><i class="fa fa-user fa-fw"></i></span>
                                <input id="username" class="input-control" type="text" placeholder="Username" autocomplete="on" autofocus required>
                            </div>
                            <div class="input-group">
                                <span class="input-icon"><i class="fa fa-key fa-flip-horizontal fa-fw"></i></span>
                                <input id="password" class="input-control" type="password" placeholder="Password" autocomplete="on" required>
                            </div>
                        </fieldset>
                        <fieldset id="actions">
                            <input type="submit" class="genericBtn" id="submit" value="Log in">
                            <a href="javascript:void(0);" onclick="showForgotPwdForm();">Password dimenticata?</a>
                            <a href="javascript:void(0);" onclick="getRegisterForm();">Registrati</a>
                        </fieldset>
                        
                    </form>
                     
                    <div class="clear"></div>
                </div>
            </div>
        </div>
        <div class="clear"></div> 
    </div>
    <!-- End Logo/Login Header -->

    <div class="spacing-30"></div>

    <!-- Start Controls Header -->
    <div class="container zerogrid">
        <div class="col-full">
            <div class="wrap-col">
            	<div id="header-nav-container">
                
                    <!-- Navigation Menu -->
                    <ul class="sf-menu">
                        
                        <li id="home-menu-item" class="menu-item current-menu-item">
                        	<a href="javascript:void(0);" onclick="getHomePage();">Home</a>
                        </li>
                        
                        <li id="prodotti-menu-item" class="menu-item">
                            <a href="#">Prodotti</a>
                            <ul class="sub-menu">
                                <li class="menu-item"><a href="javascript:void(0);" onclick="getInserzionePage();">Inserisci</a></li>
                                <li class="menu-item"><a href="javascript:void(0);" onclick="getValutaPage();">Valuta</a></li>
                            </ul>
                        </li>

                        <li id="liste-menu-item" class="menu-item"> <a href="javascript:void(0);" onclick="getLeTueListePage();">Le tue liste</a> </li>
                        <!-- 
                        <li id="scadenza-menu-item" class="menu-item">
                            <a href="#">In scadenza</a>
                        </li>
                        <li id="affari-menu-item" class="menu-item">
                            <a href="#">I migliori affari</a>
                        </li>
                        -->
                        <li id="account-menu-item" class="menu-item">
                            <a href="#">Account</a>
                            <ul class="sub-menu">
                                <li class="menu-item"><a href="#">Premium</a></li>
                                <li class="menu-item"><a href="javascript:void(0);" onclick="getRiscattaCreditiPage();">Usa Crediti</a></li>
                            </ul>
                        </li>
						<li id="about-menu-item" class="menu-item">
                            <a href="javascript:void(0);" onclick="getAboutPage();">About</a>
                        </li>
                    </ul>	
                    <!-- End Navigation Menu -->
                        
                    <div class="clear"></div>
                </div>
			</div>
        </div>
        <div class="clear"></div> 
    </div>
    <!-- End Controls Header -->
    
    <div class="spacing-30"></div>
    
    <!-- Start Main Container -->
    <div class="container zerogrid">
       
        <!-- Start Left Container -->
        <div class="col-3-4" id="post-container">
 			<div class="wrap-col">
        	    <!-- Start Post Item -->
                <div class="post">
	                <div id="home" class="homeContainer" >
		                <h2>Benvenuto su CLEVER BUY!</h2>
		                <div>
			                Trova le offerte migliori e segnala i prodotti più convenienti.
			                Potrai guadagnare tanti crediti e risparmiare ancora di più.
			                <br><br>
			                
			                Clever Buy è pensata per gli amanti dello shopping e del risparmio.
			                Potrai creare le tue liste dei desideri, durante la compilazione il sistema ti suggerirà i prodotti disponibili intorno a te al prezzo migliore.
			                <br><br>
			                
			                Consulta le tue liste usando la nostra applicazione Android, è semplice, utile e GRATUITA.
			                <br><br>
			                
			                Inserisci le tue inserzioni o valuta quelle che ti sono state utili, puoi guadagnare tanti crediti e ricevere buoni sconto o effettuare l'upgrade del tuo account.
			                La versione PREMIUM offre funzionalità di ricerca avanzate, statistiche sui prodotti e tanto altro ancora...
		                </div>
		                <h2>ISCRIVITI SUBITO... è GRATIS!</h2>
	                </div>
                </div>
                <div class="clear"></div>
            </div>
            <!-- End Post Item -->
            
             
            <div class="clear"></div>
		</div>
        <!-- End Left Container -->
		
        <!-- Start Right Container -->
    	<div class="col-1-4">
    		<div class="wrap-col right-side-box">
                <div id="carousel-list" class="widget-container">
                    <%
				    ArrayList<Integer> inserzioniIDs = Dati.getInstance().getInserzioniValide();
				    Inserzione inserzione = null;
				    String supermercato = null;
				    
				    for (Integer index : inserzioniIDs) {
				    	inserzione = Dati.getInstance().getInserzioni().get(index);
				    	supermercato = inserzione.getSupermercato().getNome() + ", " + inserzione.getSupermercato().getIndirizzo() + ", " + inserzione.getSupermercato().getComune();
				    	System.out.println(inserzione.getDescrizione());
				    	System.out.println(supermercato);
				    %>  
				    <div class="carousel-item">
				        <div class="post-margin">
				            <h6><a href="#"><%= inserzione.getProdotto().getDescrizione() %></a></h6>
				            <span><i class="fa fa-clock-o"></i><%= inserzione.getDataFine() %></span>
				        </div>
				        <div class="featured-image">
				            <img class="featured-image-img" src="<%= inserzione.getFoto() %>"  />
				        </div>
				        <div class="post-margin"><i class="fa fa-map-marker"></i><%= supermercato %></div>
						<div class="post-margin"><i class="fa fa-eur"></i><%= inserzione.getPrezzo() %></div>
				    </div>
				    <%
				    }
				    %>
                </div>
                <div class="clear"></div>
            </div>
        </div>        
        <!-- End Sidebar -->
    
        <div class="clear"></div>
    </div>
	<!-- End Main Container -->

    <!-- Start Footer -->
    <div class="spacing-30"></div>
    <div class="container zerogrid">
        <div id="footer-container" class="col-full">
            <div class="wrap-col">	
                <!-- Footer Copyright -->
                <p>Copyright &copy; 2014 Politecnico di Torino. All Rights Reserved.</p>
                <!-- End Footer Copyright -->
                
                <!-- Footer Logo -->
    			<img src="" id="footer-logo" />
                <!-- End Footer Logo -->
            
            <div class="clear"></div>
    		</div>
        </div>
    </div>
    <!-- End Footer -->

	<div id="garbage" style="display: none;"></div>
</body>

</html>
