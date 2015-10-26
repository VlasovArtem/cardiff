/**
 * Created by artemvlasov on 25/09/15.
 */
var app = angular.module('card-booking-controllers', []);

app.controller('CardBookingCtrl', ['$scope', '$modalInstance', 'cardId', 'availableDate', 'CardBookingFactory', '$filter', '$location',
    function($scope, $modalInstance, cardId, availableDate, CardBookingFactory, $filter, $location) {
        $scope.availableDate = $filter('ToDateFilter')(availableDate);
        $scope.bookingStartDate = angular.copy($scope.availableDate);
        $scope.status = {
            opened: false
        };
        var pickedDate;
        $scope.book = function() {
            var bookData = {
                discountCardId: cardId,
                bookingStartDate: $filter('ToLocalDateFilter')(new Date($scope.bookingStartDate))
            };
            CardBookingFactory.bookCard($.param(bookData),
                function () {
                    alert('Discount card successfully booked');
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
                var valid = pickedDate.getTime() >= $scope.availableDate.getTime();
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
            if(pickedDate.getTime() < $scope.availableDate.getTime()) {
                $scope.error = {
                    error: 'Booking start date, cannot be less than today'
                }
            }
        };
        $scope.changeDate();
        $scope.open = function($event) {
            $scope.status.opened = true;
        };
    }]);

app.controller('PersonDiscountCardBookingsCtrl', ['$scope', 'PersonBookedDiscountCardsFactory', 'PersonDiscountCardBookingsFactory', '$filter', 'booked', 'bookings', '$sce',
    function($scope, PersonBookedDiscountCardsFactory, PersonDiscountCardBookingsFactory, $filter, booked, bookings, $sce) {
        $scope.bookedTableInfo = {
            data: booked,
            dataTemplate: 'app/card-booking/table-template.html',
            factory: PersonBookedDiscountCardsFactory,
            head: [
                {name: 'Card #', property: 'discount_card.card_number', width: '10%'},
                {name: 'Company', property: 'discount_card.company_name', width: '20%'},
                {name: 'Start', property: 'booking_start_date', width: '15%', class: 'center'},
                {name: 'End', property: 'booking_end_date', width: '15%', class: 'center'}
            ],
            filteredProperties: [
                {property: 'booking_start_date', filter: $filter('dateFilter')},
                {property: 'booking_end_date', filter: $filter('dateFilter')}
            ],
            dataButtons: {
                desktop: 'app/card-booking/booked-buttons.html'
            },
            htmlBinding: [{
                head: 'Picked',
                width: '5%',
                desktopClass: 'center',
                desktop: $sce.trustAsHtml('<boolean value="{{data.discount_card.picked}}"></boolean>')
            }]
        };
        $scope.cardBookingsTableInfo = {
            data: bookings,
            dataTemplate: 'app/card-booking/table-template.html',
            factory: PersonDiscountCardBookingsFactory,
            head: [
                {name: 'Card #', property: 'discount_card.card_number', width: '10%'},
                {name: 'Company', property: 'discount_card.company_name', width: '20%'},
                {name: 'Person', property: 'person.name'},
                {name: 'Start', property: 'booking_start_date', width: '10%', class: 'center'},
                {name: 'End', property: 'booking_end_date', width: '10%', class: 'center'}
            ],
            filteredProperties: [
                {property: 'booking_start_date', filter: $filter('dateFilter')},
                {property: 'booking_end_date', filter: $filter('dateFilter')}
            ],
            dataButtons: {
                desktop: 'app/card-booking/bookings-buttons.html'
            },
            htmlBinding: [{
                head: 'Picked',
                width: '5%',
                desktopClass: 'center',
                desktop: $sce.trustAsHtml('<boolean value="{{data.discount_card.picked}}"></boolean>')
            }]
        };
    }
]);

app.controller('BookingsFunctionCtrl', ['$scope', 'CardBookingFactory', '$route', function ($scope, CardBookingFactory, $route) {
    $scope.cancel = function(bookingId) {
        CardBookingFactory.cancel({bookingId: bookingId}, function() {
            $route.reload();
        });
    };

    $scope.picked = function(bookingId) {
        CardBookingFactory.picked({bookingId: bookingId}, function() {
            $route.reload();
        });
    };

    $scope.returned = function(bookingId) {
        CardBookingFactory.returned({bookingId: bookingId}, function() {
            $route.reload();
        });
    }

}]);