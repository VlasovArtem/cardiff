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
                controller: 'SignUpCtrl',
                resolve: {
                    locations: function(Locations) {
                        return Locations.query().$promise.then(function (data) {
                            return data;
                        })
                    }
                }
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
                    },
                    locations: function(Locations) {
                        return Locations.query().$promise.then(function (data) {
                            return data;
                        })
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
            when('/cards', {
                templateUrl: 'app/discount-card/discount-cards.html',
                controller: 'DiscountCardsCtrl',
                resolve: {
                    discountCards: function(CardsCtrl, $location) {
                        return CardsCtrl.get(
                            function(data) {
                                return data;
                            }, function() {
                                $location.path('/');
                            })
                    }
                }
            }).
            when('/add', {
                templateUrl: 'app/discount-card/add.html',
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