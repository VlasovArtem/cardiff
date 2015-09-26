/**
 * Created by artemvlasov on 25/09/15.
 */
var app = angular.module('card-booking-controllers', []);

app.controller('CardBookingCtrl', ['$scope', '$modalInstance', 'cardId', 'CardBookingFactory', '$filter', '$location', function($scope, $modalInstance, cardId, CardBookingFactory, $filter, $location) {
    var currentDate = new Date(), pickedDate;
    currentDate.setHours(0, 0, 0, 0);
    $scope.book = function() {
        var bookData = {
            discountCardId: cardId,
            bookingStartDate: $filter('ToLocalDateFilter')(new Date($scope.bookingStartDate))
        };
        CardBookingFactory.bookCard($.param(bookData),
            function () {
                $location.path('/account/booking')
            }, function(data) {
                $scope.error = data.data.error;
                $scope.bookingStartDate = null;
            });
    };
    $scope.close = function() {
        $modalInstance.dismiss('cancel');
    };
    $scope.isValidDate = function() {
        if(angular.isDefined(pickedDate)) {
            var valid = pickedDate.getTime() >= currentDate.getTime();
            if (valid) {
                $scope.error = null;
            }

            return valid;
        } else {
            return false;
        }
    };
    $scope.changeDate = function() {
        pickedDate = new Date($scope.bookingStartDate);
        pickedDate.setHours(0, 0, 0, 0);
        if(pickedDate.getTime() < currentDate.getTime()) {
            $scope.error = {
                error: 'Booking start date, cannot be less than today'
            }
        }
    };
}]);