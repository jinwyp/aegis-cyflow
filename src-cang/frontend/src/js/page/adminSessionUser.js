/**
 * Created by JinWYP on 24/11/2016.
 */



var avalon = require('avalon2') ;
require('../component/header.js');

var userService = require('../service/user.js') ;


var url = window.location.href;
var urlShowStatus = url.substring(url.lastIndexOf("\/") + 1, url.length);

// console.log(urlShowStatus);


var sessionUser = function() {

    var vm = avalon.define({
        $id : 'passwordController',

        currentUser : {
            username : '',
            email : '',
            mobilePhone : '',
            companyName : '',
            belongToUser : '', // 资金方财务关联资金方用户ID, 贸易商财务关联贸易商用户ID
            role : ''
        },

        modifyPassword : {
            oldPwd : '',
            newPwd : '',
            rePwd : ''
        },

        errorMessage : {
            inputEmail : '',
            inputPhone : '',
            inputOldPwd : '',
            inputNewPwd : '',
            inputRePwd : ''
        },
        successInputName : [],
        errorInputName : [],
        validate: {
            onSuccess: function (reasons) {
                if(vm.successInputName.indexOf(this.id) === -1) vm.successInputName.push(this.id.toString());
                if(vm.errorInputName.indexOf(this.id) > -1) vm.errorInputName.splice(vm.errorInputName.indexOf(this.id), 1);
            },
            onError: function (reasons) {
                console.log(reasons[0].getMessage());
                vm.errorMessage[this.id.toString()] = reasons[0].getMessage();

                if (vm.successInputName.indexOf(this.id) > -1) vm.successInputName.splice(vm.successInputName.indexOf(this.id),1);
                if (vm.errorInputName.indexOf(this.id.toString()) === -1) vm.errorInputName.push(this.id.toString());
            },
            onValidateAll: function (reasons) {
                if (reasons.length) {
                    console.log('有表单没有通过');
                    $("input").focus().blur();
                } else {
                    console.log('全部通过');
                    if (vm.pageShowStatus === 'info') {

                        var user = {
                            username : vm.currentUser.username,
                            email : vm.currentUser.email,
                            mobilePhone : vm.currentUser.mobilePhone,
                            companyName : vm.currentUser.companyName,
                            role : vm.currentUser.role
                        };

                        userService.updateUserInfoById(vm.currentUser._id, user).done(function (data, textStatus, jqXHR) {
                            if (data.success) {
                                $.notify("用户修改信息成功!", 'success');
                            }
                        })
                    }
                    if (vm.pageShowStatus === 'password') {
                        userService.modifyPassword(vm.currentUser._id, vm.modifyPassword.oldPwd, vm.modifyPassword.newPwd).done(function (data, textStatus, jqXHR) {
                            if (data.success) {
                                $.notify("密码修改成功!", 'success');
                            }
                        })
                    }
                }
            }
        },

        pageShowStatus : 'password'

    });

    if (urlShowStatus === 'password'){
        vm.pageShowStatus = 'password';
    }else {
        vm.pageShowStatus = 'info';
    }

    userService.getSessionUser().done(function(data, textStatus, jqXHR) {
        if (data.success){
            vm.currentUser = data.data;
        }else{
            console.log(data.error);
        }
    });
};





sessionUser();


module.exports = sessionUser;