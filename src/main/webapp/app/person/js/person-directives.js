var app = angular.module('person-directives', []);

app.directive('accountData',
    function($filter) {
        return {
            restrict: 'A',
            scope: {
                label: '=',
                data: '='
            },
            link: function(scope, element, attrs) {
                if(scope.label == 'phone_number') {
                    scope.data = $filter('phoneNumberFilter')(scope.data)
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