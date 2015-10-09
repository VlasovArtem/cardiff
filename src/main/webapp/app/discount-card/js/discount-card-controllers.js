var app = angular.module('discount-card-controllers', ['ngResource']);

app.controller('AddCtrl', ['$scope', '$location', 'AddDiscountCardFactory', 'tags', '$filter', 'DiscountCardFactory', '$route',
    function ($scope, $location, AddDiscountCardFactory, tags, $filter, DiscountCardFactory, $route) {
        $scope.tags = tags;
        $scope.card = {};
        $scope.today = new Date();
        $scope.addNewCard = function () {
            DiscountCardFactory.check({
                cardNumber: $scope.card.card_number,
                companyName: $scope.card.company_name
            }, function() {
                $scope.card.company_name = $filter('camelCase')($scope.card.company_name);
                AddDiscountCardFactory.save($scope.card, function() {
                    $location.hash('discount_cards');
                    $location.path('/account')
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

app.controller('DiscountCardsCtrl', ['$scope', '$location', 'discountCards', 'DiscountCardsFactory', '$filter', '$sce',
    function ($scope, $location, discountCards, DiscountCardsFactory, $filter, $sce) {
        $scope.tableInfo = {
            data: discountCards,
            factory: DiscountCardsFactory,
            head: [
                {name: 'Card #', property: 'card_number', width: '10%'},
                {name: 'Company', property: 'company_name', width: '20%'},
                {name: 'Discount', property: 'amount_of_discount', width: '9%'},
                {name: 'Description', property: 'description', width: '25%'},
                {name: 'Created', property: 'created_date', width: '10%'}
            ],
            filteredProperties: [
                {property: 'created_date', filter: $filter('dateFilter')},
                {property: 'phone_number', filter: $filter('phoneNumberFilter')},
                {property: 'amount_of_discount', appender: ' %'}
            ],
            dataButtons: {
                desktop: 'app/discount-card/discount-cards-buttons.html'
            },
            htmlBinding: [{
                head: 'Tags',
                desktopClass: 'tags',
                desktop: $sce.trustAsHtml('<span class="label label-success" ng-repeat="tag in data.tags | orderBy :\'id\'" ng-bind="tag.tag"></span>'),
                mobile: $sce.trustAsHtml('<span class="label label-success" ng-repeat="tag in data.tags | orderBy : \'id\'" ng-bind="tag.tag"></span>')
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
                {name: 'Card #', property: 'card_number', width: '10%'},
                {name: 'Company', property: 'company_name', width: '20%'},
                {name: 'Discount', property: 'amount_of_discount', width: '9%'},
                {name: 'Description', property: 'description', width: '25%'},
                {name: 'Created', property: 'created_date', width: '10%'}
            ],
            filteredProperties: [
                {property: 'created_date', filter: $filter('dateFilter')},
                {property: 'amount_of_discount', appender: ' %'}
            ],
            htmlBinding: [{
                head: 'Tags',
                desktopClass: 'tags',
                desktop: $sce.trustAsHtml('<span class="label label-success" ng-repeat="tag in data.tags | orderBy : \'id\'" ng-bind="tag.tag"></span>'),
                mobile: $sce.trustAsHtml('<span class="label label-success" ng-repeat="tag in data.tags | orderBy : \'id\'" ng-bind="tag.tag"></span>')
            }]
        };
    }
]);

app.controller('AccountDiscountCardsFunctionCtrl', ['$scope', function($scope) {}]);