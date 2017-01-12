var jQuery = require('jquery');
var avalon = require('avalon2');
require('../component/header.js');

var API = require('../service/token').url;
var tokenHeaders = require('../service/token').headers;

window.jQuery   = jQuery;
var WebUploader = require('../libs/webuploader/webuploader.nolog.js');


var echarts         = require('echarts');
var userService     = require('../service/user.js');
var orderService    = require('../service/financeOrder.js');
var sessionUserId   = require('../service/token.js').sessionUserId;
var sessionUserRole = require('../service/token.js').sessionUserRole;


var url           = window.location.href;
var orderId       = url.substring(url.lastIndexOf("\/") + 1, url.length);
var urlShowStatus = 'orderInfo'
if (url.indexOf('contract') > -1) urlShowStatus = 'contractInfo'
console.log(orderId, urlShowStatus);

var orderInfo = function () {

    var uploadFileList = [];
    var tempDeliveryId = '';

    var vm = avalon.define({
        $id                  : 'orderInfoController',
        role                 : userService.userRoleKeyObject,
        contractType         : orderService.contractType,
        action               : orderService.actionObject,
        selectedContractType : '',
        contractList         : [],
        uploadFileList       : [],
        repaymentList        : [],
        depositList          : [],
        deliveryList         : [],
        contractInvestigationInfo      : [],
        contractSupervisorInfo     : [],


        currentUser          : {
            id   : sessionUserId,
            role : sessionUserRole
        },
        currentOrderId       : orderId,
        currentOrder         : {
            flowId : '',
            currentSessionUserTaskId : '',
            currentSessionUserTaskTaskName : '',
            cyPartyMember : {
                financer : {}
            },
            flowData : {},
            spData : {}
        },

        doAction             : function (actionName) {
            var additionalData = {};

            if (sessionUserRole === vm.role.trader) {

                // 贸易商选择 资金方 资金方财务 港口 监管方
                if (vm.currentOrder.flowData.status === vm.action.a11SelectHarborAndSupervisor.statusAt){

                    vm.traderFormError.fundProvider = false;
                    vm.traderFormError.fundProviderAccountant = false;
                    vm.traderFormError.harbor       = false;
                    vm.traderFormError.supervisor   = false;

                    if (!vm.traderForm.selectedFundProvider) vm.traderFormError.fundProvider = true;
                    if (!vm.traderForm.selectedFundProviderAccountant) vm.traderFormError.fundProviderAccountant = true;
                    if (!vm.traderForm.selectedHarbor) vm.traderFormError.harbor = true;
                    if (!vm.traderForm.selectedSupervisor) vm.traderFormError.supervisor = true;

                    if (vm.traderFormError.fundProvider || vm.traderFormError.fundProviderAccountant || vm.traderFormError.harbor || vm.traderFormError.supervisor) {
                        return;
                    }else{
                        additionalData = {
                            "harborUserId"                    : vm.traderForm.selectedHarbor.split('-')[0],
                            "harborCompanyId"                 : vm.traderForm.selectedHarbor.split('-')[1],
                            "supervisorUserId"                : vm.traderForm.selectedSupervisor.split('-')[0],
                            "supervisorCompanyId"             : vm.traderForm.selectedSupervisor.split('-')[1],
                            "fundProviderUserId"              : vm.traderForm.selectedFundProvider.split('-')[0],
                            "fundProviderCompanyId"           : vm.traderForm.selectedFundProvider.split('-')[1],
                            "fundProviderAccountantUserId"    : vm.traderForm.selectedFundProviderAccountant.split('-')[0],
                            "fundProviderAccountantCompanyId" : vm.traderForm.selectedFundProviderAccountant.split('-')[1]
                        }
                    }
                }

                // 贸易商给出从资金方的贷款利率
                console.log(vm.currentOrder.flowData.status,vm.action.a15Approved.statusAt );
                if (vm.currentOrder.flowData.status === vm.action.a15Approved.statusAt){

                    vm.errorFundProviderInterestRate = false;

                    if (!vm.inputFundProviderInterestRate) {
                        vm.errorFundProviderInterestRate = true;
                        return ;
                    }else{
                        additionalData = {
                            "fundProviderInterestRate" : vm.inputFundProviderInterestRate
                        }
                    }
                }


                if (vm.currentOrder.flowData.status === 'repaymentStep31'){

                    // 贸易商 返还货物 并给港口上传货物文件

                    vm.errorRedemptionAmount = false;
                    vm.errorRedemptionReceiver = false;
                    vm.errorRedemptionFileList = false;

                    if (!vm.inputRedemptionAmount || vm.inputRedemptionAmount < 10 ) {
                        vm.errorRedemptionAmount = true;
                        return ;
                    }
                    if (!vm.inputRedemptionReceiver ||  vm.inputRedemptionReceiver.length < 2 ) {
                        vm.errorRedemptionReceiver = true;
                        return ;
                    }
                    if (vm.inputRedemptionFileList.length === 0 ) {
                        vm.errorRedemptionFileList = true;
                        return ;
                    }


                    var tempDelivery = {
                        uploadFiles : [],
                        redemptionAmount : vm.inputRedemptionAmount,
                        receiver : vm.inputRedemptionReceiver,
                        orderId      : orderId,
                        orderNo      : vm.currentOrder.orderNo
                    }

                    additionalData.fileList = []
                    additionalData.redemptionAmount = vm.inputRedemptionAmount
                    additionalData.goodsReceiveCompanyName = vm.inputRedemptionReceiver

                    vm.inputRedemptionFileList.forEach(function(file, fileIndex){
                        tempDelivery.uploadFiles.push(file.fileId)
                        additionalData.fileList.push(file.fileId)
                    })



                    orderService.addNewDelivery(tempDelivery).done(function (data) {
                        if (data.success) {
                            getOrderInfo()
                            $.notify("保存成功!", 'success');
                        } else {
                            console.log(data.error);
                        }
                    })


            }

            }



            // 融资方 港口 监管 上传文件
            if (sessionUserRole === vm.role.financer || sessionUserRole === vm.role.harbor || sessionUserRole === vm.role.supervisor){
                additionalData.fileList = uploadFileList;
            }



            if (sessionUserRole === vm.role.harbor){

                // 港口确认货物
                if (vm.currentOrder.flowData.status === vm.action.a13FinishedUpload.statusAt){
                    vm.errorHarborConfirmAmount = false;

                    if (!vm.inputHarborConfirmAmount || vm.inputHarborConfirmAmount < 10) {
                        vm.errorHarborConfirmAmount = true;
                        return ;
                    } else {
                        additionalData.harborConfirmAmount =  vm.inputHarborConfirmAmount
                    }
                }

                if (vm.currentOrder.flowData.status === 'repaymentStep32' || vm.currentOrder.flowData.status === 'repaymentStep33'){
                    additionalData.redemptionAmountDeliveryId = tempDeliveryId

                    if (tempDeliveryId){
                        orderService.updateDeliveryInfoById(tempDeliveryId, {}).done(function (data) {
                            if (data.success) {
                            } else {
                                console.log(data.error);
                            }
                        })
                    }
                }
            }


            // 贸易商财务 给出具体放款金额
            if (sessionUserRole === vm.role.traderAccountant){

                if (vm.currentOrder.flowData.status === vm.action.a16traderRecommendAmount.statusAt){
                    vm.errorActualLoanValue = false;

                    if (!vm.inputActualLoanValue || vm.inputActualLoanValue < 10) {
                        vm.errorActualLoanValue = true;
                        return ;
                    } else {
                        additionalData.loanValue = vm.inputActualLoanValue
                    }
                }
                console.log(additionalData, vm.currentOrder.currentSessionUserTaskTaskName)
            }


            if (sessionUserRole === vm.role.financer){

                // 融资方 还款金额

                // 融资方 还款金额
                if (vm.currentOrder.flowData.status === 'financingStep12'){

                }else{
                    vm.errorRepaymentValue = false;

                    if (!vm.inputRepaymentValue || vm.inputRepaymentValue < 10) {
                        vm.errorRepaymentValue = true;
                        return ;

                    } else {
                        var tempLeftValue = vm.currentOrder.loanValue;

                        vm.repaymentList.forEach(function(pay){
                            tempLeftValue = tempLeftValue - pay.redemptionValue;
                        })

                        var tempPaymentOrder = {
                            redemptionValue : vm.inputRepaymentValue,
                            leftPrincipalValue : tempLeftValue - vm.inputRepaymentValue,
                            paymentType  : orderService.paymentTypeKey.repayment,
                            orderId      : orderId,
                            orderNo      : vm.currentOrder.orderNo
                        }

                        additionalData.repaymentValue = vm.inputRepaymentValue

                        orderService.addNewPaymentOrder(tempPaymentOrder).done(function (data) {
                            if (data.success) {
                            } else {
                                console.log(data.error);
                            }
                        })
                    }
                }

            }

            orderService.auditFinanceOrder(orderId, vm.currentOrder.currentSessionUserTaskTaskName, vm.currentOrder.currentSessionUserTaskId, actionName, additionalData).done(function (data) {
                if (data.success) {
                    getOrderInfo();
                    $.notify("提交成功!", 'success');
                } else {
                    console.log(data.error);
                }
            });
        },

        getFile : function (event, file) {
            event.preventDefault();
            orderService.getContractById(file._id);
        },
        deleteFile : function (event, file) {
            event.preventDefault();
            var tempIndex = vm.uploadFileList.indexOf(file);
            if (tempIndex > -1){
                vm.uploadFileList.splice(tempIndex, 1)
                uploadFileList.splice(tempIndex, 1)
            }
        },

        contractFilter : function (el, i, role) {
            // console.log(el, i,role)
            return el.role === role
        },

        userListFilter : function (el, i, role) {
            // console.log(el, i,role)
            return el.role === role
        },

        traderSelectUserList       : [],

        traderForm       : {
            selectedFundProvider                  : '',
            selectedFundProviderAccountant        : '',
            selectedHarbor                        : '',
            selectedSupervisor                    : '',
            selectedFundProviderCompany           : '',
            selectedFundProviderAccountantCompany : '',
            selectedHarborCompany                 : '',
            selectedSupervisorCompany             : ''
        },
        traderFormError  : {
            fundProvider           : '',
            fundProviderAccountant : '',
            harbor                 : '',
            supervisor             : ''
        },



        inputHarborConfirmAmount : 0, // 港口确认货物
        errorHarborConfirmAmount : '',


        inputDepositValue : 0,
        inputDepositMemo : '',
        errorDepositValue : '',
        addNotifyDeposit       : function (event) {
            event.preventDefault();
            vm.errorDepositValue = false;

            if (!vm.inputDepositValue || vm.inputDepositValue < 10) {
                vm.errorDepositValue = true;
            } else {
                var tempPaymentOrder = {
                    flowId      : orderId,
                    expectedAmount : vm.inputDepositValue,
                    state  : orderService.depositTypeKey.notified,
                    memo  : vm.inputDepositMemo
                }

                orderService.addNewDepositOrder(tempPaymentOrder).done(function (data) {
                    if (data.success) {
                        getOrderInfo()
                        $.notify("保存成功!", 'success');
                    } else {
                        console.log(data.error);
                    }
                })
            }
        },

        inputPaymentOrderNo : '',
        errorPaymentOrderNo : '',
        savePaymentOrder : function(id){
            vm.errorPaymentOrderNo = false;

            if (!vm.inputPaymentOrderNo || vm.inputPaymentOrderNo.length < 10) {
                vm.errorPaymentOrderNo = true;
            } else {
                orderService.updatePaymentOrderInfoById(id, {paymentNo:vm.inputPaymentOrderNo, depositType:'alreadyPaid'}).done(function (data) {
                    if (data.success) {
                        getOrderInfo()
                        $.notify("保存成功!", 'success');
                    } else {
                        console.log(data.error);
                    }
                })
            }
        },


        inputFundProviderInterestRate : 0,
        errorFundProviderInterestRate : '',


        inputActualLoanValue : 0,
        errorActualLoanValue : '',


        inputRepaymentValue : 0,
        errorRepaymentValue : '',

        inputRedemptionAmount : 0,
        inputRedemptionFileList : [],
        errorRedemptionAmount : '',
        isNeedDelivery  : false,
        saveRedemptionAmount  : function (event) {
            event.preventDefault();

        }

    });




    function getOrderInfo() {
        orderService.getFinanceOrderInfoById(orderId).done(function (data) {
            if (data.success) {
                vm.contractList = data.data.flowData.fileList;
                vm.currentOrder = data.data;

                upload()

            } else {
                console.log(data.error);
            }
        });
    }

    getOrderInfo();

    function getUsersOfRoles() {

        userService.getUserList({count : 50000}).done(function (data) {
            if (data.success) {
                vm.traderSelectUserList = data.data;
            } else {
                console.log(data.error);
            }
        });
    }


    if (vm.currentUser.role === vm.role.trader) {
        getUsersOfRoles()
    }



    // 上传文件
    function upload(){

        function uploadBeforeSend (block, data, headers) {

            jQuery.extend(data, {
                orderId          : orderId,
                contractUserType : sessionUserRole,
                contractType     : vm.selectedContractType
            });
            jQuery.extend(headers, tokenHeaders);
        }

        function fileQueued (file) {
            console.log(file)
        }

        function uploadError (file) {
            $.notify("上传出现问题!", 'error');
        }

        var uploadSetting = {

            // 选完文件后，是否自动上传。
            auto : true,

            // swf文件路径
            swf : '/static/admin/js/libs/webuploader/Uploader.swf',

            // 文件接收服务端。
            server : API.files,
            // server : '/apz/upload/file',

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick : '#uploadPicker',

            // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
            resize : false
        };

        var uploadSettingRedemptionFile = jQuery.extend({}, uploadSetting, {
            pick : '#uploadPickerRedemptionFile'
        });


        if (vm.currentUser.role === vm.role.trader){

            var uploaderRedemptionFile = WebUploader.create(uploadSettingRedemptionFile);

            uploaderRedemptionFile.on('uploadBeforeSend', uploadBeforeSend);
            uploaderRedemptionFile.on('fileQueued', fileQueued);
            uploaderRedemptionFile.on('uploadError', uploadError);

            uploaderRedemptionFile.on('uploadSuccess', function (file, response) {
                var tempFile = {
                    fileId : response.data._id,
                    path : response.data.fileId,
                    name : file.name,
                    ext  : file.ext,
                    size : file.size,
                    type : file.type
                };
                vm.inputRedemptionFileList.push(tempFile);
                $.notify("上传成功!", 'success');
            });
        }


        if (sessionUserRole === vm.role.financer || sessionUserRole === vm.role.harbor || sessionUserRole === vm.role.supervisor){

            var uploader = WebUploader.create(uploadSetting);

            uploader.on('uploadBeforeSend', uploadBeforeSend);
            uploader.on('fileQueued', fileQueued);
            uploader.on('uploadError', uploadError);

            uploader.on('uploadSuccess', function (file, response) {
                var tempFile = {
                    fileId : response.data._id,
                    path : response.data.fileId,
                    name : file.name,
                    ext  : file.ext,
                    size : file.size,
                    type : file.type
                };

                uploadFileList.push(tempFile)
                vm.uploadFileList.push(tempFile);
                $.notify("上传成功!", 'success');
            });

        }

    }






    //折线图
/*
    var myChart = echarts.init(document.getElementById('main'));
    myChart.setOption({
        title   : {
            text : ''
        },
        tooltip : {
            trigger : 'axis'
        },
        legend  : {
            data : ['当前警戒线', '当前货值', '待还款金额']
        },
        toolbox : {
            feature : {
                saveAsImage : {}
            },
            right   : '4%'
        },
        grid    : {
            left         : '3%',
            right        : '4%',
            bottom       : '3%',
            containLabel : true
        },
        xAxis   : [
            {
                type        : 'category',
                boundaryGap : false,
                data        : ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
            }
        ],
        yAxis   : [
            {
                type : 'value'
            }
        ],
        series  : [

            {
                name      : '当前警戒线',
                type      : 'line',
                stack     : '总量',
                areaStyle : {normal : {opacity : '0'}},
                lineStyle : {normal : {type : 'dotted'}},
                data      : [150, 150, 150, 150, 150, 150, 150]
            },
            {
                name      : '待还款金额',
                type      : 'line',
                stack     : '总量',
                areaStyle : {normal : {opacity : '0'}},
                data      : [320, 332, 301, 334, 390, 330, 320]
            },
            {
                name      : '当前货值',
                type      : 'line',
                stack     : '总量',
                label     : {
                    normal : {
                        show     : true,
                        position : 'top'
                    }
                },
                areaStyle : {normal : {opacity : '0'}},
                data      : [820, 932, 901, 934, 1290, 1330, 1320]
            }
        ]
    });

*/


    if (urlShowStatus === 'orderInfo') {


    }


};

orderInfo();


module.exports = orderInfo;