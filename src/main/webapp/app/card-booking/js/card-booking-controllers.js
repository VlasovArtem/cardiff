/**
 * Created by artemvlasov on 25/09/15.
 */
var app = angular.module('card-booking-controllers', []);

app.controller('CardBookingCtrl', ['$scope', '$modalInstance', 'cardId', 'CardBookingFactory', function($scope, $modalInstance, cardId, CardBookingFactory) {
    $scope.book = function() {
        CardBookingFactory.save({
            book: 'book',
            discount_card_id: cardId,
            booking_start_date: $scope.bookingStartDate
        })
    }
}]);