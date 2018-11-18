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

app.filter('camelCaseFilter', function() {
    return function(convertedText) {
        var returnedString = "";
        var firstWordIsSetted = false;
        convertedText.replace(/([A-Z][a-z]*)|([a-z]*)/g, function(text) {
            if(text != "") {
                if (!firstWordIsSetted) {
                    returnedString = returnedString + text.charAt(0).toUpperCase() + text.substr(1).toLowerCase();
                    firstWordIsSetted = true;
                } else {
                    returnedString = returnedString + " " + text.toLowerCase();
                }
            }
        });
        return returnedString;
    }
});

app.filter('locationFilter', function() {
    return function (location) {
        return location.city;
    }
});