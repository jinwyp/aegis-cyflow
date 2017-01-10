/**
 * Created by JinWYP on 24/11/2016.
 */



var avalon = require('avalon2') ;
require('../component/header.js');

var userService = require('../service/user.js') ;
var role = userService.userRoleKeyObject;

var url = window.location.href;
var urlShowStatus = url.substring(url.lastIndexOf("\/") + 1, url.length);
var userId = url.match(/\/user\/[a-zA-Z_0-9]{2,24}/);
if (userId){ userId = userId[0].split('/')[2] }

console.log('userID:', userId, '页面状态:', urlShowStatus);


var userInfo = function() {

    var vm = avalon.define({
        $id : 'userAddController',
        companyList:[],
        currentUser : {
            username : '',
            email : '',
            phone : '',
            companyId : '',
            companyName : '',
            // belongToUser : '', // 资金方财务关联资金方用户ID, 贸易商财务关联贸易商用户ID
            role : ''
        },
        currentCompanyJSON : {},
        currentCompany : {
            partyClass : ''
        },

        traderList : [],
        fundProviderList : [],

        userRoleTrader : userService.userRoleTrader,
        userRoleFundProvider : userService.userRoleFundProvider,
        role : userService.userRoleKeyObject,

        pageShowStatus : 'add',

        errorMessage : {
            inputUsername : '',
            inputEmail : '',
            inputMobilePhone : '',
            inputCompanyName : '请选择公司名称',
            inputUserRole:''
        },

        selectCompany: function (e) {
            if (vm.currentCompanyJSON){
                vm.currentCompany = JSON.parse(vm.currentCompanyJSON);
                vm.currentUser.role = vm.currentCompany.partyClass;
                vm.currentUser.companyId = vm.currentCompany.instanceId.substring(vm.currentCompany.instanceId.lastIndexOf("\/") + 1, vm.currentCompany.instanceId.length);
                vm.currentUser.companyName = vm.currentCompany.companyName;
            }else{
                vm.currentUser.role = '';
                vm.currentUser.companyId = '';
                vm.currentUser.companyName = '';

                vm.currentCompany.partyClass = '';
            }

            checkCompanyNameError();
        },

        jsonStringfy : function(obj){
            return JSON.stringify(obj)
        },
        // isMYSCWValid : false,
        successInputName : [],
        errorInputName : [],
        addValidate : {

            onSuccess : function (reasons){
                if (vm.successInputName.indexOf(this.id) === -1) vm.successInputName.push(this.id.toString());
                if (vm.errorInputName.indexOf(this.id) > -1) vm.errorInputName.splice(vm.errorInputName.indexOf(this.id),1);
            },
            onError: function (reasons) {
                console.log("单个字段错误信息: ",reasons[0].getMessage());
                vm.errorMessage[this.id.toString()] = reasons[0].getMessage();

                if (vm.successInputName.indexOf(this.id) > -1) vm.successInputName.splice(vm.successInputName.indexOf(this.id),1);
                if (vm.errorInputName.indexOf(this.id.toString()) === -1) vm.errorInputName.push(this.id.toString());

            },
            onValidateAll: function (reasons) {
                // console.log(vm.isMYSCWValid);

                var isValid = true;

                if (reasons.length) {
                    isValid = false;
                }

                if(!vm.currentUser.companyName){
                    isValid = false;
                    checkCompanyNameError();
                }



                if(!isValid){
                    console.log('表单项没有通过');
                    console.log(reasons);
                    $("input").focus().blur();
                    $("select").focus().blur();
                } else{
                    var user = {
                        password : '123456',
                        username : vm.currentUser.username,
                        email : vm.currentUser.email,
                        phone : vm.currentUser.phone,
                        companyName : vm.currentUser.companyName,
                        companyId : vm.currentUser.companyId,
                        className : vm.currentUser.role
                    };

                    console.log(user);

                    // if (vm.currentUser.belongToUser) {
                    //     user.belongToUser = vm.currentUser.belongToUser
                    // }

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
                        userService.updateUserInfoById(userId, user).done(function( data, textStatus, jqXHR ) {
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
            console.log(vm.currentUser)
        },
        editUser :function(){
            console.log(vm.currentUser)
        },
        
        resetPassword : function () {
            userService.resetPasswordByUserId(userId).done(function (data, textStatus, jqXHR) {
                if (data.success) {
                    $.notify("重置密码成功!", 'success');
                    // vm.currentUser = data.data;
                    // vm.configPagination.totalPages = Math.ceil(data.meta.total / data.meta.count);
                }
            });
        }
        // isValid : checkMYS

    });


    function checkCompanyNameError() {
        if(vm.currentUser.companyName === ''){
            vm.errorInputName.push('inputCompanyName');
        }else{
            var tempIndex = vm.errorInputName.indexOf('inputCompanyName')
            if (tempIndex > -1){
                vm.errorInputName.splice(tempIndex, 1);
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


    function getCompanies(query){
        query = query || {};

        userService.getCompanyList(query).done(function(data, textStatus, jqXHR) {
            if (data.success){
                vm.companyList = data.data;

            }else{
                console.log(data.error);
            }
        })
    }


    getCompanies();






    if (urlShowStatus === 'add'){
        vm.pageShowStatus = 'add';
    }else if (urlShowStatus === 'edit'){
        vm.pageShowStatus = 'edit';
        getUserInfo();
    }else {
        vm.pageShowStatus = 'info';
        getUserInfo()
    }




};





userInfo();


module.exports = userInfo;