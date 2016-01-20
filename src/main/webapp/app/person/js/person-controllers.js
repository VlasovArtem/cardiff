var app = angular.module('person-controllers', ['ngResource']);
app.controller('SignUpCtrl', ['$scope', '$location', 'SignUp', 'auth', 'locations', '$route',
    function($scope, $location, SignUp, auth, locations, $route) {
        $scope.isMacintosh = window.navigator.appVersion.indexOf('Macintosh') > -1;
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
            $route.reload();
        };
        $scope.emailPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\\.[a-z0-9-]+)+";
        $scope.changeMask = function(country) {
            if(angular.isDefined(country)) {
                $scope.mask = $scope.phoneNumberInfo[country].mask;
                $scope.person.phoneNumber = null;
            }
        };
    }
]);

app.controller('AccountCtrl', ['$scope', '$location', 'personData', 'changePassword', '$filter', '$sessionStorage', '$route',
    function($scope, $location, personData, changePassword, $filter, $sessionStorage, $route) {
        $scope.person = personData;

        $scope.changePassword = function() {
            if(!$scope.personInfo.oldPassword || !$scope.personInfo.newPassword) {
                $scope.errorFn('New or old password cannot be null');
            } else if(_.isEqual($scope.personInfo.oldPassword, $scope.personInfo.newPassword)) {
                $scope.errorFn('Old and new password is equals');
            } else {
                changePassword.change($.param({
                    oldPassword: $scope.personInfo.oldPassword,
                    newPassword: $scope.personInfo.newPassword
                }), function() {
                    alert('Password successfully changed');
                    $route.reload();
                }, function(data) {
                    $scope.errorFn(data.data.error);
                })
            }
        };
        $scope.errorFn = function(message) {
            $scope.error = message;
            $scope.personInfo = null;
        };
        $scope.changeData = function() {
            delete $sessionStorage.updatedPerson;
            $location.path('/account/update')
        };
        $scope.location = $location.path == '/account';
    }
]);

app.controller('UpdateAccountCtrl', ['$scope', 'personData', '$location', 'PersonFactory', 'locations', 'PersonUpdateFactory', '$sessionStorage',
    function($scope, personData, $location, PersonFactory, locations, PersonUpdateFactory, $sessionStorage) {
        $scope.phoneNumberInfo = {
            Ukraine: {
                code: '+380', mask: '(S4) 444-44-44'},
            Russia : {
                code: '+7', mask: '(S44) 444-44-44'},
            'United States' : {
                code: '+1', mask: '(S44) 444-44-44'},
            Bulgaria : {
                code: '+359', mask: '(S4) 444-44-44'}
        };
        $scope.uiMaskOptions = {
            maskDefinitions : {'4': /\d/, 'S': /[1-9]/}
        };
        var wipeFormInfo = function () {
            var removableClasses = ["has-error", "has-warning", "has-success"];
            var removableElements = [".form-control-feedback", ".help-block"];
            _.each(removableElements, function(value) {
                _.each(angular.element.find(value), function(elem) {
                    elem.remove();
                })
            });
            _.each(angular.element.find('div.has-feedback'), function (elem) {

                _.each(removableClasses, function(removableClass) {
                    angular.element(elem).removeClass(removableClass);
                });
            });
            _.each($scope.updateFrom, function(value) {
                if(angular.isDefined(value)) {
                    if(value.$name) {
                        value.$setPristine();
                        value.$render();
                    }
                }
            });
        };
        $scope.mask = "";
        $scope.changedPerson = personData;
        $scope.data = angular.copy(personData);
        $scope.personIsEquals = function() {
            if($scope.data != undefined && $scope.changedPerson != undefined) {
                return _.every(["name", "login", "email", "phoneNumber", "location", "description", "skype"], function (data) {
                    return _.isEqual($scope.data[data], $scope.changedPerson[data]) || $scope.data[data] == $scope.changedPerson[data]
                });
            }
            return true;
        };
        $scope.changeMask = function(country) {
            if($scope.updateForm) {
                $scope.updateForm.phoneNumber.$setPristine();
            }
            if(angular.isDefined(country)) {
                $scope.mask = $scope.phoneNumberInfo[country].mask;
                if ($scope.data.location.country != $scope.changedPerson.location.country) {
                    $scope.changedPerson.phoneNumber = null;
                } else {
                    $scope.changedPerson.phoneNumber = $scope.data.phoneNumber;
                }
            }
        };
        $scope.changeMask($scope.changedPerson.location.country);
        $scope.update = function() {
            PersonUpdateFactory.updatePerson($scope.changedPerson,
                function() {
                    alert('Person data successfully changed');
                    if($scope.data.$resolved) {
                        $location.path('/account');
                    } else {
                        delete $sessionStorage.updatedPerson;
                        $location.path('/admin/persons');
                    }
                },
                function(data) {
                    $scope.error = data.data;
                })
        };
        $scope.locations = locations;
        $scope.reset = function() {
            wipeFormInfo();
            $scope.changedPerson = angular.copy($scope.data);
        };
    }
]);

app.controller('AdminPersonsCtrl', ['$scope', '$location', '$filter', '$route', 'persons', 'AdminPersonFactory', 'AdminPersonTableFactory', '$sce',
    function($scope, $location, $filter, $route, persons, AdminPersonFactory, AdminPersonTableFactory, $sce) {
        $scope.tableInfo = {
            data: persons,
            dataTemplate: 'app/person/table-data-template.html',
            factory: AdminPersonTableFactory,
            head: [
                {name : 'Name', property : 'name', width: '7%'},
                {name : 'Login', property: 'login', width: '7%'},
                {name : 'Email', property: 'email', width: '20%'},
                {name : 'Phone', property: 'phoneNumber', width: '15%'},
                {name : 'Skype', property: 'skype', width: '15%'},
                {name : 'Role', property: 'role', width: '7%', class: 'center'},
                {name : 'Created', property: 'createdDate', width: '10%'}
            ],
            filteredProperties: [
                {property: 'createdDate', filter: $filter('dateFilter')},
                {property: 'phoneNumber', filter: $filter('phoneNumberFilter')}
            ],
            dataButtons : {desktop: 'app/person/table-buttons-desktop.html',
                mobile: 'app/person/table-buttons-mobile.html'},
            htmlBinding: [{
                head: 'Deleted',
                sorted: true,
                property: 'deleted',
                width: '7%',
                desktopClass: 'center',
                desktop: $sce.trustAsHtml('<boolean value="{{data.deleted}}"></boolean>')
            }]
        };


    }
]);

app.controller('AdminPersonFunctionCtrl', ['$scope', '$sessionStorage', '$location', '$route', 'AdminPersonFactory', function($scope, $sessionStorage, $location, $route, AdminPersonFactory) {
    $scope.removePerson = function(id) {
        AdminPersonFactory.remove({delete: 'delete', id : id},
            function() {
                $route.reload()
            });
    };

    $scope.editPerson = function(person) {
        $sessionStorage.updatedPerson = person;
        $location.path('/account/update');
    };

    $scope.restorePerson = function(personId) {
        AdminPersonFactory.restore({id: personId}, function() {$route.reload()});
    };

    $scope.changeRole = function(personId) {
        AdminPersonFactory.updateRole({id: personId}, function() {$route.reload()});
    };
}]);