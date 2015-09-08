var app = angular.module('DC-controllers', ['ngResource']);
app.controller('AddCtrl', ['$scope', '$location', 'addition', function ($scope, $location, addition) {
    $scope.reg = function () {
        AddCtrl.addition($scope.card,
            function (data) {
                alert(data.success);
                $location.path('/');
            }, function (data) {
                $scope.error = data.error;
            }
        )
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