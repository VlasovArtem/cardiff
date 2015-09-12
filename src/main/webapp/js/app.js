var app = angular.module('cardiff', ['ngRoute', 'underscore', 'ngStorage',
    'ui.bootstrap.showErrors', 'ui.mask', 'ui.bootstrap', 'ui.select',
    'person-controllers', 'person-services', 'person-directives', 'person-filters',
    'discount-card-controllers', 'discount-card-services', 'discount-card-directives']).config(
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
                    locations: function(Locations, auth) {
                        if(auth.authenticated) {
                            return Locations.query().$promise.then(function (data) {
                                return data;
                            })
                        }
                    }
                }
            }).
            when('/admin/persons', {
                templateUrl: 'app/person/admin-panel.html',
                controller: 'AdminPersonsCtrl',
                resolve: {
                    persons: function(AdminPersons, $location, auth) {
                        if(auth.admin) {
                            return AdminPersons.get(
                                function (data) {
                                    return data;
                                }, function () {
                                    $location.path('/');
                                })
                        } else {
                            $location.path('/');
                        }
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
            when('/card/info', {
                templateUrl: 'app/discount-card/discount-card-info.html',
                controller: 'DiscountCardInfoCtrl',
                resolve: {
                    discountCardInfo: function(DiscountCardOwner, $sessionStorage, $location) {
                        return DiscountCardOwner.get({cardId : $sessionStorage.cardId})
                            .$promise.then(
                            function (data) {
                                return data;
                            }, function () {
                                $location.path('/');
                            })
                    }
                }
            }).
            when('/card/add', {
                templateUrl: 'app/discount-card/add.html',
                controller: 'AddCtrl',
                resolve: {
                    tags: function(TagFactory) {
                        return TagFactory.query();
                    }
                }
            }).
            otherwise({
                redirectTo: '/'
            })
    });
app.run(['$rootScope', 'auth', '$location', function($root, auth, $location) {
    auth.init('/', '/signin', '/logout');
    $root.$on('$routeChangeStart', function(event, next, current) {
        var authenticatedRequiredPath = ['/account', '/account/update', '/admin/persons', '/card/add'];
        if(next) {
            if(_.isEqual(next.$$route.originalPath, '/')) {
                $('body').addClass('background');
            } else {
                $('body').removeClass('background');
            }
            if(_.contains(authenticatedRequiredPath, next.$$route.originalPath)) {
                if(!auth.authenticated) {
                    $location.path('/')
                }
            }
        }
    });
}]);