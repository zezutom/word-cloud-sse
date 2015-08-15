<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Word Cloud powered by AngularJS and jQcloud</title>
	<link rel="stylesheet" href="/app/bower_components/jqcloud2/dist/jqcloud.min.css">
</head>
<body ng-app="app" ng-controller="controller">
	<jqcloud words="words" width="500" height="350" steps="7"></jqcloud>
	<script src="/app/bower_components/angular/angular.min.js"></script>
	<script src="/app/bower_components/jquery/dist/jquery.min.js"></script>
	<script src="/app/bower_components/jqcloud2/dist/jqcloud.min.js"></script>	
	<script src="/app/bower_components/angular-jqcloud/angular-jqcloud.js"></script>
    <script src="/app/src/app.js"></script>
</body>
</html>