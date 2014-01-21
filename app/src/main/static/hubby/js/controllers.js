'use strict';

/* Controllers */

var phonecatApp = angular.module('phonecatApp', [])

phonecatApp.controller('PhoneListCtrl', function($scope) {
  $scope.phones = [
    { 'name': 'Google Nexus', 'snippet': 'Fast got faster' },
    { 'name': 'Motorola Xoom', 'snippet': 'Next gen tablet' },
    { 'name': 'HTC One', 'snippet': 'the One to watch' }
 ]
})