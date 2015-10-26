var app = angular.module('discount-card-directives', []);

app.directive('cardSearch', ['DiscountCardSearchFactory', function(DiscountCardSearchFactory) {
    var search;
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, ctrl) {
            scope.$watch(attrs.ngModel, function(newValue, oldValue) {
                if(!_.isEqual(newValue, oldValue) && newValue != oldValue && newValue != "") {
                    if(search) clearTimeout(search);
                    if(!ctrl.$pristine) {
                        search = setTimeout(function() {
                            DiscountCardSearchFactory.searchByName({companyName: newValue}).$promise.then(
                                function(data) {
                                    scope.companies = data;
                                },
                                function(data) {
                                    scope.companies = [];
                                }
                            )
                        }, 1000)

                    }
                } else if (newValue == "") {
                    scope.companies = []
                }
            })
        }
    }
}]);

app.directive('numberValidator', [function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, modelCtrl) {
            if(_.isUndefined(modelCtrl.$validators.min)) {
                throw new DOMException("Min input attribute is required")
            }
            var minNumber = modelCtrl.$validators.min.length;
            modelCtrl.$parsers.push(function(value) {
                if(value < minNumber) {
                    modelCtrl.$setViewValue();
                    modelCtrl.$render();
                }
                return value;
            })
        }
    }
}]);