var app = angular.module('app', ['angular-jqcloud']);
                         	
app.controller('controller', function($scope, $http) {
	$scope.words = [];
	
	$http.get('/words').success(function(data) {
		$scope.words = data;
	}).error(function(data) {
		console.err('Request failed!')
	})
});