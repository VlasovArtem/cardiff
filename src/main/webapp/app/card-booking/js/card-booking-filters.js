/**
 * Created by artemvlasov on 26/09/15.
 */
var app = angular.module('card-booking-filters', []);

app.filter('ToLocalDateFilter', function() {
    return function(date) {
        if(angular.isDefined(date) && _.isDate(date)) {
            var year = date.getFullYear(),
                month = (date.getMonth() + 1) >= 10 ? (date.getMonth() + 1) : '0' + (date.getMonth() + 1),
                parsedDate = date.getDate() >= 10 ? date.getDate() : '0' + date.getDate();
            return year + "-" + month + "-" + parsedDate;
        }
    }
});

app.filter('ToDateFilter', function() {
    return function(date) {
        if(_.isArray(date)) {
            return new Date(date[0], date[1] - 1, date[2], 0, 0, 0, 0);
        }
    }
});