(function (window) {
    'use strict';
    angular.module('commons', ['ngMaterial', 'ngMessages', 'md.data.table', 'ngResource', 'oitozero.ngSweetAlert', 'ngCookies', 'ng.deviceDetector', 'reTree']);
    angular.module('app', ['commons']);
})(window);