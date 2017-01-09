/**
 * Created by JinWYP on 24/11/2016.
 */



var avalon = require('avalon2') ;
require('../component/header.js');

var userService = require('../service/user.js') ;

var url = window.location.href;
var urlShowStatus = url.substring(url.lastIndexOf("\/") + 1, url.length);
var companyId = url.match(/\/company\/[a-zA-Z_0-9]{5,13}\/[a-zA-Z_0-9]{8,30}/);
console.log(companyId)
if (companyId){ companyId = companyId[0].split('/')[2] + '/' + companyId[0].split('/')[3] }

console.log('companyID:', companyId, '页面状态:', urlShowStatus);


var companyInfo = function() {

    var vm = avalon.define({
        $id : 'companyAddController',
        currentCompany : {
            companyName : '',
            partyClass : ''
        },
        traderList : [],
        // fundProviderList : [],

        roleList : userService.userRoleList,

        pageShowStatus : 'add',

        errorMessage : {
            inputCompanyName : '',
            inputPartyClass:''
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


                if(reasons.length){
                    console.log('表单项没有通过');
                    $("input").focus().blur();
                    $("select").focus().blur()
                } else{

                    var user = {
                        companyName : vm.currentCompany.companyName,
                        partyClass : vm.currentCompany.partyClass
                    };


                    if (vm.pageShowStatus === 'add') {
                        userService.addNewCompany(user).done(function( data, textStatus, jqXHR ) {
                            if (data.success){
                                vm.successInputName = [];
                                vm.errorInputName = [];
                                $.notify("创建成功!", 'success');
                            }
                        })
                    }

                    if (vm.pageShowStatus === 'edit'){
                        userService.updateCompanyInfoById(vm.currentCompany.instanceId, user).done(function( data, textStatus, jqXHR ) {
                            if (data.success){
                                vm.successInputName = [];
                                vm.errorInputName = [];
                                $.notify("修改信息成功!", 'success');
                            }
                        })
                    }
                }

            }
        },

        addCompany :function(){

        }

    });


    function getCompanyInfo() {
        userService.getCompanyInfoById(companyId).done(function (data, textStatus, jqXHR) {
            if (data.success) {
                vm.currentCompany = data.data;
            } else {
                console.log(data.error);
            }
        });
    }


    if (urlShowStatus === 'add'){
        vm.pageShowStatus = 'add';
    }else if (urlShowStatus === 'edit'){
        vm.pageShowStatus = 'edit';
        getCompanyInfo();
    }else {
        vm.pageShowStatus = 'info';
        getCompanyInfo()
    }


};





companyInfo();


module.exports = companyInfo;