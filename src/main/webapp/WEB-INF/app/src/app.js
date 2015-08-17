var app = angular.module('app', ['angular-jqcloud', 'ngTagsInput']);
                         	
app.controller('StreamController', function($scope, $http) {
	$scope.words = [];

	$scope.subId = '';
	
	$scope.label = 'Start';
	
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

    $scope.subscribe = function() {
        $http.get('/api/v1/stream/subscribe').success(function(data) {
        	
        	// Keep the subscription ID
        	$scope.subId = data;
        	
        	// Start listening for push notifications
        	$scope.listen();
        	
        	// Change the label
        	$scope.label = 'Stop';
        	
        	// Request the filter tags
        	$http.get('/api/v1/stream/filters/' + data).success(function(data) {
        		$scope.tags = data;
        	}).error(function(data) {
        		console.error('Failed to fetch the filter tags!');
        	});    	
        	
        }).error(function(data) {
        	console.error('Subscription failed!');
        });    	
    };
    
    $scope.unsubscribe = function() {
		// server side
		$http.get('/api/v1/stream/unsubscribe/' + $scope.subId).success(function(done) {
			if (done) {
		    	// client side
				$scope.wordsFeed.close();
				$scope.subId = null;
				$scope.label = 'Start';				
			} else {
				console.error('The attempt to unsubscribe wasn\'t successful. Try later.');
			}
		}).error(function(data) {
			console.error('The attempt to unsubscribe wasn\'t successful. Try later.');
		});
		
		
    }
    
    $scope.toggleSubscription = function() {
    	if ($scope.subId) {
    		$scope.unsubscribe();
    	} else {
    		$scope.subscribe();
    	}    	
    };
    
    // Subscribe and let it all rock!
    $scope.subscribe();
});

app.controller('FilterController', function($scope, $http) {
	$scope.tags = [];
	
	$scope.filterTags = function(query) {
        return $http.get('/api/v1/stream/filters/' + $scope.subId + '/' + query);
	};	
});