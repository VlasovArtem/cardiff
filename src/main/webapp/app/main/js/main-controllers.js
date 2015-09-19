/**
 * Created by artemvlasov on 17/09/15.
 */
var app = angular.module('main-controllers', []);

app.controller('NavCtrl', ['$scope', 'auth', '$timeout', '$route',
    function($scope, auth) {
    $scope.auth = auth;
    $scope.adminPermission = function() {
        return auth.admin;
    };
    $scope.authenticated = function () {
        return auth.authenticated;
    };
    $scope.login = function() {
        auth.authenticate($scope.person, function(error) {
            if(error) {
                $scope.error = error;
                $scope.person.password = null;
            }
        });
    };
    $scope.logout = function() {
        auth.clear();
    };

}
]);

app.controller('SearchCtrl', ['$scope', 'DiscountCardSearchFactory', '$sessionStorage', '$location',
    function($scope, DiscountCardSearchFactory, $sessionStorage, $location) {
    $scope.ENTER_BUTTON = 13;
    $scope.searchData = {};
    $scope.search = function() {
        $sessionStorage.cardId = $scope.searchData.selected[0];
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
}
]);