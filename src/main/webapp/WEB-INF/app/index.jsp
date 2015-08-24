<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<c:set var="context" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Word Cloud powered by AngularJS and jQcloud</title>
	<link rel="stylesheet" href="${context}/assets/css/vendor/jqcloud.min.css">
	<link rel="stylesheet" type="text/css" href="${context}/assets/css/vendor/ng-tags-input.min.css">
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
	<script src="${context}/assets/js/vendor/angular.min.js"></script>
	<script src="${context}/assets/js/vendor/ng-tags-input.min.js"></script>		
	<script src="${context}/assets/js/vendor/jquery.min.js"></script>
	<script src="${context}/assets/js/vendor/jqcloud.min.js"></script>	
	<script src="${context}/assets/js/vendor/angular-jqcloud.js"></script>
    <script src="${context}/assets/js/app.js"></script>
</body>
</html>