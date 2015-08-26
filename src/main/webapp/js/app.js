var app = angular.module('cardiff', ['ngRoute', 'underscore', 'ui.bootstrap.showErrors', 'ui.mask',
    'person-controllers', 'person-services', 'person-directives', 'person-filters']).config(
    function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider
            .when('/', {
                templateUrl: 'app/main.html'
            }).
            when('/cardiff/signin', {
                templateUrl: 'app/person/sign-in.html',
                controller: 'NavCtrl'
            }).
            when('/cardiff/signup', {
                templateUrl: 'app/person/sign-up.html',
                controller: 'SignUpCtrl'
            }).
            when('/cardiff/account', {
                templateUrl: 'app/person/account.html',
                controller: 'AccountCtrl',
                resolve: {
                    personData: function(Authenticated, $location) {
                        return Authenticated.get(function(data) {
                            return data;
                        }, function() {
                            $location.path('/');
                        });
                    }
                }
            }).
            otherwise({
                redirectTo: '/'
            })
    });
app.run(function(auth) {
    auth.init('/', '/cardiff/signin', '/logout')
});