/**
 * Created by artemvlasov on 25/09/15.
 */
var app = angular.module('card-booking-services', ['ngResource']);

app.factory('CardBookingFactory', ['$resource', function($resource) {
    return $resource('/rest/card/booking/:book')
}]);