/**
 * Created by JinWYP on 24/11/2016.
 */



var avalon = require('avalon2') ;
require('../component/header.js');

var userService = require('../service/user.js') ;
var role = userService.userRoleKeyObject;

var url = window.location.href;
var urlShowStatus = url.substring(url.lastIndexOf("\/") + 1, url.length);
var userId = url.match(/\/user\/[a-zA-Z_0-9]{24,24}/);
if (userId){ userId = userId[0].split('/')[2] }

console.log('userID:', userId, '页面状态:', urlShowStatus);


var userInfo = function() {

    var vm = avalon.define({
        $id : 'userAddController',
        currentUser : {
            username : '',
            email : '',
            mobilePhone : '',
            companyName : '',
            belongToUser : '', // 资金方财务关联资金方用户ID, 贸易商财务关联贸易商用户ID
            role : ''
        },
        traderList : [],
        fundProviderList : [],

        roleList : userService.userRoleList,
        role : userService.userRoleKeyObject,

        pageShowStatus : 'add',

        errorMessage : {
            inputUsername : '',
            inputEmail : '',
            inputMobilePhone : '',
            inputCompanyName : '',
            inputUserRole:''
        },
        isMYSCWValid : false,
        successInputName : [],
        errorInputName : [],
        addValidate : {

            onSuccess : function (reasons){
                if (vm.successInputName.indexOf(this.id) === -1) vm.successInputName.push(this.id.toString());
                if (vm.errorInputName.indexOf(this.id) > -1) vm.errorInputName.splice(vm.errorInputName.indexOf(this.id),1);
            },
            onError: function (reasons) {
                console.log(reasons[0].getMessage());
                vm.errorMessage[this.id.toString()] = reasons[0].getMessage();

                if (vm.successInputName.indexOf(this.id) > -1) vm.successInputName.splice(vm.successInputName.indexOf(this.id),1);
                if (vm.errorInputName.indexOf(this.id.toString()) === -1) vm.errorInputName.push(this.id.toString());

            },
            onValidateAll: function (reasons) {
                console.log(vm.isMYSCWValid);

                var isValid = true;
                if(vm.currentUser.role === role.traderAccountant || vm.currentUser.role === role.fundProviderAccountant){
                    if (reasons.length || !vm.isMYSCWValid ) {
                        isValid = false;
                    }
                }else{
                    if (reasons.length ) {
                        isValid = false;
                    }
                }

                if(!isValid){
                    console.log('表单项没有通过');
                    $("input").focus().blur();
                    $("select").focus().blur()
                } else{
                    var user = {
                        username : vm.currentUser.username,
                        email : vm.currentUser.email,
                        mobilePhone : vm.currentUser.mobilePhone,
                        companyName : vm.currentUser.companyName,
                        role : vm.currentUser.role
                    };

                    if (vm.currentUser.belongToUser) {
                        user.belongToUser = vm.currentUser.belongToUser
                    }

                    if (vm.pageShowStatus === 'add') {
                        userService.addNewUser(user).done(function( data, textStatus, jqXHR ) {
                            if (data.success){
                                vm.successInputName = [];
                                vm.errorInputName = [];
                                $.notify("创建用户成功!", 'success');
                            }
                        })
                    }

                    if (vm.pageShowStatus === 'edit'){
                        userService.updateUserInfoById(vm.currentUser._id, user).done(function( data, textStatus, jqXHR ) {
                            if (data.success){
                                vm.successInputName = [];
                                vm.errorInputName = [];
                                $.notify("用户修改信息成功!", 'success');
                            }
                        })
                    }
                }




            }
        },

        addUser :function(){
            console.log(vm.currentUser.role, vm.currentUser.belongToUser)
        },
        isValid : checkMYS

    });

    function checkMYS() {

        if (vm.currentUser.role === role.traderAccountant){
            if (vm.currentUser.belongToUser){
                vm.isMYSCWValid = true;
            }else{
                vm.isMYSCWValid = false;
            }
        }else if (vm.currentUser.role === role.fundProviderAccountant){
            if (vm.currentUser.belongToUser){
                vm.isMYSCWValid = true;
            }else{
                vm.isMYSCWValid = false;
            }
        }
    }

    function getUserInfo() {
        userService.getUserInfoById(userId).done(function (data, textStatus, jqXHR) {
            if (data.success) {
                vm.currentUser = data.data;
                // vm.configPagination.totalPages = Math.ceil(data.meta.total / data.meta.count);
            } else {
                console.log(data.error);
            }
        });
    }
    function getUsersOfRoles(){

        userService.getUserList({role : role.trader, $limit : 500}).done(function(data, textStatus, jqXHR) {
            if (data.success){
                vm.traderList = data.data;
            }else{
                console.log(data.error);
            }
        });

        userService.getUserList({role : role.fundProvider, $limit : 500}).done(function(data, textStatus, jqXHR) {
            if (data.success){
                vm.fundProviderList = data.data;
            }else{
                console.log(data.error);
            }
        })
    }


    if (urlShowStatus === 'add'){
        vm.pageShowStatus = 'add';
        getUsersOfRoles()
    }else if (urlShowStatus === 'edit'){
        vm.pageShowStatus = 'edit';
        getUserInfo()
        getUsersOfRoles()
    }else {
        vm.pageShowStatus = 'info';
        getUserInfo()
    }




};





userInfo();


module.exports = userInfo;