var app = angular.module('cardiff', ['ngRoute', 'underscore', 'ngStorage',
    'ui.bootstrap.showErrors', 'ui.mask', 'ui.bootstrap', 'ui.select',
    'person-controllers', 'person-services', 'person-directives', 'person-filters',
    'discount-card-controllers', 'discount-card-services', 'discount-card-directives', 'discount-card-filters',
    'main-controllers', 'main-services', 'main-directives', 'main-filters']).config(
    function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider
            .when('/', {
                redirectTo: '/cards'
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
            when('/account/cards', {
                templateUrl: 'app/discount-card/account-discount-cards.html',
                controller: 'AccountDiscountCardsCtrl',
                discountCards : function(OwnerCardsCtrl, $location) {
                    return OwnerCardsCtrl.getAll(function(data) {
                        return data;
                    }, function() {
                        $location.path('/');
                    })
                }
            }).
            when('/account/update', {
                templateUrl: 'app/person/update-account.html',
                controller: 'UpdateAccountCtrl',
                resolve: {
                    personData: function(Authenticated, $location, $sessionStorage) {
                        var person = angular.copy($sessionStorage.updatedPerson);
                        if(person) {
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
app.run(['$rootScope', 'auth', 'deviceCheck', '$sessionStorage', function($root, auth, deviceCheck, $sessionStorage) {
    auth.init('/', '/signin', '/logout');
    deviceCheck.checkIsMobile();
    $root.$on('$viewContentLoaded', function(event){
        $('.footer').fadeIn(500);
    });
    $root.$on('$routeChangeStart', function(event, next, current) {
        if(!_.isUndefined(next)) {
            if(next.$$route) {
                if(next.$$route.originalPath == '/') {
                    $('.view').removeClass('main-view');
                } else {
                    $('.view').addClass('main-view');
                }
            }
        }
    });
    $root.$on('$routeChangeSuccess', function(event, current, previous) {
        if(!_.isUndefined(current)) {
            if(current.$$route) {
                if(current.$$route.originalPath == '/card/info') {
                    delete $sessionStorage.updatedPerson;
                }
            }
        }
    });
    $root.$on('$routeChangeError', function(event, current, previous, reject) {
    });
}]);