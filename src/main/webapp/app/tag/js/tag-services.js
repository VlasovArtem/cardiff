/**
 * Created by artemvlasov on 05/09/15.
 */
 service.factory('AdminTagTableFactory', ['$resource', function($resource) {
     return $resource('/rest/untag/get/page')
 }]);
service.factory('AdminPersonFactory', ['$resource',
    function($resource) {
        return $resource('/rest/untag/admin/:delete/:adoptTag/', {
            delete: '@delete',
            adoptTag: '@adoptTag',
        })
    }
]);