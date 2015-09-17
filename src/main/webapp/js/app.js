var app = angular.module('cardiff', ['ngRoute', 'underscore', 'ngStorage',
    'ui.bootstrap.showErrors', 'ui.mask', 'ui.bootstrap', 'ui.select', 'wu.masonry',
    'person-controllers', 'person-services', 'person-directives', 'person-filters',
    'discount-card-controllers', 'discount-card-services', 'discount-card-directives',
    'main-controllers', 'main-services', 'main-directives', 'main-filters']).config(
    function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider
            .when('/', {
                templateUrl: 'app/main/main.html',
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
                    persons: function(AdminPersonFactory) {
                        return AdminPersonFactory.getAll();
                    },
                    admin: function(PersonFactory) {
                        return PersonFactory.get({
                            authorized: 'authorized',
                            hasRole: 'ADMIN'});
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
app.run(['$rootScope', 'auth', 'deviceCheck', function($root, auth, deviceCheck) {
    auth.init('/', '/signin', '/logout');
    deviceCheck.checkIsMobile();
    $root.$on('$viewContentLoaded', function(event){
        $('.footer').fadeIn(500);
    });
}]);