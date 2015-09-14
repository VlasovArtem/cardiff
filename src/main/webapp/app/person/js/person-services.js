var service = angular.module('person-services', ['ngResource']);

service.factory('Login', ['$resource', function($resource) {
    return $resource('/rest/person/login', {}, {
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
    return $resource('/rest/person/registration', {}, {
        registration: {
            method: 'POST'
        }
    })
}]);
service.factory('Authentication', ['$resource', function($resource) {
    return $resource('/rest/person/authentication');
}]);
service.factory('Authenticated', ['$resource', function($resource) {
    return $resource('/rest/person/authenticated');
}]);
service.factory('auth', ['$resource', '$location', '$route', 'Login', 'Authentication', 'PersonFactory', function($resource, $location, $route, Login, Authentication, PersonFactory) {
    var enter = function() {
        if($location.path() != auth.loginPath) {
            auth.path = $location.path();
            if(!auth.authenticated) {
                $location.path(auth.loginPath);
            }
        }
    };
    var auth = {
        authenticated: false,
        admin: false,
        resolved: false,
        loginPath: '/signin',
        logoutPath: '/logout',
        homePath: '/',
        error: null,
        authentication: function() {
            Authentication.get(
                function() {
                    PersonFactory.get({
                            authorized: 'authorized',
                            hasRole: 'ADMIN'
                        },
                        function() {auth.admin = true;},
                        function() {auth.admin = false;});
                    auth.authenticated = true;
                    auth.resolved = true;
                },
                function() {
                    auth.authenticated = false;
                    auth.resolved = true;
                }
            )
        },
        authenticate: function(credentials, callback) {
            Login.login($.param({
                loginData: credentials.loginData,
                password: credentials.password,
                rememberMe: credentials.rememberMe ? credentials.rememberMe : false
            }), function() {
                auth.authenticated = true;
                auth.authentication();
                $location.path(auth.homePath);
                $route.reload();
            }, function(data) {
                auth.authenticated = false;
                callback && callback(data.data.error);
            })
        },
        clear: function() {
            auth.authenticated = false;
            $location.path(auth.loginPath);
            var res = $resource('/rest/person/logout');
            res.get();
        },
        init: function(homePath, loginPath, logoutPath) {
            auth.homePath = homePath;
            auth.loginPath = loginPath;
            auth.logoutPath = logoutPath;
            auth.authentication();
        }
    };
    return auth;
}]);
service.factory('changePassword', ['$resource', function($resource) {
    return $resource('/rest/person/password/update', {}, {
        change: {
            method: 'PUT',
            isArray: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }
    });
}]);

service.factory('AdminPersonFactory', ['$resource', function($resource) {
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
}]);

service.factory('PersonFactory', ['$resource', function($resource) {
    return $resource('/rest/person/:update/:delete/:get/:authorized/:cardId/:id', {
        update: '@update',
        delete: '@delete',
        get: '@get',
        authorized: '@authorized',
        cardId: '@cardId',
        id: '@id'
    }, {
        updatePerson: {
            method: 'PUT',
            isArray: false
        }
    })
}]);
service.factory('UpdatePerson', [function() {
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
}]);
service.factory('Locations', ['$resource', function($resource) {
    return $resource('/rest/location/get/all')
}]);
