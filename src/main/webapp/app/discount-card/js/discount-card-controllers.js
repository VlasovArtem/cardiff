var app = angular.module('discount-card-controllers', ['ngResource']);
app.controller('AddCtrl', ['$scope', '$location', 'AddDiscountCardFactory', function ($scope, $location, AddDiscountCardFactory) {
    $scope.currentDate = new Date();
    $scope.add = function () {
        AddDiscountCardFactory.add($scope.card)
    };
    $scope.reset = function () {
        $scope.card = {};
    }
}]);

app.controller('DiscountCardsCtrl', ['$scope', '$location', 'discountCards', 'CardsCtrl', '$filter', function ($scope, $location, discountCards, CardsCtrl, $filter) {
    discountCards.$promise.then(function (data) {
        $scope.discountCards = data.content;
        $scope.totalItems = data.total_elements;
    });

    $scope.initialSort = {
        direction: 'DESC',
        property: 'createdDate'
    };

    $scope.head = [
        {name: 'Card #', property: 'card_number', width: '10%'},
        {name: 'Company', property: 'company_name', width: '20%'},
        {name: 'Discount', property: 'amount_of_discount', width: '7%'},
        {name: 'Description', property: 'description', width: '25%'},
        {name: 'Created', property: 'created_date', width: '10%'},
        {name: 'Expired', property: 'expired_date', width: '10%'},
        {name: 'Available', property: 'available', width: '10%'}
    ];

    $scope.getData = function (pageable) {
        CardsCtrl.get(pageable).$promise.then(function (data) {
            $scope.discountCards = data.content;
            $scope.totalItems = data.total_elements;
        });
    };
    $scope.setData = function (dIndex, dcIndex) {
        if($scope.head[dIndex].property == 'created_date' || $scope.head[dIndex].property == 'expired_date') {
            return $filter('dateFilter')($scope.discountCards[dcIndex][$scope.head[dIndex].property])
        } else if($scope.head[dIndex].property == 'amount_of_discount') {
            return $scope.discountCards[dcIndex][$scope.head[dIndex].property] + " %"
        } else {
            return $scope.discountCards[dcIndex][$scope.head[dIndex].property]
        }
    }

}]);

app.controller('SearchCtrl', ['$scope', 'DiscountCardSearchFactory', '$sessionStorage', '$location', function($scope, DiscountCardSearchFactory, $sessionStorage, $location) {
    $scope.ENTER_BUTTON = 13;
    $scope.searchData = {};
    $scope.search = function() {
        $scope.$storage = $sessionStorage.$default({
            cardId: $scope.searchData.selected[0]
        });
        $location.path('/card/info');
    };
    $scope.refreshData = function(data) {
        if(data != "") {
            DiscountCardSearchFactory.searchByName({company_name: data}).$promise.then(
                function(data) { $scope.companies = data; },
                function() { $scope.companies = []; })
        } else {
            $scope.companies = []
        }
    }
}]);

app.controller('DiscountCardInfoCtrl', ['$scope', 'discountCardInfo', 'PersonFactory', 'auth', function($scope, discountCardInfo, PersonFactory, auth) {
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

}]);