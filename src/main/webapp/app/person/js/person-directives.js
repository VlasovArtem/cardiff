/**
 * Created by artemvlasov on 22/08/15.
 */
var app = angular.module('person-directives', []);
app.directive('validationMessages', function () {
    return {
        scope: {
            modelController: '='
        },
        restrict: 'EA',
        link: function (scope, elm, attrs) {
            if (!scope.modelController) {
                console.log('Requires a html attribute data-model-controller. This should point to the input field model controller.');
            }
            scope.$watch('modelController.$error', function (newValue) {
                if (newValue) {
                    scope.errorMessages = [];
                    angular.forEach(newValue, function (value, key) {
                        if (value && attrs[key + 'Error']) {
                            scope.errorMessages.push(attrs[key + 'Error']);
                        }
                    });
                }
            }, true);
        },
        template: '<small class="help-block test" ng-repeat="message in errorMessages" ng-show= "!modelController.$pristine && $first" class="warning">{{message}}</small>'
    }
});
app.directive('ensureUnique', ["$http", function($http) {
    var toId;
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elem, attr, ctrl) {
            scope.$watch(attr.ngModel, function(value) {
                ctrl.$setValidity('unique', null);
                if(toId) clearTimeout(toId);
                if(!ctrl.$pristine) {
                    toId = setTimeout(function () {
                        $http.get('/rest/persons/check/' + attr.name
                            + '?' + attr.name + '=' + value)
                            .success(function () {
                                ctrl.$setValidity('unique', true);
                            }).error(function() {
                                ctrl.$setValidity('unique', false);
                            });

                    }, 1000);
                }
            })
        }
    }
}]);