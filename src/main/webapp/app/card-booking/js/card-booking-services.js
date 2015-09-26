/**
 * Created by artemvlasov on 25/09/15.
 */
var app = angular.module('card-booking-services', ['ngResource']);

app.factory('CardBookingFactory', ['$resource', function($resource) {
    return $resource('/rest/card/booking/:book', {
        book: '@book'
    }, {
        bookCard: {
            method: 'POST',
            isArray: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            params: {
                book: 'book'
            }
        }
    })
}]);