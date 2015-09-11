var service = angular.module('discount-card-services', ['ngResource']);

service.factory('AddDiscountCardFactory', ['$resource', function($resource) {
    return $resource('/rest/card/add');
}]);

service.factory('CardsCtrl', ['$resource', function($resource) {
    return $resource('/rest/card/get/all');
}]);

service.factory('DiscountCardSearchFactory', ['$resource', function($resource) {
    return $resource('/rest/card/get/by/:name/:number/:tags', {
        name: '@name',
        number: '@number',
        tags: '@tags'
    }, {
        searchByName: {
            method: 'GET',
            params: {
                name: 'name'
            },
            isArray: true
        },
        searchByTags: {
            method: 'GET',
            params: {
                tags: 'tags'
            },
            isArray: true
        },
        searchByNumber: {
            method: 'GET',
            params: {
                number: 'number'
            }
        }
    })
}]);

service.factory('DiscountCardOwner', ['$resource', function($resource) {
    return $resource('/rest/card/get/:cardId/available', {
        cardId: '@cardId'
    })
}]);

service.factory('TagFactory', ['$resource', function($resource) {
    return $resource('/rest/tag/get/all')
}]);