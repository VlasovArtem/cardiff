var service = angular.module('discount-card-services', ['ngResource']);

service.factory('AddDiscountCardFactory', ['$resource', function($resource) {
    return $resource('/rest/card/add');
}]);

service.factory('DiscountCardFactory', ['$resource', function($resource) {
    return $resource('/rest/card/check', {}, {
        check: {
            method: 'GET'
        }
    })
}]);

service.factory('DiscountCardsFactory', ['$resource', function($resource) {
    return $resource('/rest/card/get/all', {}, {
        getAll: {
            method: 'GET'
        }
    });
}]);

service.factory('OwnerDiscountCardsFactory', ['$resource', function($resource) {
    return $resource('/rest/card/owner/all', {}, {
        getAll: {
            method: 'GET'
        }
    })
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
    return $resource('/rest/card/get/:cardId/', {
        cardId: '@cardId'
    })
}]);

service.factory('TagFactory', ['$resource', function($resource) {
    return $resource('/rest/tag/get/all')
}]);