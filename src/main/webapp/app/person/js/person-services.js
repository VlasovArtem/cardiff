var service = angular.module('person-services', ['ngResource']);

service.factory('Login', ['$resource',
    function($resource) {
        return $resource('/rest/person/login', {}, {
            login: {
                method: 'POST',
                isArray: false,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }
        })
    }
]);

service.factory('SignUp', ['$resource',
    function($resource) {
        return $resource('/rest/person/registration', {}, {
            registration: {
                method: 'POST'
            }
        })
    }
]);
service.factory('Authentication', ['$resource',
    function($resource) {
        return $resource('/rest/person/authentication');
    }
]);
service.factory('Authenticated', ['$resource',
    function($resource) {
        return $resource('/rest/person/authenticated');
    }
]);

service.factory('changePassword', ['$resource',
    function($resource) {
        return $resource('/rest/person/password/update', {}, {
            change: {
                method: 'PUT',
                isArray: false,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }
        });
    }
]);

service.factory('AdminPersonFactory', ['$resource',
    function($resource) {
        return $resource('/rest/person/admin/:get/:all/:delete/:update/:role/:restore/:id', {
            get: '@get',
            all: '@all',
            delete: '@delete',
            update: '@update',
            role: '@role',
            restore: '@restore',
            id: '@id'
        }, {
            restore: {
                method: 'PUT',
                params: {
                    restore: 'restore'
                }
            },
            updateRole: {
                method: 'PUT',
                params: {
                    update: 'update',
                    role: 'role'
                }
            },
            getAll: {
                method: 'GET',
                params: {
                    get: 'get',
                    all: 'all'
                }
            }
        })
    }
]);

service.factory('PersonFactory', ['$resource',
    function($resource) {
        return $resource('/rest/person/:delete/:get/:authorized/:cardId/:id', {
            delete: '@delete',
            get: '@get',
            authorized: '@authorized',
            cardId: '@cardId',
            id: '@id'
        })
    }
]);

service.factory('PersonUpdateFactory', ['$resource',
    function($resource) {
        return $resource('/rest/person/update', {} , {
            updatePerson: {
                method: 'PUT'
            }
        })
    }
]);

service.factory('UpdatePerson', [
    function() {
        var person = null;
        var setPerson = function(data) {
            person = data;
        };
        var getPerson = function() {
            return person;
        };
        return {
            setPerson: setPerson,
            getPerson: getPerson
        }
    }
]);

service.factory('Locations', ['$resource', function($resource) {
    return $resource('/rest/location/get/all')
}]);
