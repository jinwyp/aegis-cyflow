/**
 * Created by JinWYP on 24/10/2016.
 */

var avalon = require('avalon2') ;

var userService = require('../service/user.js') ;
var tokenService = require('../service/token.js') ;


var login = function() {

    var vm = avalon.define({
        $id: "loginController",
        user : {
            username : '',
            email : '',
            password : ''
        },
        errorMessage : {
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
                console.log('有表单项通过')
            },
            onError: function (reasons) {

                vm.errorMessage[this.id.toString()] = reasons[0].getMessage();

                if (vm.successInputName.indexOf(this.id) > -1) vm.successInputName.splice(vm.successInputName.indexOf(this.id),1);
                if (vm.errorInputName.indexOf(this.id.toString()) === -1) vm.errorInputName.push(this.id.toString());
                console.log(vm.errorInputName)

            },
            onValidateAll: function (reasons) {
                if (reasons.length) {
                    console.log('有表单项没有通过')
                } else {
                    console.log('表单全部通过');

                    userService.login({
                        email : vm.user.email,
                        password : vm.user.password
                    }).done(function( data, textStatus, jqXHR ) {
                        if (data.success){
                            console.log('登录成功', data);

                            tokenService.saveSessionInLocalStorage(data.data.token, data.data.data)
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


