<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Word Cloud powered by AngularJS and jQcloud</title>
	<link rel="stylesheet" href="/app/bower_components/jqcloud2/dist/jqcloud.min.css">
	<link rel="stylesheet" type="text/css" href="/app/bower_components/ng-tags-input/ng-tags-input.min.css">
</head>
<body ng-app="app">
	<div ng-controller="StreamController">
		<jqcloud words="words" width="500" height="350" steps="10"></jqcloud>
	</div>
	<form ng-submit="submit()" ng-controller="FilterController">
		<tags-input ng-model="tags"></tags-input>
	</form>
	<script src="/app/bower_components/angular/angular.min.js"></script>
	<script src="/app/bower_components/ng-tags-input/ng-tags-input.min.js"></script>		
	<script src="/app/bower_components/jquery/dist/jquery.min.js"></script>
	<script src="/app/bower_components/jqcloud2/dist/jqcloud.min.js"></script>	
	<script src="/app/bower_components/angular-jqcloud/angular-jqcloud.js"></script>
    <script src="/app/src/app.js"></script>
</body>
</html>