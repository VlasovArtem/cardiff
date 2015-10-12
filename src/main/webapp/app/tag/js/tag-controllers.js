/**
 * Created by artemvlasov on 05/09/15.
 */
app.controller('AdminTagCtrl', ['$scope', '$location', 'tags', 'AdminTagFactory','AdminTagTableFactory','$filter',
    function ($scope, $location, tags, TagFactory,AdminTagTableFactory, $filter) {
        $scope.tableInfo = {
            data: tags,
            factory: AdminTagTableFactory,
            head: [
                {name: 'Tag #', property: 'tag', width: '40%'},
                {name: 'Created', property: 'created_date', width: '600%'}
            ],
            filteredProperties: [
                {property: 'created_date', filter: $filter('dateFilter')}
            ],
            dataButtons: {
                desktop: 'app/tag/tag-buttons.html'
            }
        };

    }
]);
app.controller('AdminTagFunctionCtrl', ['$scope', '$sessionStorage', '$location', '$route', 'AdminTagFactory', function($scope, $sessionStorage, $location, $route, AdminPersonFactory) {
    $scope.remove = function(id) {
        AdminTagFactory.remove({delete: 'delete', id : id},
            function() {
                $route.reload()
            });
    };
    $scope.adoptTag = function(id) {
            AdminTagFactory.remove({adoptTag: 'adoptTag', id : id},
                function() {
                    $route.reload()
                });
        };
}]);
