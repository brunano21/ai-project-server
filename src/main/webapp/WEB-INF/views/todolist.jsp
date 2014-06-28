<%@ page import="hibernate.Inserzione" %>
<%@ page import="dati.Dati" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<script type="text/javascript" src="<c:url value="resources/js/todolistJS/scripts.js" />" ></script>
<script type="text/javascript" src="<c:url value="resources/js/todolistJS/jquery.tooltipster.min.js" />" ></script>

<div class="todolistContainer">

    <div id="post_left" class="col-1-2">
	    <div class="listControl">
	        <div id="expandList">Expand</div>
	        <div id="collapseList">Collapse</div>
	        <div id="newTodoListBtn">New List</div>
	    </div>
	     <div id="listContainer">
	         <ul id="expList"></ul>
	     </div>
    </div>

    <div id="post_right" class="col-1-2">
        <h3>SUGGERIMENTI</h3>
        <div class="carousel-list-mini"></div>
    </div>

</div>

<div id="carousel-list" class="widget-container">
 <%
 ArrayList<Integer> inserzioniIDs = Dati.getInstance().getInserzioniValide();
 Inserzione inserzione = null;
 String supermercato = null;
 
 for (Integer index : inserzioniIDs) {
 	inserzione = Dati.getInstance().getInserzioni().get(index);
 	supermercato = inserzione.getSupermercato().getNome() + ", " + inserzione.getSupermercato().getIndirizzo() + ", " + inserzione.getSupermercato().getComune();
 %>  <div class="carousel-item">
     <div class="post-margin">
         <h6><a href="#"><%= inserzione.getProdotto().getDescrizione() %></a></h6>
         <span><i class="fa fa-clock-o"></i><% inserzione.getDataFine(); %></span>
     </div>
     <div class="featured-image">
         <img src="<% inserzione.getFoto(); %>"  />
     </div>
     <div class="post-margin"><i class="fa fa-map-marker"></i><% System.out.print(supermercato); %></div>
<div class="post-margin"><i class="fa fa-eur"></i><% inserzione.getPrezzo(); %></div>
 </div>
 <%
 }
 %>
</div>