var service = angular.module('discount-card-services', ['ngResource']);

service.factory('AddDiscountCardFactory', ['$resource', function($resource) {
    return $resource('/rest/card/add');
}]);
service.factory('UpdateDiscountCardFactory', ['$resource', function($resource) {
    return $resource('/rest/card/update', {}, {
        update: {
            method: 'PUT'
        }
    });
}]);
service.factory('DiscountCardFactory', ['$resource', function($resource) {
    return $resource('/rest/card/:check/:get/:cardId', {}, {
        check: {
            method: 'GET',
            params: {
                check : 'check'
            }
        }
    })
}]);

service.factory('DiscountCardsFactory', ['$resource', function($resource) {
    return $resource('/rest/card/get/page/:search');
}]);

service.factory('OwnerDiscountCardsFactory', ['$resource', function($resource) {
    return $resource('/rest/card/owner/page')
}]);

service.factory('AuthDiscountCardFactory', ['$resource', function($resource) {
    return $resource('/rest/card/auth/:cardId', {
        cardId: '@cardId'
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