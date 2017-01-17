webpackJsonp([2],{

/***/ 0:
/*!************************!*\
  !*** multi adminLogin ***!
  \************************/
/***/ function(module, exports, __webpack_require__) {

	module.exports = __webpack_require__(/*! ./js/page/adminLogin.js */13);


/***/ },

/***/ 13:
/*!*******************************!*\
  !*** ./js/page/adminLogin.js ***!
  \*******************************/
/***/ function(module, exports, __webpack_require__) {

	/**
	 * Created by JinWYP on 24/10/2016.
	 */
	
	var avalon = __webpack_require__(/*! avalon2 */ 3) ;
	
	var userService = __webpack_require__(/*! ../service/user.js */ 8) ;
	var tokenService = __webpack_require__(/*! ../service/token.js */ 9) ;
	
	
	var login = function() {
	
	    var vm = avalon.define({
	        $id: "loginController",
	        user : {
	            username : '',
	            // email : '',
	            password : ''
	        },
	        errorMessage : {
	            inputUsername : '',
	            inputEmail : '',
	            inputPassword : '',
	            ajax : ''
	        },
	        successInputName : [],
	        errorInputName : [],
	        loginValidate : {
	            onSuccess : function (reasons){
	                if (vm.successInputName.indexOf(this.id) === -1) vm.successInputName.push(this.id.toString());
	                if (vm.errorInputName.indexOf(this.id) > -1) vm.errorInputName.splice(vm.errorInputName.indexOf(this.id),1);
	                // console.log('有单个表单项通过')
	            },
	            onError: function (reasons) {
	
	                vm.errorMessage[this.id.toString()] = reasons[0].getMessage();
	
	                if (vm.successInputName.indexOf(this.id) > -1) vm.successInputName.splice(vm.successInputName.indexOf(this.id),1);
	                if (vm.errorInputName.indexOf(this.id.toString()) === -1) vm.errorInputName.push(this.id.toString());
	                // console.log(vm.errorInputName)
	
	            },
	            onValidateAll: function (reasons) {
	                if (reasons.length) {
	                    console.log('有表单项没有通过')
	                } else {
	                    // console.log('表单全部通过');
	
	                    userService.login({
	                        username : vm.user.username,
	                        password : vm.user.password
	                    }).done(function( data, textStatus, jqXHR ) {
	                        if (data.success){
	                            console.log('登录成功', data);
	
	                            tokenService.saveSessionInLocalStorage('', data.data)
	                            window.location.href = '/warehouse/admin/home/finance'
	                        }else{
	                            vm.errorMessage.ajax = data.error.message;
	                        }
	                    }).fail(function( jqXHR, textStatus, errorThrown ) {
	                        var result = JSON.parse(jqXHR.responseText);
	                        console.error(result.error.message);
	                        vm.errorMessage.ajax = "用户名或密码错误!";
	                    });
	                }
	            }
	        }
	    });
	
	
	};
	
	
	login();
	
	module.exports = login;
	
	


/***/ }

});
//# sourceMappingURL=adminLogin.bundle.js.map