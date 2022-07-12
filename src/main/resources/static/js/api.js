(function (window) {
    console.log('api')
    'use strict';
    angular.module('app')
        .factory('API', ['$resource',
            function ($resource) {
                console.log('api....')
                var api = {};

				api.prescriptions = $resource('http://localhost:8080/prescriptions/:PU', { }, {
                //api.prescriptions = $resource('https://e0cc-125-128-63-112.jp.ngrok.io/prescriptions/:PU', { }, {
                    'get_list':{
                        method: 'get'
                    },
                    'get_single':{
                        method:'get'
                    },
                    'insert':{
                        method: 'post',
                        headers: { "content-type": "application/json; charset=UTF-8" },
                        params: ''
                    },
                    'delete':{
                        method: 'delete'
                    }
                });

				api.medicines = $resource('http://localhost:8080/medicines', { }, {
                //api.medicines = $resource('https://e0cc-125-128-63-112.jp.ngrok.io/medicines', { }, {
                    'get_medicine_info':{
                        method: 'get'
                        
                    }
                });
                return api;
            }
        ]);
    }(window));