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
                            DiscountCardSearchFactory.searchByName({company_name: newValue}).$promise.then(
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