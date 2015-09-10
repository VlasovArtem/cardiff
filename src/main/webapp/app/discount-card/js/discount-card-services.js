var service = angular.module('DC-services', ['ngResource']);

service.factory('addition', ['$resource', function($resource) {
    return $resource('/rest/card/add', {}, {
        add: {
            method: 'POST',
        }
    })
}]);

service.factory('CardsCtrl', ['$resource', function($resource) {
    return $resource('/rest/card/get/all');
}]);