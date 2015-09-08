var service = angular.module('DC-services', ['ngResource']);

service.factory('addition', ['$resource', function($resource) {
    return $resource('/rest/card/addition', {}, {
        login: {
            method: 'POST',
            isArray: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }
    })
}]);

service.factory('CardsCtrl', ['$resource', function($resource) {
    return $resource('/rest/card/get/all');
}]);