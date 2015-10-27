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

app.directive('discountCardSearch', function($sessionStorage, $timeout) {
    return {
        replace: true,
        link: function(scope, element, attr) {
            if($sessionStorage.search) {
                scope.search = $sessionStorage.search;
            } else {
                scope.search = {
                    tags: [],
                    companyName: null
                };
            }
            scope.requestElements = {
                search : 'search'
            };
            scope.resetSearch = function() {
                scope.customSearch = false;
                scope.search = {
                    tags: [],
                    companyName: null
                };
                if($sessionStorage.search) {
                    scope.infoMessage = "Search information successfully deleted!";
                    timeout = $timeout(messageCount, 1000);
                    delete $sessionStorage.search;
                }
                scope.getData();
            };
            scope.chooseSearchTag = function(tag) {
                scope.search.tags.push(tag);
                scope.discountCardSearch();
            };
            scope.discountCardSearch = function() {
                scope.customSearch = scope.search.tags.length > 0 || scope.search.companyName != null || scope.search.companyName != "";
                scope.getData();
            };
            var timeout;
            scope.saveSearch = function() {
                $sessionStorage.search = scope.search;
                scope.infoMessage = "Search information successfully saved!";
                timeout = $timeout(messageCount, 1000);
            };
            var count = 0;
            var messageCount = function() {
                if(count == 5) {
                    scope.infoMessage = null;
                    count = 0;
                    $timeout.cancel(timeout);
                } else {
                    count++;
                    timeout = $timeout(messageCount, 1000);
                }
            };
        }
    }
});

app.directive('tagSearch', function(TagFactory) {
    return {
        replace: true,
        restrict: 'C',
        link: function(scope, element, attr) {
            scope.tags = TagFactory.query();
        },
        templateUrl: '/app/discount-card/discount-card-search.html'
    }
});