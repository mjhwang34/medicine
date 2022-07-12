(function (window) {
    angular.module('app')
        .controller('mainController', ['$scope', 'API', '$cookies',"deviceDetector", function ($scope, API, $cookies, deviceDetector) {
            $scope.page = "index";
            $scope.is_loading = false;
			$scope.user = {
                password: ''
            }
			$scope.ctrl = {
				userCamera:"default",
				userQr: 1
			}
			$scope.camera_list_scope=[];
			$scope.multiple_cameras=false;
			$scope.qr_list = [{id:1, name:'처방전 QR코드'}, {id:2, name:'약포지 QR코드'}];
			$scope.qr_type = 1;
			$scope.select_qr_show=false;
			
			if(deviceDetector.raw.userAgent.indexOf('KAKAOTALK') != -1){
				swal("카카오톡 인앱 브라우저에서는 qr코드 기능이 작동하지 않을 수 있습니다.");
			}

            if ($cookies.get('passwd') == '1234') {
                list();
            }

            $scope.login = function (user) {
                if (user.password == '1234') {
                    $scope.is_loading = true;
					$cookies.put('passwd', '1234');
                    console.log($cookies.get('passwd'));
                    list();
                }
            }

            function list() {
                $scope.title = '이력';
                $scope.page = "plist";
                API.prescriptions.get_list({}).$promise.then(response => {
                    $scope.data_list = response.data
                    console.log(response);
                }).catch(err => {
                    console.error(err);
                })
                $scope.is_loading = false;
            }

            function show_item() {
                $scope.title = '처방전';
                $scope.page = "pdetail";

            }

            $scope.selectPrescription = function (item) {
                $scope.is_loading = true;
                $scope.data_detail = data_to_obj(item.data)
                set_item();
                show_item();
            }

			function set_item() {
                console.log($scope.data_detail);
                var edi_code_list = [];
                $scope.ME_list = [];
                for (var i in $scope.data_detail['ME']) {
                    edi_code_list.push($scope.data_detail['ME'][i].substring(0, 9))
                }
                console.log(edi_code_list);
                var edi_code_string = edi_code_list.join(',');
                API.medicines.get_medicine_info({ 'EDI_CODE_LIST': edi_code_string }).$promise.then(response => {
                    console.log(response.data);
                    var edi_code_obj = {};
                    for (var i in response.data){
                        edi_code_obj[response.data[i]['edi_code']] = response.data[i]['item_name'];
                    }
                    for (var i in edi_code_list){
                        var temp = {}
                        if(edi_code_list[i] in edi_code_obj){
                            temp['ME'] = edi_code_list[i];
                            temp['item_name'] = edi_code_obj[edi_code_list[i]];
							temp['amount'] = $scope.data_detail['ME'][i].substring(10, 11);
							temp['num'] = $scope.data_detail['ME'][i].substring(12, 13);
							temp['day'] = $scope.data_detail['ME'][i].substring(14, 15);
                        }
                        else{
                            temp['ME'] = edi_code_list[i];
                            temp['item_name'] = "정보없음"
                        }
                        $scope.ME_list.push(temp);
                    }
                    console.log(response);
                    $scope.is_loading = false;
                }).catch(err => {
                    console.error(err);
                })
            }

            $scope.back = function () {
                if ($scope.page == 'qr') {
                    $scope.qr_stop();
                    list();
                }
                else {
                    list();
                }
            }

            $scope.deleteAll = function () {
                console.log("deleteAll");
				swal({
                    text:'정말로 삭제하시겠습니까?',
                    buttons:{
                        cancel:true,
                        confirm:true
                    }
                }).then((result)=>{
                    console.log(result);
                    if(result==true){
                        API.prescriptions.delete({}).$promise.then(response => {
		                    console.log(response);
		                    $scope.list = [];
		                    list();
		                }).catch(err => {
		                    console.error(err);
		                })
                    }
                })
            }

            var qrcode_header = '식약처_오픈처방전';
            var html5QrCode = null;

            $scope.qr = function () {
                $scope.page = 'qr';
				$scope.is_loading=true;
                Html5Qrcode.getCameras().then(devices => {
                    console.log(devices)
                    $scope.$apply(function () {
                        $scope.devices = devices;
                    });
                    if (devices && devices.length) {
						$scope.$apply(function () {
							$scope.cur_camera = devices[0].id;
						});

                        var camera_list = [];
                        devices.forEach(device => {
                            console.log(device)
							if (device.label.indexOf('Pro') != -1 || device.label.indexOf('LG') != -1 || device.label.indexOf('back') != -1 || device.label.indexOf('후면') != -1) {
                                $scope.$apply(function () {
									$scope.cur_camera = device.id;
								});
                                camera_list.push(device);
                            }
							$scope.$apply(function () {
								$scope.camera_list_scope = [];
                        		$scope.camera_list_scope = camera_list;
								console.log($scope.camera_list_scope);
                    		});
                        })
                        console.log(camera_list.length);
                        if (camera_list.length == 0) {
							swal("카메라가 존재하지 않습니다.");
							list();
                        } else if (camera_list.length == 1) {
							qr_start()	
                        } else {
                            // select 박스..
							$scope.$apply(function () {
                    			$scope.multiple_cameras = true;	
								qr_start();
                    		});
                        }
                    }
                }).catch(err => {
					console.log(err);
                    // handle err
                });
            }

			$scope.select_qr = function(item){
				console.log(item);
				$scope.qr_type = item.id;
				$scope.ctrl.userQr=$scope.qr_type;
				$scope.qr_stop();
				console.log($scope.qr_type);
				qr_start();
			}
			
			$scope.select_camera = function (item) {
				console.log(item);
				$scope.qr_stop();
				$scope.is_loading=true;
				$scope.cur_camera = item.id;
				qr_start();
            }


            function qr_start() {
				var cameraId = $scope.cur_camera;
                html5QrCode = new Html5Qrcode("reader");
				console.log($scope.qr_type);
				var qrbox = {};
				if($scope.qr_type==1){
					qrbox={width:150, height:150}
				}
				else if($scope.qr_type==2){
					qrbox={width:100, height:100}
				}
				html5QrCode.start(
                cameraId,
                {
                    fps: 10,
                    qrbox: qrbox,
                    aspectRatio: 1
                },
                (decodedText, decodedResult) => {
                    console.log(decodedText)
                    console.log(decodedResult)
                    set_qrcode(decodedText)
                },
                (errorMessage) => {
                    // parse error, ignore it.
                    //console.log(errorMessage)
                })
				.then(()=>{
					$scope.$apply(function () {
                		$scope.select_qr_show=true;
						$scope.is_loading=false;
						console.log($scope.select_qr_show);
                	});
				})
                .catch((err) => {
                    // Start failed, handle it.
                    console.log(err)
                });
            }

            
            function set_qrcode(decodedText) {
                console.log(decodedText);
                console.log(decodedText.length);
				$scope.multiple_cameras = false;
                //큰 qr
                if (decodedText.indexOf(qrcode_header) == 0 && decodedText.length>60) {
                    console.log('qr_found');
                    $scope.$apply(function () {
                        $scope.qr_stop();
                    });
                    var item = {
                        data:decodedText
                    }
                    $scope.is_loading = true;
                    $scope.data_detail = data_to_obj(item.data)
                    set_item();
                    show_item();
                    API.prescriptions.get({PU:$scope.data_detail['PU']}).$promise.then(response=>{
                        console.log("response");
                        console.log(response);
                        if(response.data.length==0){
                            swal({
                                text:'처방전을 등록할까요?',
                                buttons:{
                                    cancel:true,
                                    confirm:true
                                }
                            }).then((result)=>{
                                console.log(result);
                                if(result==true){
                                    var dataMap = {};
                                    dataMap['PU'] = $scope.data_detail['PU'];
                                    dataMap['data'] = decodedText;
                                    console.log(dataMap);
                                    var dataMap_stringify = JSON.stringify(dataMap);
                                    console.log(dataMap_stringify)
                                    API.prescriptions.insert({ }, dataMap_stringify).$promise.then(response => {
                                        console.log(response);
                                        swal('등록이 완료되었습니다.');
                                    }).catch(err => {
                                        console.error(err);
                                    })
                                }
                            })
                        }
                        else{
                        }
                    })
                }
                //작은 qr
                else if (decodedText.indexOf(qrcode_header) == 0 && decodedText.length<=60){
                    console.log('qr_found');
                    $scope.$apply(function () {
                        $scope.qr_stop();
                    });
                    var item = {
                        data:decodedText
                    }
                    $scope.is_loading = true;
                    console.log("*");
                    var cur_pu = get_PU_from_small_qr(item.data);
                    console.log(cur_pu)
                    API.prescriptions.get({PU:cur_pu}).$promise.then(response=>{
                        if(response.data.length==0){
                            $scope.is_loading = false;
                            swal('처방전의 QR 코드를 먼저 등록해주세요.');
                            list();
                        }
                        else{
                            console.log(response.data[0].data);
                            $scope.data_detail = data_to_obj(response.data[0].data);
                            set_item();
                            show_item();
                        }
                    })
                } 
                else {
                }
            }
			
			$scope.qr_stop =function(){
                html5QrCode.stop().then((ignore) => {

                }).catch((err) => {
                    console.log(err)
                });   
				$scope.select_qr_show=false;
            }

            $scope.qr_success = function (decodedText) {
                html5QrCode.stop().then((ignore) => {

                }).catch((err) => {
                    console.log(err)
                });     
                var item = {
                    data:decodedText
                }
                set_item(item.data);
               
            }



            function data_to_obj(decodedText) {
                console.log("here");
                var data = {};
                var split_data = decodedText.split('\n');
                for (var i in split_data) {
                    console.log(split_data[i]);
                }
                for (var i in split_data) {
                    console.log(split_data[i]);
                    if (i == 0) {
                        data['VS'] = split_data[i];
                    }
                    else if (split_data[i].substring(0, 2) == 'ME') {
                        if('ME' in data == false){
                            data['ME'] = [];
                        }
                        data['ME'].push(split_data[i].substring(3, split_data[i].length));
                    }
					else if(split_data[i].substring(0, 2) == 'DA'){
						data[split_data[i].substring(0, 2)+'_year'] = split_data[i].substring(3, 7);
						data[split_data[i].substring(0, 2)+'_month'] = split_data[i].substring(7, 9);
						data[split_data[i].substring(0, 2)+'_day'] = split_data[i].substring(9, 11);
						data[split_data[i].substring(0, 2)+'_num'] = split_data[i].substring(12, split_data[i].length);
					}
                    else {
                        data[split_data[i].substring(0, 2)] = split_data[i].substring(3, split_data[i].length);
                    }
                }
                console.log(data);
                return data;
            }
            
            function get_PU_from_small_qr(decodedText){
                console.log("!");
                var split_data = decodedText.split('\n');
                console.log(split_data[1].substring(3, split_data[1].length));
                return split_data[1].substring(3, split_data[1].length);
            }
        }])
})(window);