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
<link rel="stylesheet" id="todolistStyle-css" href="<c:url value="resources/css/todolistStyle.css" />">
<link rel="stylesheet" id="tooltipster-css" href="<c:url value="resources/css/tooltipster/tooltipster.css" />">
<link rel="stylesheet" id="tooltipster-light-css" href="<c:url value="resources/css/tooltipster/themes/tooltipster-light.css" />">
<link rel="stylesheet" id="bxslider-css" href="<c:url value="resources/css/bxslider/jquery.bxslider.css" />">
<link rel="stylesheet" id="valutazioneStyle-css" href="<c:url value="resources/css/valutazioneStyle.css" />">

<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery-1.10.2.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery-migrate.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery-ui-1.10.4.custom.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery.ui.datepicker-it.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery.color-2.1.2.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery.carouFredSel-6.2.1-packed.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/hoverIntent.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/superfish.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/select.min.js" />" ></script>

<script type="text/javascript" src="<c:url value="resources/js/valutazioneJS/bxslider/jquery.bxslider.min.js" />"></script>
<script type="text/javascript" src="<c:url value="resources/js/valutazioneJS/valutazione.js" />"></script>


<script src="https://www.google.com/jsapi" type="text/javascript"></script>
<script type="text/javascript">
    google.load('search', '1');
</script>

<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true&libraries=places"></script>
<script src="https://www.google.com/jsapi" type="text/javascript"></script>

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
            	console.log(returnedData);
				
				if(jqXHR.getResponseHeader("loginFailed") != null) {
					$("#executeJS").html(returnedData);
						
					//console.log($(returnedData).find("script").innerHTML);
					//eval($(returnedData).find("script").innerHTML);
				}
										
				else {
					$("#loginForm").hide();
					$("#logContainer").html(returnedData);
					getUserCustomization_Index();
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
                },
                complete: function() { 
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
					
					startCarousels();
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log("Error msg " + xhr.status + " " + ajaxOptions);
				}
        });
    }
    	       
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
		// controllare se l'inserzioneContainer non è gia presente. Se fosse cosi, devo solo mostralo e non fare la GET.
    	$.ajax({
        	url:"./inserzione", 
            type: 'GET', 
            async: false, 
            success: function(returnedData, textStatus, jqXHR) {         
            	//console.log(returnedData);
				$(".post").children().hide();
				$(".post").html(returnedData);
            }      
        });
    };

    function getCercaPage() {};
    function getValutaPage() {
    	$.ajax({
        	url:"./valutazione", 
            type: 'GET', 
            async: false, 
            success: function(returnedData, textStatus, jqXHR) {
				$(".post").children().hide();
				$(".post").html(returnedData);
            }      
        });
    };

    function getLeTueListePage() {
    	$.ajax({
        	url:"./todolist", 
            type: 'GET', 
            async: false, 
            success: function(returnedData, textStatus, jqXHR) {
            	
            	$('#garbage').html(returnedData);
            	$(".post").children().hide();
            	$('#garbage >.todolistContainer').appendTo('.post');
            	
            	console.log(returnedData);
				//$(".post").children().hide();
				//$(".post").html(returnedData);
            }      
        });
    };
    //function getInScadenzaPage() {};
    
    function getMiglioriAffariPage() {};
    //function getModificaAccountPage() {};
    //function getStatistichePage() {};
    //function getPremiumPage() {};

    function getAboutPage() {};
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
                    easing          : "elastic",
                    duration        : 1500,                         
                    pauseOnHover    : true
                },
                wrapper : {
                	element : "div",
                	classname : "caroufredsel_wrapper"
                }
            });

            $("#overlayPanel").on('click', hideOverlayPanel);
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
                        
                        <li class="menu-item current-menu-item">
                            <a href="#">Home</a>
                        </li>
                        
                        <li class="menu-item">
                            <a href="#">Prodotti</a>
                            <ul class="sub-menu">
                                <li class="menu-item"><a href="javascript:void(0);" onclick="getInserzionePage();">Inserisci</a></li>
                                <li class="menu-item"><a href="javascript:void(0);" onclick="getValutaPage();">Valuta</a></li>
                                <li class="menu-item"><a href="#">Cerca</a></li>
                            </ul>
                        </li>

                        <li class="menu-item"> <a href="javascript:void(0);" onclick="getLeTueListePage();">Le tue liste</a> </li>
                        <li class="menu-item">
                            <a href="#">In scadenza</a>
                        </li>
                        <li class="menu-item">
                            <a href="#">I migliori affari</a>
                        </li>
                        <li class="menu-item">
                            <a href="#">Account</a>
                            <ul class="sub-menu">
                                <li class="menu-item"><a href="#">Modifica</a></li>
                                <li class="menu-item"><a href="#">Statistiche</a></li>
                                <li class="menu-item"><a href="#">Premium</a></li>
                            </ul>
                        </li>
						<li class="menu-item">
                            <a href="#">About</a>
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
		                <h1>BENVENUTO</h1>
		                <h1>Sig. Malnati!!</h1>
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