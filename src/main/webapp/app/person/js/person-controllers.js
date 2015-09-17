var app = angular.module('person-controllers', ['ngResource']);
app.controller('SignUpCtrl', ['$scope', '$location', 'SignUp', 'auth', 'locations',
    function($scope, $location, SignUp, auth, locations) {
        $scope.reg = function() {
            SignUp.registration($scope.person,
                function() {
                    $scope.login = {
                        loginData: $scope.person.email,
                        password: $scope.person.password
                    };
                    auth.authenticate($scope.login, function(error) {
                        $scope.error = error;
                    });
                }, function(data) {
                    $scope.person.password = null;
                    $scope.error = data.data;

                }
            )
        };
        $scope.locations = locations;
        $scope.reset = function() {
            $scope.person = {};
        };
        $scope.emailPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\\.[a-z0-9-]+)+"
    }
]);

app.controller('AccountCtrl', ['$scope', '$location', 'personData', 'changePassword', '$timeout',
    function($scope, $location, personData, changePassword, $timeout) {
        $scope.person = personData;
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
        };
        $scope.changeData = function() {
            $location.path('/account/update')
        };
        $scope.location = $location.path == '/account';
    }
]);

app.controller('UpdateAccountCtrl', ['$scope', 'personData', '$location', 'PersonFactory', 'locations',
    function($scope, personData, $location, PersonFactory, locations) {
        $scope.changedPerson = personData;
        $scope.data = angular.copy(personData);
        $scope.personIsEquals = function() {
            if($scope.data != undefined && $scope.changedPerson != undefined) {
                return _.every(["name", "login", "email", "phone_number", "location", "description"], function(data) {
                    return _.isEqual($scope.data[data], $scope.changedPerson[data]) || $scope.data[data] == $scope.changedPerson[data]
                });
            }
            return true;
        };
        $scope.update = function() {
            PersonFactory.updatePerson($scope.changedPerson,
                function() {
                    alert('Person data successfully changed');
                    if($scope.data.$resolved) {
                        $location.path('/account');
                    } else {
                        $location.path('/admin/persons');
                    }
                },
                function(data) {
                    $scope.error = data.data;
                })
        };
        $scope.locations = locations;
        $scope.reset = function() {
            $scope.changedPerson = angular.copy($scope.data);
        }
    }
]);

app.controller('AdminPersonsCtrl', ['$scope', '$location', '$filter', '$route', 'persons', 'AdminPersonFactory', 'UpdatePerson', 'deviceCheck',
    function($scope, $location, $filter, $route, persons, AdminPersonFactory, UpdatePerson, deviceCheck) {
        var tabletLandscapeWidth = 960;
        var currentWidth = window.outerWidth;
        $scope.tabletLandscape = currentWidth >= tabletLandscapeWidth;
        $scope.mobileDevice = deviceCheck.mobileDevice;
        persons.$promise.then(function(data) {
            $scope.persons = data.content;
            $scope.totalItems = data.total_elements;
        });
        $scope.initialSort = {
            direction: 'DESC',
            property: 'createdDate'
        };
        $scope.head = [
            {name : 'Name', property : 'name', width: '15%'},
            {name : 'Login', property: 'login', width: '10%'},
            {name : 'Email', property: 'email', width: '18%'},
            {name : 'Phone', property: 'phone_number', width: '15%'},
            {name : 'Role', property: 'role', width: '7%'},
            {name : 'Created', property: 'created_date', width: '10%'},
            {name : 'Deleted', property: 'deleted', width: '7%'}
        ];
        $scope.removePerson = function(id) {
            AdminPersonFactory.remove({delete: 'delete', id : id},
                function(data) {
                    if(data.info) {
                        $location.path('/');
                    }
                    $route.reload()
                });
        };
        $scope.editPerson = function(person) {
            UpdatePerson.setPerson(person);
            $location.path('/account/update');
        };
        $scope.restorePerson = function(personId) {
            AdminPersonFactory.restore({id: personId}, function() {$route.reload()});
        };
        $scope.changeRole = function(personId) {
            AdminPersonFactory.updateRole({id: personId}, function() {$route.reload()});
        };
        $scope.getData = function(pageable) {
            AdminPersonFactory.getAll(pageable).$promise.then(function(data) {
                $scope.persons = data.content;
                $scope.totalItems = data.total_elements;
            });
        };
        $scope.setData = function(dIndex, pIndex) {
            if($scope.head[dIndex].property == 'created_date') {
                return $filter('dateFilter')($scope.persons[pIndex][$scope.head[dIndex].property])
            } else if($scope.head[dIndex].property == 'phone_number') {
                return $filter('phoneNumberFilter')($scope.persons[pIndex][$scope.head[dIndex].property])
            } else {
                return $scope.persons[pIndex][$scope.head[dIndex].property]
            }
        }
    }
]);