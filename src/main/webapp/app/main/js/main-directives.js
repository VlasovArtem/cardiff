var app = angular.module('main-directives', []);

app.directive('windowHeight', function() {
    return {
        restrict: 'A',
        compile: function(element, attr) {
            var navBarHeight = 100;
            element.css('min-height', window.outerHeight - navBarHeight);
        }
    }
});

app.directive('entitySorting', function($filter) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            _.each(scope.tableInfo.head, function(data) {
                data.direction = 'DESC'
            });
            var dataSorting = [];
            scope.$watch("head", function() {
                Array.prototype.push.apply(dataSorting, scope.tableInfo.head);
            });
            scope.currentPage = 1;
            scope.maxSize = 5;
            scope.pageSize = [
                {label: '15', value: 15},
                {label: '25', value: 25},
                {label: '50', value: 50},
                {label: '100', value: 100}
            ];
            scope.currentPageSize = scope.pageSize[0];
            var pageable = {
                direction: scope.initialSort.direction,
                property: scope.initialSort.property,
                size: scope.currentPageSize.value,
                page: scope.currentPage - 1
            };
            scope.$watch(
                "currentPageSize.value",
                function(newValue, oldValue) {
                    if(newValue === oldValue) return;
                    pageable.size = newValue;
                    scope.getData(pageable);
                }
            );
            scope.changePage = function() {
                pageable.page = scope.currentPage - 1;
                scope.getData(pageable);
            };
            scope.sorting = function(type){
                if(pageable.property == $filter('camelCase')(type)) {
                    pageable.direction = pageable.direction == 'ASC' ? 'DESC' : 'ASC';
                } else {
                    pageable.property = $filter('camelCase')(type);
                    pageable.direction = 'DESC';
                }
                scope.getData(pageable);
            };
            scope.selectSort = function(index) {
                if(pageable.property == $filter('camelCase')(scope.tableInfo.head[index].property)) {
                    if(pageable.direction != 'DESC') {
                        return 'glyphicon glyphicon-sort-by-alphabet'
                    } else {
                        return 'glyphicon glyphicon-sort-by-alphabet-alt'
                    }
                } else {
                    return ''
                }
            };
        }
    }
});

app.directive('ensureUnique', ["$http", "$location", function($http, $location) {
    var toId;
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elem, attr, ctrl) {
            scope.$watch(attr.ngModel, function(value, oldValue) {
                if(!_.isEqual(value, oldValue) && value != oldValue && !_.isUndefined(value) && value != 0 && value != "") {
                    ctrl.$setValidity('unique', null);
                    if (toId) clearTimeout(toId);
                    if (!ctrl.$pristine) {
                        toId = setTimeout(function () {
                            var attributeName = attr.name == "phoneNumber" ? "phone" : attr.name;
                            if ($location.path() == '/account/update' && _.isEqual(scope.data[attributeName], value)) {
                                ctrl.$setValidity('unique', true);
                            } else {
                                $http.post('/rest/person/check/' + attributeName
                                    + '?' + attributeName + '=' + value)
                                    .success(function () {
                                        ctrl.$setValidity('unique', true);
                                    }).error(function () {
                                        ctrl.$setValidity('unique', false);
                                    });

                            }
                        }, 1000);
                    }
                }
            })
        }
    }
}]);

app.directive('formError', function() {
    return {
        scope: {
            error: '=',
            form: '='
        },
        restrict: 'E',
        link: function(scope, elm, attrs) {
            scope.$watch('error', function(newValue) {
                if(newValue) {
                    scope.form.$error = {
                        pattern: true
                    };
                    scope.form.$invalid = true;
                }
            }, true)
        },
        template: '<small class="help-block" ng-show="error" ng-bind="error"></small>'
    }
});

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

app.directive('contentTable',
    function(deviceCheck) {
        return {
            restrict: 'E',
            link: function(scope, element, attr) {
                var tabletLandscapeWidth = 960;
                var currentWidth = window.outerWidth;
                scope.tabletLandscape = currentWidth >= tabletLandscapeWidth;
                scope.mobileDevice = deviceCheck.mobileDevice;

                scope.getData = function(pageable) {
                    scope.tableInfo.factory.getAll(pageable).$promise.then(function(data) {
                        scope.tableData = data.content;
                        scope.totalItems = data.total_elements;
                    });
                };

                scope.tableInfo.data.$promise.then(
                    function(data) {
                        scope.tableData = data.content;
                        scope.totalItems = data.total_elements;
                    }
                );

                scope.setData = function (headDataIndex, dataIndex) {
                    var data = "";
                    var contains = _.some(scope.tableInfo.filteredProperties, function(value) {
                        if(scope.tableInfo.head[headDataIndex].property == value.property) {
                            if(value.filter) {
                                data = value.filter(scope.tableData[dataIndex][scope.tableInfo.head[headDataIndex].property]);
                                return true;
                            } else if (value.appender) {
                                if(_.isNumber(scope.tableData[dataIndex][scope.tableInfo.head[headDataIndex].property])) {
                                    if(scope.tableData[dataIndex][scope.tableInfo.head[headDataIndex].property] == 0) {
                                        data = 'n/a';
                                    } else {
                                        data = scope.tableData[dataIndex][scope.tableInfo.head[headDataIndex].property] + value.appender;
                                    }
                                    return true;
                                }
                                data = scope.tableData[dataIndex][scope.tableInfo.head[headDataIndex].property] + value.appender;
                                return true;
                            }
                        }
                    });
                    if(!contains) {
                        data = scope.tableData[dataIndex][scope.tableInfo.head[headDataIndex].property];
                    }
                    return data;
                }
            },
            templateUrl: 'app/main/data-table-template.html'
        }
    }
);