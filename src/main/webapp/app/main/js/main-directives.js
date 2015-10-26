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

app.directive('ensureUnique', ["$http", "$location", function($http, $location) {
    var toId;
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elem, attr, ctrl) {
            var data;
            scope.$watch(attr.ngModel, function(value, oldValue) {
                if(!_.isEqual(value, oldValue) && value != oldValue && !_.isUndefined(value) && value != 0 && value != "" && value != null) {
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
        template: '<small class="help-block error-info" ng-repeat="message in errorMessages" ng-show= "!modelController.$pristine && $first" class="warning">{{message}}</small>'
    }
});

app.directive('contentTable',
    function(deviceCheck, $filter) {
        return {
            restrict: 'E',
            scope: {
                sectionId: '@',
                tableInfo: '='
            },
            controller: '@',
            name: "controllerName",
            link: function(scope, element, attr) {
                if(scope.tableInfo.data.content.length == 0) {
                    angular.element("#" + scope.sectionId).hide();
                }
                scope.currentPage = 1;
                scope.maxSize = 5;
                scope.pageSize = [
                    {label: '15', value: 15},
                    {label: '25', value: 25},
                    {label: '50', value: 50},
                    {label: '100', value: 100}
                ];
                scope.currentPageSize = scope.pageSize[0];
                scope.tableData = scope.tableInfo.data.content;
                scope.totalItems = scope.tableInfo.data.totalElements;
                var tabletLandscapeWidth = 960,
                    currentWidth = window.outerWidth,
                    sortInfo = scope.tableInfo.data.sort[0],
                    dataSorting = [],
                    pageable = {
                        direction: sortInfo.direction,
                        property: sortInfo.property,
                        size: scope.currentPageSize.value,
                        page: scope.currentPage - 1
                    };
                scope.tabletLandscape = currentWidth >= tabletLandscapeWidth;
                scope.mobileDevice = deviceCheck.mobileDevice;

                var getData = function(pageable) {
                    scope.tableInfo.factory.get(pageable).$promise.then(function(data) {
                        scope.tableData = data.content;
                        scope.totalItems = data.totalElements;
                    });
                };

                function getNestedData(dataIndex, property) {
                    var nestedProperties = property.split(".");
                    var data = scope.tableData[dataIndex][nestedProperties[0]];
                    _.each(nestedProperties, function(property, index) {
                        if(index != 0) {
                            data = data[property]
                        }
                    });
                    return data;
                }

                scope.setData = function (headDataIndex, dataIndex) {
                    var data = "";
                    var contains = _.some(scope.tableInfo.filteredProperties, function(value) {
                        if(scope.tableInfo.head[headDataIndex].property == value.property) {
                            if(value.filter) {
                                if(value.property.indexOf('phoneNumber') > -1) {
                                    if(value.property.indexOf('.') > -1) {
                                        data = value.filter(getNestedData(dataIndex, value.property), getNestedData(dataIndex, value.property.split(".")[0].concat(".location.country")))
                                    } else {
                                        data = value.filter(scope.tableData[dataIndex][scope.tableInfo.head[headDataIndex].property], scope.tableData[dataIndex].location.country);
                                    }
                                } else {
                                    data = value.filter(scope.tableData[dataIndex][scope.tableInfo.head[headDataIndex].property]);
                                }
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
                        if(scope.tableInfo.head[headDataIndex].property.indexOf(".") > -1) {
                            data = getNestedData(dataIndex, scope.tableInfo.head[headDataIndex].property);
                        } else {
                            data = scope.tableData[dataIndex][scope.tableInfo.head[headDataIndex].property];
                        }
                    }
                    return data;
                };

                _.each(scope.tableInfo.head, function (data) {
                    data.direction = 'DESC'
                });

                scope.$watch("head", function () {
                    Array.prototype.push.apply(dataSorting, scope.tableInfo.head);
                });

                scope.selectSort = function (property) {
                    if (pageable.property == property) {
                        if (pageable.direction != 'DESC') {
                            return 'glyphicon glyphicon-sort-by-alphabet'
                        } else {
                            return 'glyphicon glyphicon-sort-by-alphabet-alt'
                        }
                    } else {
                        return ''
                    }
                };

                /* Functions to get new data */
                scope.changePageSize = function(value) {
                    pageable.size = value;
                    getData(pageable);
                };
                scope.changePage = function() {
                    pageable.page = scope.currentPage - 1;
                    getData(pageable);
                };
                scope.sorting = function(type){
                    if(pageable.property == type) {
                        pageable.direction = pageable.direction == 'ASC' ? 'DESC' : 'ASC';
                    } else {
                        pageable.property = type;
                        pageable.direction = 'DESC';
                    }
                    getData(pageable);
                };

            },
            templateUrl: 'app/main/data-table-template.html'
        }
    }
);

app.directive('compileHtml', function($parse, $sce, $compile) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            var expression = $sce.parseAsHtml(attr.compileHtml);

            var getResult = function() {
                return expression(scope);
            };

            scope.$watch(getResult, function(newValue) {
                var linker = $compile(newValue);
                element.append(linker(scope))
            })
        }
    }
});

app.directive('uiSelect', function (){
    return {
        restrict: 'EA',
        require: 'uiSelect',
        link: function($scope, $element, $attributes, ctrl) {
            $scope.$select.limit = (angular.isDefined($attributes.limit)) ? parseInt($attributes.limit, 10) : undefined;
            var superSelect = ctrl.select;
            ctrl.select = function() {
                if(ctrl.multiple && ctrl.limit !== undefined && ctrl.selected.length >= ctrl.limit) {
                    console.log("booooo");
                } else {
                    superSelect.apply(ctrl, arguments);
                    if(ctrl.multiple && ctrl.limit !== undefined && ctrl.selected.length >= ctrl.limit) {
                        $(".ui-select-dropdown").addClass('hide');
                    }
                }
            };
        }
    }
});

app.directive('uiSelectMultiple', function () {
    return {
        restrict: 'EA',
        require: 'uiSelectMultiple',
        link: function($scope, $element, $attributes, ctrl) {
            var superRemove = ctrl.removeChoice;
            ctrl.removeChoice = function() {
                console.log($scope.$select.limit);
                console.log($scope.$select.selected.length);
                if($scope.$select.limit !== undefined && $scope.$select.selected.length >= $scope.$select.limit) {
                    $(".ui-select-dropdown").removeClass('hide');
                }
                superRemove.apply(ctrl, arguments);
            }
        }
    }
});


app.directive('capsLock', function($compile) {
    return {
        restrict: 'A',
        link: function(scope, element, attributes, ctrl) {
            scope.isMacintosh = window.navigator.appVersion.indexOf('Macintosh') > -1;
            var el = '<span class="caps form-control-feedback" ng-show="isCapsLockOn &&' + attributes.ngFocus.toString().split(" ")[0] + '&& !isMacintosh"></span>';
            element.after(el);
            $compile(angular.element('.caps'))(scope);
        }
    }
});

app.directive('boolean', function() {
    return {
        restrict: 'E',
        replace: true,
        scope: {
            value: '@'
        },
        link: function(scope, element, attr) {
            if(scope.value == 'true') {
                element.append('<span class="picked glyphicon glyphicon-ok"></span>')
            } else {
                element.append('<span class="not-picked glyphicon glyphicon-remove"></span>')
            }
        }
    }
});

app.directive('adminNav', function() {
    var controller = ['$scope', 'CustomTagFactory', function($scope, CustomTagFactory) {
        CustomTagFactory.get({admin: 'admin', count: 'count'}, function (data) {
            $scope.unacceptedTags = data.count;
        });
    }];
    return {
        replace: true,
        controller: controller,
        templateUrl: 'app/main/admin-navigation.html'
    }
});