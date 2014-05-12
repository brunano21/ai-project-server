<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

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
<link rel="stylesheet" id="superfish-css" href="<c:url value="resources/css/superfish.css" />" type="text/css" media="all" >
<link rel="stylesheet" id="fontawsome-css"  href="<c:url value="resources/css/font-awsome/css/font-awsome.css" />" type="text/css" media="all" >
<!-- <link rel='stylesheet' id='orbit-css-css'  href='css/orbit.css' type='text/css' media='all' /> -->
<link rel="stylesheet" id="style-css" href="<c:url value="resources/css/indexStyle.css" />" type="text/css" media="all" >
<link rel="stylesheet" id="color-scheme-css" href="<c:url value="resources/css/color/green.css" />" media="all" >
<link rel="stylesheet" href="<c:url value="resources/css/zerogrid.css" />" type="text/css" media="screen">
<link rel="stylesheet" href="<c:url value="resources/css/responsive.css" />" type="text/css" media="screen">

<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery-1.10.2.min.js" />" ></script>
<!-- <script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery.js" />" ></script>-->
<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery-migrate.min.js" />" ></script>

<script type="text/javascript" src="<c:url value="resources/js/indexJS/jquery.carouFredSel-6.2.1-packed.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/hoverIntent.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/indexJS/superfish.js" />" ></script>

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

    function sendLogin() {
   		// qui posso far apparire un scritta con contacting server
        $.ajax({
        	url:"./j_spring_security_check", 
            type: 'POST', 
            async: false, 
            data: {
	            j_username: $("#username").val(), 
	            j_password: $("#password").val()
	        }, 
            success: function(returnedData) {         
            	console.log("done!");
               	console.log(returnedData);
				$("#loginForm").hide();
				$("#logContainer").append(returnedData);
   				// e qui invece 
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
                    //callback option fires, when the request finishes, whether in failure or success. 
                    //It receives the jqXHR object, as well as a string containing the success or error code.
                   } 
        });
    };


       
    	       
   	function getRegisterForm() {
   		$.ajax({
        	url:"./register", 
            type: 'GET', 
                async: false,
                context: document.body, 
                data: { }, 
                success: function(returnedData, textStatus, jqXHR) {         
					$("#registerContainer").html(returnedData);
					/*$("#registerContainer").find("script").each(function(i) {
                        eval($(this).text());
                    });*/
                	$("#overlayPanel").show();
                    $("#registerContainer").show();
                }
        });
    };

    
</script>

<script type="text/javascript">
	$(function() {
		


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
    });*/
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
			
			
		/* Start Super fish */
		$(document).ready(function(){
			$('ul.sf-menu').superfish({
				delay:         100,
				speed:         'fast',
				speedOut:      'fast',
			});

            $('#caro22usel-list').carouFredSel({
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

            $("#overlayPanel").on('click', hideOverlayPanel);

			

		});
		/* End Of Super fish */
			
	});
</script>
</head>

