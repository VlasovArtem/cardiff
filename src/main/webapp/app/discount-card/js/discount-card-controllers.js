var app = angular.module('discount-card-controllers', ['ngResource']);

app.controller('AddCtrl', ['$scope', '$location', 'AddDiscountCardFactory', 'tags', '$filter', 'DiscountCardFactory', '$route',
    function ($scope, $location, AddDiscountCardFactory, tags, $filter, DiscountCardFactory, $route) {
        $scope.tags = tags;
        $scope.card = {};
        $scope.today = new Date();
        $scope.addNewCard = function () {
            DiscountCardFactory.check({
                cardNumber: $scope.card.cardNumber,
                companyName: $scope.card.companyName
            }, function() {
                console.log($scope.card);
                AddDiscountCardFactory.save($scope.card, function() {
                    $location.path('/cards');
                }, function(data) {
                    $scope.error = data.data.error;
                })
            }, function() {
                $scope.error = {
                    error: "Discount card with entered number and company name is already exists"
                };
            })
        };
        $scope.reset = function () {
            $route.reload()
        };
    }
]);

app.controller('DiscountCardsCtrl', ['$scope', '$location', 'discountCards', 'DiscountCardsFactory', '$filter', '$sce', '$http',
    function ($scope, $location, discountCards, DiscountCardsFactory, $filter, $sce, $http) {
        $scope.tableInfo = {
            data: discountCards,
            factory: DiscountCardsFactory,
            rowClass: 'unavailable',
            head: [
                {name: 'Card #', property: 'cardNumber', width: '10%'},
                {name: 'Company', property: 'companyName', width: '20%'},
                {name: 'Discount', property: 'amountOfDiscount', width: '9%', class: 'center'},
                {name: 'Description', property: 'description', width: '25%'},
                {name: 'Created', property: 'createdDate', width: '10%', class: 'center'}
            ],
            filteredProperties: [
                {property: 'createdDate', filter: $filter('dateFilter')},
                {property: 'amountOfDiscount', appender: ' %'}
            ],
            dataButtons: {
                desktop: 'app/discount-card/discount-cards-buttons.html'
            },
            additionalSearch: {
                directive: 'tag-search'
            },
            htmlBinding: [{
                head: 'Tags',
                desktopClass: 'tags',
                desktop: $sce.trustAsHtml('<span class="label label-success tag-label" ng-repeat="tag in data.tags | orderBy :\'id\'" ng-bind="tag.tag" ng-click="chooseSearchTag(tag.tag)"></span>'),
                mobile: $sce.trustAsHtml('<span class="label label-success tag-label" ng-repeat="tag in data.tags | orderBy : \'id\'" ng-bind="tag.tag" ng-click="chooseSearchTag(tag.tag)"></span>')
            }]
        };

    }
]);

app.controller('DiscountCardsFunctionCtrl', ['$scope', '$sessionStorage', '$location',
    function($scope, $sessionStorage, $location) {
        $scope.findDiscountCard = function(discountCardId) {
            $sessionStorage.cardId = discountCardId;
            $location.path('/card/info');
        };
    }
]);

app.controller('DiscountCardInfoCtrl', ['$scope', 'discountCardInfo', 'PersonFactory', 'auth', '$modal', 'AuthDiscountCardFactory', '$sessionStorage',
    function($scope, discountCardInfo, PersonFactory, auth, $modal, AuthDiscountCardFactory, $sessionStorage) {
        AuthDiscountCardFactory.get({cardId: $sessionStorage.cardId}, function(data) {
            $scope.authPersonDiscountCard = data.authPersonCard;
        });
        $scope.cardInfo = discountCardInfo;
        do {
            $scope.authenticated = auth.authenticated;
        } while(!auth.resolved);
        if(auth.authenticated) {
            do {
                if(discountCardInfo.$resolved) {
                    PersonFactory.get({get: 'get', cardId: discountCardInfo.id}, function(person) {
                        $scope.owner = person;
                    }, function() {
                        $scope.error = "Required user is unavailable or deleted"
                    })
                }
            } while (!discountCardInfo.$resolved)
        }
        $scope.book = function() {
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: 'app/card-booking/booking-modal.html',
                controller: 'CardBookingCtrl',
                resolve: {
                    cardId: function() {
                        return $scope.cardInfo.id;
                    },
                    availableDate: function(CardBookingFactory) {
                        return CardBookingFactory.query({get: 'get', available: 'available', start: 'start', discountCardId: $scope.cardInfo.id}).$promise;
                    }
                }
            })
        }
    }
]);

