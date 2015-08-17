var app = angular.module('app', ['angular-jqcloud', 'ngTagsInput']);
                         	
app.controller('StreamController', function($scope, $http) {
	$scope.words = [];

	$scope.subId = '';
	
    $scope.pushWords = function(msg) {
    	var words = JSON.parse(msg.data);
    	$scope.$apply(function() {
    		$scope.words = words;
    	});
    }

    $scope.listen = function() {
        $scope.wordsFeed = new EventSource('/api/v1/stream/words/' + $scope.subId);
        $scope.wordsFeed.addEventListener('message', $scope.pushWords, false);
    }

    $http.get('/api/v1/stream/subscribe').success(function(data) {
    	
    	// Keep the subscription ID
    	$scope.subId = data;
    	
    	// Start listening for push notifications
    	$scope.listen();
    	
    	// Request the filter tags
    	$http.get('/api/v1/stream/filters/' + data).success(function(data) {
    		$scope.tags = data;
    	}).error(function(data) {
    		console.error('Failed to fetch the filter tags!');
    	});    	
    	
    }).error(function(data) {
    	console.error('Subscription failed!');
    });
});

app.controller('FilterController', function($scope, $http) {
	$scope.tags = [];
	
	$scope.filterTags = function(query) {
        return $http.get('/api/v1/stream/filters/' + $scope.subId + '/' + query);
	};
	
});