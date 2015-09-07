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

app.controller('DiscountCardsCtrl', ['$scope', '$location', 'discountCards', 'getAll', function ($scope, $location, discountCards, getAll) {
    discountCards.$promise.then(function (data) {
        $scope.discountCards = data.content;
        $scope.totalItems = data.total_elements;
    });

    $scope.head = [
        {name: 'Card #', property: 'card_number', width: '15%'},
        {name: 'Available', property: 'available', width: '10%'},
        {name: 'Company', property: 'company_name', width: '18%'},
        {name: 'Amount', property: 'amount_of_discount', width: '15%'},
        {name: 'Description', property: 'description', width: '5%'},
        {name: 'Created', property: 'created_date', width: '10%'},
        {name: 'Expired', property: 'expired_date', width: '10%'},
    ];

    $scope.getData = function (pageable) {
        getAll.get(pageable).$promise.then(function (data) {
            $scope.discountCards = data.content;
            $scope.totalItems = data.total_elements;
        });
    };
    $scope.setData = function (dIndex, dcIndex) {

        return $scope.discountCards[dcIndex][$scope.head[dIndex].property]
    }

}]);