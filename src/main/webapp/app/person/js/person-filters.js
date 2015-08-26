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
        var phoneNumberString = phoneNumber.toString();
        return '+380 (' + phoneNumberString.substr(0, 2) + ') ' + phoneNumberString.substr(2, 3) + '-' + phoneNumberString.substr(5,2) + '-' + phoneNumberString.substr(7,2);
    }
});