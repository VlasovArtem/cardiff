var app = angular.module('discount-card-controllers', ['ngResource']);

app.controller('AddCtrl', ['$scope', '$location', 'AddDiscountCardFactory', 'tags', '$filter',
    function ($scope, $location, AddDiscountCardFactory, tags, $filter) {
        $scope.tags = tags;
        $scope.card = {};
        $scope.currentDate = new Date();
        $scope.addNewCard = function () {
            $scope.card.company_name = $filter('camelCase')($scope.card.company_name);
            AddDiscountCardFactory.save($scope.card, function() {
                $location.path('/account')
            }, function(data) {
                $scope.error = data.data;
            })
        };
        $scope.removeLastTag = function() {
            $scope.card.tags.pop();
        };
        $scope.reset = function () {
            $scope.card = {};
        };
    }
]);

app.controller('DiscountCardsCtrl', ['$scope', '$location', 'discountCards', 'CardsCtrl', '$filter', '$sessionStorage',
    function ($scope, $location, discountCards, CardsCtrl, $filter, $sessionStorage) {

        $scope.tableInfo = {
            data: discountCards,
            dataTemplate: 'app/discount-card/table-data-template.html',
            factory: CardsCtrl,
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
            ]
        };

        $scope.initialSort = {
            direction: 'DESC',
            property: 'createdDate'
        };

        $scope.findDiscountCard = function(discountCardId) {
            $sessionStorage.cardId = discountCardId;
            $location.path('/card/info');
        };

    }
]);

app.controller('DiscountCardInfoCtrl', ['$scope', 'discountCardInfo', 'PersonFactory', 'auth',
    function($scope, discountCardInfo, PersonFactory, auth) {
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

    }
]);