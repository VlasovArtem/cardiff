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

app.controller('NavCtrl', ['$scope', 'auth', '$timeout', function($scope, auth, $timeout) {
    $scope.authenticated = function () {
        return auth.authenticated;
    };
    $scope.login = function() {
        auth.authenticate($scope.person, function(error) {
            errorFn(error);
        });
    };
    $scope.logout = function() {
        auth.clear();
    };
    var errorFn = function(message) {
        $scope.error = message;
        $timeout(function() {
            $scope.error = null;
        }, 2000);
    };
}]);
app.controller('AccountCtrl', ['$scope', '$location', 'personData', 'changePassword', '$timeout', function($scope, $location, personData, changePassword, $timeout) {
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
    };
    $scope.changeData = function() {
        $location.path('/account/update')
    }
}]);
app.controller('UpdateAccountCtrl', ['$scope', 'personData', '$location', 'updatePerson', function($scope, personData, $location, updatePerson) {
    personData.$promise.then(
        function(person) {
            $scope.changedPerson = person;
            $scope.data = angular.copy(person);
        }
    );
    $scope.personIsEquals = function() {
        if($scope.data != undefined && $scope.changedPerson != undefined) {
            return _.every(["name", "login", "email", "phone_number"], function(data) {
                return _.isEqual($scope.data[data], $scope.changedPerson[data]) || $scope.data[data] == $scope.changedPerson[data]
            });
        }
        return true;
    };
    $scope.update = function() {
        console.log("Inside update");
        updatePerson.updatePerson($scope.changedPerson,
            function() {
                alert('Person data successfully changed');
                $location.path('/account');
            },
            function(data) {
                $scope.error = data.error;
            })
    };
    $scope.reset = function() {
        $scope.changedPerson = angular.copy($scope.data);
    }
}]);