var app = angular.module('tag-controllers', []);

app.controller('AddCustomTagCtrl', ['$scope', '$modalInstance', 'CustomTagFactory',
    function($scope, $modalInstance, CustomTagFactory) {
        $scope.saveCustomTag = function() {
            CustomTagFactory.addTag($scope.customTag,
                function() {
                    alert('Custom tag is successfully added');
                    $modalInstance.dismiss('cancel');
                }, function(info) {
                    console.log(info);
                    if(info) {
                        $scope.error = info.data;
                    }
                });
        };
        $scope.closeModal = function() {
            $modalInstance.dismiss('cancel');
        }
    }
]);

app.controller('AdminTagCtrl', ['$scope', '$location', 'customTags', 'tags', 'AdminCustomTagFactory','$filter',
    function ($scope, $location, customTags, tags, AdminCustomTagFactory, $filter) {
        $scope.tags = tags;
        $scope.tableInfo = {
            data: customTags,
            factory: AdminCustomTagFactory,
            head: [
                {name: 'Author name', property: 'author.name', width: '15%'},
                {name: 'Author email', property: 'author.email', width: '20%'},
                {name: 'Tag', property: 'tag', width: '10%'},
                {name: 'Created', property: 'created_date', width: '10%'},
                {name: 'Description', property: 'description', width: '25%'}
            ],
            filteredProperties: [
                {property: 'created_date', filter: $filter('dateFilter')}
            ],
            dataButtons: {
                desktop: 'app/tag/tag-buttons-desktop.html'
            }
        };

    }
]);
app.controller('AdminTagFunctionCtrl', ['$scope', '$route', 'CustomTagFactory', function($scope, $route, CustomTagFactory) {
    $scope.remove = function(id) {
        CustomTagFactory.delete({admin : 'admin', delete : 'delete', tagId : id},
            function() {
                $route.reload()
            }, function(info) {
                $scope.error = info.data;
            });
    };
    $scope.accept = function(id) {
        CustomTagFactory.acceptTag({tagId : id},
            function() {
                $route.reload()
            }, function(info) {
                $scope.error = info.data;
            })
    };
}]);
