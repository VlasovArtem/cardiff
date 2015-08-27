var app = angular.module('person-controllers', ['ngResource']);
app.controller('SignUpCtrl', ['$scope', '$location', 'SignUp', function($scope, $location, SignUp) {
    $scope.reg = function() {
        SignUp.registration($scope.person,
            function(data) {
                alert(data.success);
                $location.path('/');
            }, function(data) {
                $scope.error = data.error;
            }
        )
    };
    $scope.reset = function() {
        $scope.person = {};
    }
}]);
app.controller('NavCtrl', ['$scope', 'auth', function($scope, auth) {
    $scope.authenticated = function () {
        return auth.authenticated;
    };
    $scope.login = function() {
        auth.authenticate($scope.person, function(authenticated) {
            if(authenticated) {

            }
        });
    };
    $scope.logout = function() {
        auth.clear();
    }
}]);
app.controller('AccountCtrl', ['$scope', 'personData', 'changePassword', '$timeout', '$resource', function($scope, personData, changePassword, $timeout, $resource) {
    $scope.person = personData;
    $scope.ignoredKeys = ['discount_cards', 'id', 'created_date', 'role'];
    $scope.changePassword = function() {
        if(!$scope.data.oldPassword || !$scope.data.newPassword) {
            $scope.errorFn('New or old password cannot be null');
        } else if(_.isEqual($scope.data.oldPassword, $scope.data.newPassword)) {
            $scope.errorFn('Old and new password is equals');
        } else {
            changePassword.change($.param({
                oldPassword: $scope.data.oldPassword,
                newPassword: $scope.data.newPassword
            }), function() {
                alert('Password successfully changed');
                $scope.data = null;
            }, function(data) {
                $scope.errorFn(data.error);
                $scope.data = null;
            })
        }
    };
    $scope.errorFn = function(message) {
        $scope.error = message;
        $scope.data = null;
        $timeout(function() {
            $scope.error = null;
        }, 2000);
    }
}]);