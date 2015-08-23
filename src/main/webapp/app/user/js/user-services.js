var service = angular.module('user-services', ['ngResource']);

service.factory('Login', ['$resource', function($resource) {
    return $resource('/rest/user/login', {}, {
        login: {
            method: 'POST',
            isArray: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }
    })
}]);
service.factory('SignUp', ['$resource', function($resource) {
    return $resource('/rest/user/registration', {}, {
        registration: {
            method: 'POST'
        }
    })
}]);
