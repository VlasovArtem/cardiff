var app = angular.module('person-filters', []);

app.filter('phoneNumberFilter', function() {
    return function(phoneNumber, country) {
        if(angular.isDefined(phoneNumber) && angular.isDefined(country)) {
            var phoneNumberInfo = {
                Ukraine: {
                    code: '+380', mask: '(44) 444-44-44'
                },
                Russia: {
                    code: '+7', mask: '(444) 444-44-44'
                },
                'United States': {
                    code: '+1', mask: '(444) 444-44-44'
                },
                Bulgaria: {
                    code: '+359', mask: '(44) 444-44-44'
                }
            };
            var maskCap = [], parsedPhoneNumber = phoneNumberInfo[country].code + " ";

            function getNonReplaceableAttributes(mask) {
                var preparedMask = mask.split(''), replaceObject = '4';
                _.each(preparedMask, function (ch, i) {
                    if (ch == replaceObject) {
                        maskCap.push(i);
                    }
                })
            }

            getNonReplaceableAttributes(phoneNumberInfo[country].mask);
            function preparePhoneNumber() {
                var countNonReplaceableAttributes = 0;
                var splitPhoneNumber = phoneNumber.toString().split('');
                _.each(phoneNumberInfo[country].mask, function (ch, i) {
                    if (_.contains(maskCap, i)) {
                        parsedPhoneNumber = parsedPhoneNumber.concat(splitPhoneNumber[i - countNonReplaceableAttributes]);
                    } else {
                        parsedPhoneNumber = parsedPhoneNumber.concat(ch);
                        countNonReplaceableAttributes++;
                    }
                })
            }
            preparePhoneNumber();
            return parsedPhoneNumber;
        }
    }
});

app.filter('accountTable', function() {
    return function(data) {
        var person = {};
        if(data.$resolved) {
            var ignoredKeys = ['discountCards', 'id', 'createdDate', 'role', 'deleted'];
            _.each(data, function(value, key) {
                if(ignoredKeys.indexOf(key) == -1) {
                    person[key] = value;
                }
            });
        }
        return person;
    }
});