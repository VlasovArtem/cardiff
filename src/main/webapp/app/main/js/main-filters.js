var app = angular.module('main-filters', []);

app.filter('dateFilter', function() {
    return function(date) {
        var filteredDate = "";
        var convertNumbers = function(date) {
            return date > 9 ? date : "0" + date;
        };
        for(var i = 0; i < 3; i++) {
            if(i == 2) {
                filteredDate += convertNumbers(date[i]);
            } else {
                filteredDate += convertNumbers(date[i]) + '-';
            }
        }
        return filteredDate;
    }
});