<body class="home blog">
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
                            <input id="username" type="text" placeholder="Username" autocomplete="on" autofocus required>   
                            <input id="password" type="password" placeholder="Password" required>
                        </fieldset>
                        <fieldset id="actions">
                            <input type="submit" id="submit" value="Log in">
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
                        <!-- current-menu-item-->

                        <li class="menu-item">
                            <a href="#">Prodotti</a>
                            <ul class="sub-menu">
                                <li class="menu-item"><a href="#">Inserisci</a></li>
                                <li class="menu-item"><a href="#">Valuta</a></li>
                                <li class="menu-item"><a href="#">Cerca</a></li>
                            </ul>
                        </li>

                        <li class="menu-item">
                            <a href="#">Le tue liste</a>
                        </li>
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
        <div class="col-2-3" id="post-container">
 			<div class="wrap-col">
        	    <!-- Start Post Item -->
                <div class="post">
                    <h2>DESCRIZIONE0</h2><br>
                    <h2>DESCRIZIONE1</h2><br>
                    <h2>DESCRIZIONE2</h2>
                    <!-- <div id="registerContainer">
                        <form  id="registerForm" action="" autocomplete="on"> 
                            <h1> Sign up </h1> 
                            <fieldset id="registerInputs" >
                                <p> 
                                    <label for="usernamesignup">Your username</label>
                                    <input id="usernamesignup" name="usernamesignup" required="required" type="text" placeholder="mysuperusername690" />
                                </p>
                                <p> 
                                    <label for="emailsignup"> Your email</label>
                                    <input id="emailsignup" name="emailsignup" required="required" type="email" placeholder="mysupermail@mail.com"/> 
                                </p>
                                <p> 
                                    <label for="passwordsignup">Your password </label>
                                    <input id="passwordsignup" name="passwordsignup" required="required" type="password" placeholder="eg. X8df!90EO"/>
                                </p>
                                <p> 
                                    <label for="passwordsignup_confirm">Please confirm your password </label>
                                    <input id="passwordsignup_confirm" name="passwordsignup_confirm" required="required" type="password" placeholder="eg. X8df!90EO"/>
                                </p>
                            </fieldset>
                            <fieldset id="registerButtons">
                                <input type="submit" id="signup" value="Sign Up">
                                <p>
                                    Già Registrato?<br>
                                    <a href="#tologin" class="to_register">Effettua il Log In </a>
                                </p>
                            </fieldset>
                                
                                
                            </form>
                    </div> -->
                </div>
                <div class="clear"></div>
            </div>
            <!-- End Post Item -->
            
             
            <div class="clear"></div>
		</div>
        <!-- End Left Container -->
		
        <!-- Start Right Container -->
    	<div class="col-1-3">
    		<div class="wrap-col">
    	    	<!--
                <div class="widget-container">
                    <form role="search" method="get" id="searchform" class="searchform" action="http://demo.themesmarts.com/euclid/">
        				<div>
        					<label class="screen-reader-text" for="s">Search for:</label>
        					<input type="text" value="" name="s" id="s" />
        					<input type="submit" id="searchsubmit" value="Search" />
        				</div>
        			</form>
                    <div class="clear"></div>
                </div>
                -->
                <div id="carousel-list" class="widget-container">
                    <div class="carousel-item">
                        <div class="post-margin">
                            <h6><a href="#">Port Harbor 1</a></h6>
                            <span><i class="fa fa-clock-o"></i> December 13, 2013</span>
                        </div>
                        
                        <div class="featured-image">
                            <img src="http://www.lascelta.com/media/catalog/product/cache/1/image/700x477/9df78eab33525d08d6e5fb8d27136e95/d/s/dsc00499.jpg"  />
                        </div>
                        <div class="post-margin">testo sul prodotto</div>

                    </div>
                    <div class="carousel-item">
                        <div class="post-margin">
                            <h6><a href="#">Port Harbor 2</a></h6>
                            <span><i class="fa fa-clock-o"></i> December 13, 2013</span>
                        </div>
                        
                        <div class="featured-image">
                            <img src="http://www.lascelta.com/media/catalog/product/cache/1/image/700x477/9df78eab33525d08d6e5fb8d27136e95/d/s/dsc00499.jpg"  />
                        </div>
                        <div class="post-margin">testo sul prodotto</div>
                    </div>
                    <div class="carousel-item">
                        <div class="post-margin">
                            <h6><a href="#">Port Harbor 3 </a></h6>
                            <span><i class="fa fa-clock-o"></i> December 13, 2013</span>
                        </div>
                        
                        <div class="featured-image">
                            <img src="http://www.lascelta.com/media/catalog/product/cache/1/image/700x477/9df78eab33525d08d6e5fb8d27136e95/d/s/dsc00499.jpg"  />
                        </div>
                        <div class="post-margin">testo sul prodotto</div>
                    </div>
                    <div class="carousel-item">
                        <div class="post-margin">
                            <h6><a href="#">Port Harbor 4</a></h6>
                            <span><i class="fa fa-clock-o"></i> December 13, 2013</span>
                        </div>
                        
                        <div class="featured-image">
                            <img src="http://www.lascelta.com/media/catalog/product/cache/1/image/700x477/9df78eab33525d08d6e5fb8d27136e95/d/s/dsc00499.jpg"  />
                        </div>
                        <div class="post-margin">testo sul prodotto</div>
                    </div>
                    <div class="carousel-item">
                        <div class="post-margin">
                            <h6><a href="#">Port Harbor 5</a></h6>
                            <span><i class="fa fa-clock-o"></i> December 13, 2013</span>
                        </div>
                        
                        <div class="featured-image">
                            <img src="http://www.lascelta.com/media/catalog/product/cache/1/image/700x477/9df78eab33525d08d6e5fb8d27136e95/d/s/dsc00499.jpg"  />
                        </div>
                        <div class="post-margin">testo sul prodotto</div>

                    </div>
                    <div class="carousel-item">
                        <div class="post-margin">
                            <h6><a href="#">Port Harbor 6</a></h6>
                            <span><i class="fa fa-clock-o"></i> December 13, 2013</span>
                        </div>
                        
                        <div class="featured-image">
                            <img src="http://www.lascelta.com/media/catalog/product/cache/1/image/700x477/9df78eab33525d08d6e5fb8d27136e95/d/s/dsc00499.jpg"  />
                        </div>
                        <div class="post-margin">testo sul prodotto</div>

                    </div>

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


</body>

</html>