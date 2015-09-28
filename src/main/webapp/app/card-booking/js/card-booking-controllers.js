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
                $modalInstance.dismiss('cancel');
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

app.controller('PersonDiscountCardBookingsCtrl', ['$scope', 'PersonBookedDiscountCardsFactory', 'PersonDiscountCardBookingsFactory', '$filter', 'booked', 'bookings',
    function($scope, PersonBookedDiscountCardsFactory, PersonDiscountCardBookingsFactory, $filter, booked, bookings) {
        $scope.bookedTableInfo = {
            data: booked,
            dataTemplate: 'app/card-booking/table-template.html',
            factory: PersonBookedDiscountCardsFactory,
            head: [
                {name: 'Card #', property: 'discount_card.card_number', width: '10%'},
                {name: 'Company', property: 'discount_card.company_name', width: '20%'},
                {name: 'Start', property: 'booking_start_date', width: '10%'},
                {name: 'End', property: 'booking_end_date', width: '10%'}
            ],
            filteredProperties: [
                {property: 'booking_start_date', filter: $filter('dateFilter')},
                {property: 'booking_end_date', filter: $filter('dateFilter')}
            ]
        };
        $scope.cardBookingsTableInfo = {
            data: bookings,
            dataTemplate: 'app/card-booking/table-template.html',
            factory: PersonDiscountCardBookingsFactory,
            head: [
                {name: 'Card #', property: 'discount_card.card_number', width: '10%'},
                {name: 'Company', property: 'discount_card.company_name', width: '20%'},
                {name: 'Person', property: 'person.name'},
                {name: 'Start', property: 'booking_start_date', width: '10%'},
                {name: 'End', property: 'booking_end_date', width: '10%'}
            ],
            filteredProperties: [
                {property: 'booking_start_date', filter: $filter('dateFilter')},
                {property: 'booking_end_date', filter: $filter('dateFilter')}
            ]
        };
    }
]);

app.controller('PersonDiscountCardBookingsFunctionCtrl', ['$scope', function ($scope) {}]);

app.controller('PersonBookedDiscountCardsFunctionCtrl', ['$scope', function ($scope) {}]);