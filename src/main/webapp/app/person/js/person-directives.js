var app = angular.module('person-directives', []);

app.directive('accountData',
    function($filter) {
        return {
            restrict: 'A',
            scope: {
                label: '=',
                data: '=',
                country: '='
            },
            link: function(scope, element, attrs) {
                if(scope.label == 'phone_number') {
                    scope.data = $filter('phoneNumberFilter')(scope.data, scope.country)
                } else if(scope.label == 'location') {
                    scope.data = scope.data.city + ', ' + scope.data.country;
                }
                scope.label = $filter('accountFilter')(scope.label);
            },
            template: '<td ng-bind="label" class="key"></td>' +
            '<td ng-bind="data"></td>'

        }
    }
);

app.directive('phoneNumberMask',
    function() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs, ctrl) {
                scope.uiMaskOptions = {
                    maskDefinitions : {'4': /\d/, 'S': /[1-9]/}
                };
                scope.phoneNumberInfo = {
                    Ukraine: {
                        code: '+380', mask: '(S4) 444-44-44'},
                    Russia : {
                        code: '+7', mask: '(S44) 444-44-44'},
                    'United States' : {
                        code: '+1', mask: '(S44) 444-44-44'},
                    Bulgaria : {
                        code: '+359', mask: '(S4) 444-44-44'}
                };
                scope.mask = scope.mask == "" ? "" : scope.mask;
            }
        }
    }
);