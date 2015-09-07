var app = angular.module('cardiff', ['ngRoute', 'underscore', 'ui.bootstrap.showErrors', 'ui.mask', 'ui.bootstrap',
    'person-controllers', 'person-services', 'person-directives', 'person-filters', 'DC-controllers', 'DC-services']).config(
    function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider
            .when('/', {
                templateUrl: 'app/main.html',
                controller: 'SearchCtrl'
            }).
            when('/signin', {
                templateUrl: 'app/person/sign-in.html',
                controller: 'NavCtrl'
            }).
            when('/signup', {
                templateUrl: 'app/person/sign-up.html',
                controller: 'SignUpCtrl'
            }).
            when('/account', {
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
            when('/account/update', {
                templateUrl: 'app/person/update-account.html',
                controller: 'UpdateAccountCtrl',
                resolve: {
                    personData: function(Authenticated, $location, UpdatePerson) {
                        var person = angular.copy(UpdatePerson.getPerson());
                        if(person) {
                            UpdatePerson.setPerson(null);
                            return person
                        } else {
                            return Authenticated.get().$promise.then(
                                function(data) {
                                    return data;
                                }, function() {
                                    $location.path('/');
                                });
                        }
                    }
                }
            }).
            when('/admin/persons', {
                templateUrl: 'app/person/admin-panel.html',
                controller: 'AdminPersonsCtrl',
                resolve: {
                    persons: function(AdminPersons, $location) {
                        return AdminPersons.get(
                            function(data) {
                                return data;
                            }, function() {
                                $location.path('/');
                            })
                    }
                }
            }).
            when('/card/get/all', {
                templateUrl: 'app/DiscountCard/DiscountCards.html',
                controller: 'DiscountCardsCtrl',
                resolve: {
                    discountCards: function(getAll, $location) {
                        return getAll.get(
                            function(data) {
                                return data;
                            }, function() {
                                $location.path('/');
                            })
                    }
                }
            }).
            when('/Add', {
                templateUrl: 'app/DiscountCard/Add.html',
                controller: 'AddCtrl'
            }).
            otherwise({
                redirectTo: '/'
            })
    });
app.run(['$rootScope', 'auth', function($root, auth) {
    $root.$on('$routeChangeStart', function(event, next, current) {
        if(next) {
            if(_.isEqual(next.$$route.originalPath, '/')) {
                $('body').addClass('background');
            } else {
                $('body').removeClass('background');
            }
        }
    });
    auth.init('/', '/signin', '/logout')
}]);