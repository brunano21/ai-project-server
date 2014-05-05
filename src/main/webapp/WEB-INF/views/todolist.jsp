<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Demo Expandable list</title>
	<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
	<script src="<c:url value="/resources/js/todolistJS/jquery-1.11.0.min.js"/>"></script>
	<script src="<c:url value="/resources/js/todolistJS/jquery-ui-1.8.7.custom.min.js"/>"></script>
	
	<script src="<c:url value="/resources/js/todolistJS/scripts.js"/>"></script>
</head>

    <body>
        <h1><b>Demo ExpandableList</b></h1>
        <div id="main">
            <div id="loaderGif"></div>
        
            <div id="listContainer">
                <div class="listControl">
                    <div id="expandList">Expand All</div>
                    <div id="collapseList">Collapse All</div>
                    <div id="newTodoListBtn">New Todo List</div>
                    <div id="saveBtn">Save</div>
                </div>
                <ul id="expList"></ul>
            </div>
        </div>
    </body>
</html>