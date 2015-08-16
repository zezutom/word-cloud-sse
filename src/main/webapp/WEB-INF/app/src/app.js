var app = angular.module('app', ['angular-jqcloud', 'ngTagsInput']);
                         	
app.controller('StreamController', function($scope, $http) {
	$scope.words = [];

    $scope.pushWords = function(msg) {
    	var words = JSON.parse(msg.data);
    	$scope.$apply(function() {
    		$scope.words = words;
    	});
    }

    $scope.listen = function() {
        $scope.wordsFeed = new EventSource('/words/stream');
        $scope.wordsFeed.addEventListener('message', $scope.pushWords, false);
    }

    $scope.listen();
});

app.controller('FilterController', function($scope, $http) {
	$scope.tags = [
                   { text: 'Android' },
                   { text: 'iPhone' },
                   { text: 'Blackberry' },
                   { text: 'Nokia' }
               ];	
});