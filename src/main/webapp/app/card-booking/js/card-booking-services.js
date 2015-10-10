/**
 * Created by artemvlasov on 25/09/15.
 */
var app = angular.module('card-booking-services', ['ngResource']);

app.factory('CardBookingFactory', ['$resource', function($resource) {
    return $resource('/rest/card/booking/:book/:cancel/:picked/:returned/:bookingId/:get/:available/:start', {
        book: '@book',
        cancel: '@cancel',
        picked: '@picked',
        returned: '@returned',
        bookingId: '@bookingId'
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
        },
        cancel: {
            method: 'PUT',
            params: {
                cancel: 'cancel'
            }
        },
        picked: {
            method: 'PUT',
            params: {
                picked: 'picked'
            }
        },
        returned: {
            method: 'PUT',
            params: {
                returned: 'returned'
            }
        }
    })
}]);

app.factory('PersonBookedDiscountCardsFactory', ['$resource', function($resource) {
    return $resource('/rest/card/booking/booked/page')
}]);

app.factory('PersonDiscountCardBookingsFactory', ['$resource', function($resource) {
    return $resource('/rest/card/booking/bookings/page')
}]);