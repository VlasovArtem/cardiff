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
service.factory('auth', ['$resource', '$location', '$route', 'Login', 'Authentication', function($resource, $location, $route, Login, Authentication) {
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
        loginPath: '/signin',
        logoutPath: '/logout',
        homePath: '/',
        error: null,
        authentication: function() {
            Authentication.get(
                function() { auth.authenticated = true },
                function() { auth.authenticated = false }
            )
        },
        authenticate: function(credentials, callback) {
            Login.login($.param({
                login_data: credentials.login_data,
                password: credentials.password,
                remember_me: credentials.remember_me ? credentials.remember_me : false
            }), function() {
                auth.authenticated = true;
                $location.path(auth.homePath);
                $route.reload();
                callback && callback(auth.authenticated);
            }, function(data) {
                auth.error = data.error;
                auth.authenticated = false;
                callback && callback(false);
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
            method: 'POST',
            isArray: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }
    });
}]);
