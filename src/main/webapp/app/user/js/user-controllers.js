var app = angular.module('user-controllers', []);
app.controller('SignInCtrl', ['$scope', '$location', 'Login', function($scope, $location, Login) {
    $scope.login = function() {
        Login.login($.param({
            login_data : $scope.user.login_data,
            password : $scope.user.password
        }), function(data) {
            alert("Welcome, " + data.login);
            $location.path('/')
        }, function(data) {
            alert(data.data.error);
            console.log(data);
            $scope.user.password = "";
        })
    }
}]);
app.controller('SignUpCtrl', ['$scope', '$location', 'SignUp', function($scope, $location, SignUp) {
    $scope.reg = function() {
        SignUp.registration($scope.user,
            function(data) {
                alert(data.success);
                $location.path('/');
            }, function(data) {
                $scope.error = data.error;
            }
        )
    };
    $scope.reset = function() {
        $scope.user = {};
    }
}]);
app.controller('MainCtrl', ['$scope', function($scope) {
    $scope.init = function() {

    }
}]);