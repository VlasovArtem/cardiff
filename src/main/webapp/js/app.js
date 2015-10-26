var app = angular.module('cardiff', ['ngRoute', 'underscore', 'ngStorage', 'ngSanitize',
    'ui.bootstrap.showErrors', 'ui.mask', 'ui.bootstrap', 'ui.select', 'ngCapsLock',
    'person-controllers', 'person-services', 'person-directives', 'person-filters',
    'discount-card-controllers', 'discount-card-services', 'discount-card-directives', 'discount-card-filters',
    'main-controllers', 'main-services', 'main-directives', 'main-filters',
    'card-booking-controllers', 'card-booking-services', 'card-booking-filters',
    'tag-controllers', 'tag-services']).config(
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
                resolve: {
                    discountCards: function (OwnerDiscountCardsFactory) {
                        return OwnerDiscountCardsFactory.get().$promise;
                    }
                }
            }).
            when('/account/bookings', {
                templateUrl: 'app/card-booking/person-card-bookings.html',
                controller: 'PersonDiscountCardBookingsCtrl',
                resolve: {
                    booked: function(PersonBookedDiscountCardsFactory) {
                        return PersonBookedDiscountCardsFactory.get().$promise;
                    },
                    bookings: function(PersonDiscountCardBookingsFactory) {
                        return PersonDiscountCardBookingsFactory.get().$promise;
                    }
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
                    persons: function(AdminPersonTableFactory) {
                        return AdminPersonTableFactory.get().$promise;
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
                    discountCards: function(DiscountCardsFactory) {
                        return DiscountCardsFactory.get().$promise;
                    }
                }
            }).
            when('/card/info', {
                templateUrl: 'app/discount-card/discount-card-info.html',
                controller: 'DiscountCardInfoCtrl',
                resolve: {
                    discountCardInfo: function(DiscountCardOwner, $sessionStorage) {
                        return DiscountCardOwner.get({cardId : $sessionStorage.cardId}).$promise;
                    }
                }
            }).
            when('/card/add', {
                templateUrl: 'app/discount-card/add.html',
                controller: 'AddCtrl',
                resolve: {
                    tags: function(TagFactory) {
                        return TagFactory.query().$promise;
                    }
                }
            }).
            when('/admin/tags', {
                templateUrl: 'app/tag/custom-tag-admin-panel.html',
                controller: 'AdminTagCtrl',
                resolve: {
                    customTags: function(AdminCustomTagFactory) {
                        return AdminCustomTagFactory.get().$promise;
                    },
                    tags: function(TagFactory) {
                        return TagFactory.query().$promise;
                    }
                }
            }).
            otherwise({
                redirectTo: '/'
            })
    });
app.run(['$rootScope', 'auth', 'deviceCheck', '$sessionStorage', 'Authentication', '$compile',
    function($root, auth, deviceCheck, $sessionStorage, Authentication, $compile) {
        auth.init('/', '/signin', '/logout');
        deviceCheck.checkIsMobile();
        $root.$on('$viewContentLoaded', function(event){
            $('.footer').fadeIn(2000);
        });
        $root.$on('$routeChangeStart', function(event, next, current) {
            $('.footer').hide();
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
            $('.footer').fadeIn(2000);
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