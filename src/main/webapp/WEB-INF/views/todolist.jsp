<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
    <script type="text/javascript" src="<c:url value="resources/js/todolistJS/scripts.js" />" ></script>
    
   	<div class="todolistContainer">
        <div id="loaderGif"></div>
        <div class="listControl">
            <div id="expandList">Expand All</div>
            <div id="collapseList">Collapse All</div>
            <div id="newTodoListBtn">New Todo List</div>
            <div id="saveBtn">Save</div>
        </div>

        <div id="post_left" class="col-1-2">
                <div id="listContainer">
                    <ul id="expList"></ul>
                </div>
        </div>


        <div id="post_right" class="col-1-2">
            <div class="carousel-list-mini">

                <div class="carousel-item-mini">
                    <div class="featured-image">
                        <img src="http://www.lascelta.com/media/catalog/product/cache/1/image/700x477/9df78eab33525d08d6e5fb8d27136e95/d/s/dsc00499.jpg">
                    </div>
                    <div class="post-margin">
                        <h6><a href="#">Pasta Barilla</a></h6>
                        <div><i class="fa fa-map-marker"></i>Conad Via Pergole</div>
                        <div><i class="fa fa-clock-o"></i> March 20, 2014</div>
                    </div>
                    <div class="connect-item genericBtn"><i class="fa fa-plus fa-lg"></i>Add</div>
                </div>

                <div class="carousel-item-mini">
                    <div class="featured-image">
                        <img src="http://www.lascelta.com/media/catalog/product/cache/1/image/700x477/9df78eab33525d08d6e5fb8d27136e95/d/s/dsc00499.jpg">
                    </div>
                    <div class="post-margin">
                        <h6><a href="#">Pasta Barilla</a></h6>
                        <div><i class="fa fa-map-marker"></i>Conad Via Pergole</div>
                        <div><i class="fa fa-clock-o"></i> March 20, 2014</div>
                    </div>
                    <div class="connect-item genericBtn"><i class="fa fa-plus fa-lg"></i>Add</div>
                </div>

                <div class="carousel-item-mini">
                    <div class="featured-image">
                        <img src="http://www.lascelta.com/media/catalog/product/cache/1/image/700x477/9df78eab33525d08d6e5fb8d27136e95/d/s/dsc00499.jpg">
                    </div>
                    <div class="post-margin">
                        <h6><a href="#">Pasta Barilla</a></h6>
                        <div><i class="fa fa-map-marker"></i>Conad Via Pergole</div>
                        <div><i class="fa fa-clock-o"></i> March 20, 2014</div>
                    </div>
                    <div class="connect-item genericBtn"><i class="fa fa-plus fa-lg"></i>Add</div>
                </div>

            </div>
        </div>

    </div>