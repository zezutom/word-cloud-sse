<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<c:set var="context" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Word Cloud powered by AngularJS and jQcloud</title>
	<link rel="stylesheet" href="${context}/app/bower_components/jqcloud2/dist/jqcloud.min.css">
	<link rel="stylesheet" type="text/css" href="${context}/app/bower_components/ng-tags-input/ng-tags-input.min.css">
</head>
<body ng-app="app">
	<div ng-controller="StreamController">
		<jqcloud words="words" width="500" height="350" steps="10"></jqcloud>
		<div>
			<button ng-click="toggleSubscription()">
				{{label}}
			</button>
		</div>
	</div>
	<form ng-submit="submit()" ng-controller="FilterController">
		<tags-input ng-model="tags">
			<auto-complete source="filterTags($query)"></auto-complete>
		</tags-input>
	</form>
	<script src="${context}/app/bower_components/angular/angular.min.js"></script>
	<script src="${context}/app/bower_components/ng-tags-input/ng-tags-input.min.js"></script>		
	<script src="${context}/app/bower_components/jquery/dist/jquery.min.js"></script>
	<script src="${context}/app/bower_components/jqcloud2/dist/jqcloud.min.js"></script>	
	<script src="${context}/app/bower_components/angular-jqcloud/angular-jqcloud.js"></script>
    <script src="${context}/app/src/app.js"></script>
</body>
</html>