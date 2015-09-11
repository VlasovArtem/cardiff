var app = angular.module('discount-card-controllers', ['ngResource']);
app.controller('AddCtrl', ['$scope', '$location', 'AddDiscountCardFactory', 'tags', '$filter', function ($scope, $location, AddDiscountCardFactory, tags, $filter) {
    $scope.tags = tags;
    $scope.card = {};
    $scope.currentDate = new Date();
    $scope.addNewCard = function () {
        $scope.card.company_name = $filter('camelCase')($scope.card.company_name);
        if($scope.customTag != null) {
            var r = confirm("You have unsaved custom tags, do you want to add them to your discount card?");
            if(r) {
                $scope.addCustomTag();
            }
        }
        AddDiscountCardFactory.save($scope.card, function() {
            $location.path('/account')
        }, function(data) {
            $scope.error = data.data;
        })
    };

    $scope.addCustomTag = function() {
        if(_.isUndefined($scope.card.tags)) {
            $scope.card.tags = [];
        }
        _.each(splitCustomTags($scope.customTag), function(tag) {
            $scope.card.tags.push({tag : String(tag).toLowerCase()});
        });
        $scope.customTag = null;
    };

    $scope.reset = function () {
        $scope.card = {};
    };

    var splitCustomTags = function(customTags) {
        var tags = [];
        var regex = /\w+(\s(?!\s|^)\w*)*/g; //Matches groups that has only one spaces between them, if string has
        // text that divided by two or more spaces it matches as different groups. If string contains text divided
        // by , it will divided it on different groups
        var match;
        while (match = regex.exec(customTags)) {
            tags.push(match[0])
        }
        return tags.length > 0 ? tags : customTags;
    }
}]);

app.controller('DiscountCardsCtrl', ['$scope', '$location', 'discountCards', 'CardsCtrl', '$filter', function ($scope, $location, discountCards, CardsCtrl, $filter) {
    discountCards.$promise.then(function (data) {
        $scope.discountCards = data.content;
        $scope.totalItems = data.total_elements;
    });

    $scope.initialSort = {
        direction: 'DESC',
        property: 'createdDate'
    };

    $scope.head = [
        {name: 'Card #', property: 'card_number', width: '10%'},
        {name: 'Company', property: 'company_name', width: '20%'},
        {name: 'Discount', property: 'amount_of_discount', width: '9%'},
        {name: 'Description', property: 'description', width: '25%'},
        {name: 'Created', property: 'created_date', width: '10%'},
        {name: 'Expired', property: 'expired_date', width: '10%'},
        {name: 'Available', property: 'available', width: '10%'}
    ];

    $scope.getData = function (pageable) {
        CardsCtrl.get(pageable).$promise.then(function (data) {
            $scope.discountCards = data.content;
            $scope.totalItems = data.total_elements;
        });
    };
    $scope.setData = function (dIndex, dcIndex) {
        if($scope.head[dIndex].property == 'created_date' || $scope.head[dIndex].property == 'expired_date') {
            return $filter('dateFilter')($scope.discountCards[dcIndex][$scope.head[dIndex].property])
        } else if($scope.head[dIndex].property == 'amount_of_discount') {
            return $scope.discountCards[dcIndex][$scope.head[dIndex].property] + " %"
        } else {
            return $scope.discountCards[dcIndex][$scope.head[dIndex].property]
        }
    }

}]);

app.controller('SearchCtrl', ['$scope', 'DiscountCardSearchFactory', '$sessionStorage', '$location', function($scope, DiscountCardSearchFactory, $sessionStorage, $location) {
    $scope.ENTER_BUTTON = 13;
    $scope.searchData = {};
    $scope.search = function() {
        $scope.$storage = $sessionStorage.$default({
            cardId: $scope.searchData.selected[0]
        });
        $location.path('/card/info');
    };
    $scope.refreshData = function(data) {
        if(data != "") {
            DiscountCardSearchFactory.searchByName({company_name: data}).$promise.then(
                function(data) { $scope.companies = data; },
                function() { $scope.companies = []; })
        } else {
            $scope.companies = []
        }
    }
}]);

app.controller('DiscountCardInfoCtrl', ['$scope', 'discountCardInfo', 'PersonFactory', 'auth', function($scope, discountCardInfo, PersonFactory, auth) {
    $scope.cardInfo = discountCardInfo;
    do {
        $scope.authenticated = auth.authenticated;
    } while(!auth.resolved);
    if(auth.authenticated) {
        do {
            if(discountCardInfo.$resolved) {
                PersonFactory.get({get: 'get', cardId: discountCardInfo.id}, function(person) {
                    $scope.owner = person;
                }, function() {
                    $scope.error = "Required user is unavailable or deleted"
                })
            }
        } while (!discountCardInfo.$resolved)
    }

}]);