app.controller('AccountDiscountCardsCtrl', ['$scope', '$filter', 'discountCards', 'OwnerDiscountCardsFactory', '$sce',
    function($scope, $filter, discountCards, OwnerDiscountCardsFactory, $sce) {
        $scope.tableInfo = {
            data: discountCards,
            dataTemplate: 'app/discount-card/table-data-template.html',
            factory: OwnerDiscountCardsFactory,
            head: [
                {name: 'Card #', property: 'cardNumber', width: '10%'},
                {name: 'Company', property: 'companyName', width: '20%'},
                {name: 'Discount', property: 'amountOfDiscount', width: '9%', class: 'center'},
                {name: 'Description', property: 'description', width: '25%'},
                {name: 'Created', property: 'createdDate', width: '10%', class: 'center'}
            ],
            filteredProperties: [
                {property: 'createdDate', filter: $filter('dateFilter')},
                {property: 'amountOfDiscount', appender: ' %'}
            ],
            dataButtons: {
                desktop: 'app/discount-card/account-discount-cards-buttons.html'
            },
            htmlBinding: [{
                head: 'Tags',
                desktopClass: 'tags',
                desktop: $sce.trustAsHtml('<span class="label label-success" ng-repeat="tag in data.tags | orderBy : \'id\'" ng-bind="tag.tag"></span>'),
                mobile: $sce.trustAsHtml('<span class="label label-success" ng-repeat="tag in data.tags | orderBy : \'id\'" ng-bind="tag.tag"></span>')
            }]
        };
    }
]);

app.controller('AccountDiscountCardsFunctionCtrl', ['$scope', '$location',
    function($scope, $location) {
        $scope.update = function(discountCardId) {
            $location.path('/card/update/' + discountCardId);
        }
    }
]);

app.controller('UpdateDiscountCardCtrl', ['$scope', 'discountCard', 'tags', 'UpdateDiscountCardFactory', '$location',
    function($scope, discountCard, tags, UpdateDiscountCardFactory, $location) {
        $scope.tags = tags;
        var wipeFormInfo = function () {
            var removableClasses = ["has-error", "has-warning", "has-success"];
            var removableElements = [".form-control-feedback", ".help-block"];
            _.each(removableElements, function(value) {
                _.each(angular.element.find(value), function(elem) {
                    elem.remove();
                })
            });
            _.each(angular.element.find('div.has-feedback'), function (elem) {

                _.each(removableClasses, function(removableClass) {
                    angular.element(elem).removeClass(removableClass);
                });
            });
            _.each($scope.updateFrom, function(value) {
                if(angular.isDefined(value)) {
                    if(value.$name) {
                        value.$setPristine();
                        value.$render();
                    }
                }
            });
        };
        $scope.updatedData = discountCard;
        $scope.data = angular.copy(discountCard);
        $scope.updateCard = function() {
            UpdateDiscountCardFactory.update($scope.updatedData,
                function() {
                    alert('Person data successfully changed');
                    $location.path('/account/cards');
                },
                function(data) {
                    $scope.error = data.data;
                }
            );
        };
        $scope.reset = function() {
            wipeFormInfo();
            $scope.updatedData = angular.copy($scope.data);
        };

        $scope.cardIsEquals = function() {
            if($scope.data != undefined && $scope.updatedData != undefined) {
                return _.every(["cardNumber", "companyName", "amountOfDiscount", "description", "tags"], function (data) {
                    if(data == "tags") {
                        return _.every($scope.updatedData.tags, function(updatedValue) {
                            return _.some($scope.data.tags, function(reserveValue) {
                                return _.isEqual(updatedValue.id, reserveValue.id);
                            });
                        })
                    } else {
                        return _.isEqual($scope.data[data], $scope.updatedData[data]) || $scope.data[data] == $scope.updatedData[data];
                    }
                });
            }
            return true;
        };
    }
]);