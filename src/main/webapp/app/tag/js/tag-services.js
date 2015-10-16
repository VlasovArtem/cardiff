var service = angular.module('tag-services', ['ngResource']);

service.factory('CustomTagFactory', function($resource) {
    return $resource('/rest/tag/custom/:add/:admin/:count/:accept/:delete/:tagId', {
        add : '@add',
        tagId: '@tagId'
    }, {
        addTag : {
            params: {
                add : 'add'
            },
            method: 'POST'
        },
        acceptTag : {
            method: 'PUT',
            params: {
                admin: 'admin',
                accept: 'accept'
            }
        }
    });
});

service.factory('AdminCustomTagFactory', function($resource) {
    return $resource('/rest/tag/custom/admin/get/page');
});

service.factory('TagFactory', function($resource) {
    return $resource('/rest/tag/get/all');
});