/**
 * Created by artemvlasov on 17/09/15.
 */
var app = angular.module('main-controllers', []);

app.controller('NavCtrl', ['$scope', 'auth', '$modal',
    function ($scope, auth, $modal) {
        $scope.auth = auth;
        $scope.showCustomTagModal = function () {
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: 'app/tag/add-custom-tag-modal.html',
                controller: 'AddCustomTagCtrl'
            })
        };
        $scope.adminPermission = function () {
            return auth.admin;
        };
        $scope.authenticated = function () {
            return auth.authenticated;
        };
        $scope.login = function () {
            auth.authenticate($scope.person, function (callback) {
                if (callback) {
                    $scope.error = callback;
                    $scope.person.password = null;
                }
            });
        };
        $scope.logout = function () {
            auth.clear();
        };
    }
]);

app.controller('MainCtrl', ['$scope', '$http', '$timeout', 'DiscountCardFactory', '$sessionStorage', '$location',
    function ($scope, $http, $timeout, DiscountCardFactory, $sessionStorage, $location) {
        var userCountDelayMilliseconds = 7;
        var cardCountDelayMilliseconds = 8;
        var locationCountMilliseconds = 9;
        new Waypoint({
            element: document.getElementById('project-info'),
            handler: function (direction) {
                var projectInfo = $('#project-info');
                if (direction == "down") {
                    countAnimation();
                    projectInfo.find('.circle').addClass('anim');
                    projectInfo.find('.button').addClass('anim');
                } else {
                    resetCountAnimation();
                    projectInfo.find('.circle').removeClass('anim');
                    projectInfo.find('.button').removeClass('anim');
                }
            },
            offset: '50%'
        });
        new Waypoint({
            element: document.getElementById("how-it-works"),
            handler: function(direction) {
                var howItWorks = $('#how-it-works');
                if (direction == "down") {
                    howItWorks.find('.info > div').addClass('anim');
                    howItWorks.find('.button').addClass('anim');
                } else {
                    howItWorks.find('.info > div').removeClass('anim');
                    howItWorks.find('.button').removeClass('anim');
                }
            },
            offset: '70%'
        });

        $scope.locationCount = 0;
        $scope.userCount = 0;
        $scope.cardCount = 0;
        var userCount, cardCount, locationCount;
        $http.get('/rest/location/count').success(function(data) {
            locationCount = data
        });
        $http.get('/rest/person/count').success(function(data) {
            userCount = data;
        });
        $http.get('/rest/card/count').success(function(data) {
            cardCount =  data;
        });

        var countAnimation = function () {
            var userAnim = function () {
                if($scope.userCount == userCount) {
                    $timeout.cancel(userAnimTimeout);
                } else {
                    userAnimTimeout = $timeout(userAnim, userCount > 10 ? 50 : 100);
                    $scope.userCount++;
                }
            };
            var userAnimTimeout = $timeout(userAnim, userCountDelayMilliseconds * 100);
            var locationAnim = function () {
                if($scope.locationCount == locationCount) {
                    $timeout.cancel(locationAnimTimeout);
                } else {
                    locationAnimTimeout = $timeout(locationAnim, locationCount > 10 ? 50 : 100);
                    $scope.locationCount++;
                }
            };
            var locationAnimTimeout = $timeout(locationAnim, locationCountMilliseconds * 100);
            var cardAnim = function () {
                if($scope.cardCount == cardCount) {
                    $timeout.cancel(cardAnimTimeout);
                } else {
                    cardAnimTimeout = $timeout(cardAnim, cardCount > 10 ? 50 : 100);
                    $scope.cardCount++;
                }
            };
            var cardAnimTimeout = $timeout(cardAnim, cardCountDelayMilliseconds * 100);

        };
        var resetCountAnimation = function () {
            $scope.locationCount = 0;
            $scope.userCount = 0;
            $scope.cardCount = 0;
        };
        DiscountCardFactory.query({top : 'top'}, function(data) {
            $scope.top5Cards = data;
        });
        $scope.showDiscountCard = function(discountCardId) {
            $sessionStorage.cardId = discountCardId;
            $location.path("/card/info");
        };
    }
]);