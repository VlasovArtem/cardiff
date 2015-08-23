var app = angular.module('cardiff', ['ngRoute', 'underscore', 'ui.bootstrap.showErrors', 'ui.mask',
    'user-controllers', 'user-services', 'user-directives']).
    config(function($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'app/main.html'
            }).
            when('/signin', {
                templateUrl: 'app/user/sign-in.html',
                controller: 'SignInCtrl'
            }).
            when('/signup', {
                templateUrl: 'app/user/sign-up.html',
                controller: 'SignUpCtrl'
            }).
            otherwise({
                redirectTo: '/'
            })
    });