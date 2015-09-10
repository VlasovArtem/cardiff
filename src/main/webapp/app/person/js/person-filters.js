var app = angular.module('person-filters', []);
app.filter('accountFilter', function() {
    return function(key) {
        var withOutUnderscore = key.replace("_", " ");
        var capitalized = withOutUnderscore.charAt(0).toUpperCase().slice(0,1);
        return capitalized.concat(withOutUnderscore.substr(1, withOutUnderscore.length - 1));
    }
});
app.filter('phoneNumberFilter', function() {
    return function(phoneNumber) {
        if(phoneNumber != undefined) {
            var phoneNumberString = phoneNumber.toString();
            return '+380 (' + phoneNumberString.substr(0, 2) + ') ' + phoneNumberString.substr(2, 3) + '-' + phoneNumberString.substr(5, 2) + '-' + phoneNumberString.substr(7, 2);
        } else {
            return "";
        }
    }
});
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
app.filter('camelCase', function() {
    return function(input) {
        var replace = function(text) {
            return text.replace(/_.*/g, function(txt) {
                txt = txt.replace("_", "");
                return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase()
            });
        };
        do {
            input = replace(input)
        } while(input.indexOf("_") > -1);
        return input;
    }
});
app.filter('accountTable', function() {
    return function(data) {
        var person = {};
        if(data.$resolved) {
            var ignoredKeys = ['discount_cards', 'id', 'created_date', 'role', 'deleted'];
            _.each(data, function(value, key) {
                if(ignoredKeys.indexOf(key) == -1) {
                    person[key] = value;
                }
            });
        }
        return person;
    }
});