/**
 * Created by JinWYP on 09/12/2016.
 */

var jQuery = require('jquery');
var headers = require('./token').headers;
var url = require('./token').url;
var role = require('./user.js').userRoleKeyObject;

var status = [
    {name : 'financingStep11', displayName:'等待贸易商选择港口,监管方和资金方'},
    {name : 'financingStep12', displayName:'等待融资方,港口和监管方上传合同及单据'},
    {name : 'financingStep13', displayName:'融资方完成上传合同,待贸易商审核'}, // 不需要
    {name : 'financingStep14', displayName:'港口完成上传合同,待贸易商审核'}, // 不需要
    {name : 'financingStep15', displayName:'监管方完成上传合同,待贸易商审核'}, // 不需要
    {name : 'financingStep51', displayName:'贸易商审核不通过，流程结束'},
    {name : 'financingStep16', displayName:'贸易商审核通过,待贸易商财务放款建议'},
    {name : 'financingStep17', displayName:'贸易商财务放款建议审核通过,待资金方审核'},
    {name : 'financingStep52', displayName:'资金方审核不通过，流程结束'},
    {name : 'financingStep18', displayName:'资金方审核通过,待资金方财务放款'},
    {name : 'financingStep19', displayName:'资金方财务已放款,待贸易商确认收款,银行转账中'},
    {name : 'financingStep20', displayName:'贸易商已自动确认收款,贸易商已自动打款给融资方, 待融资方确认收款,银行转账中'},
    {name : 'financingStep21', displayName:'融资方已自动确认收款,融资放款阶段完成,待融资方回款'},
    {name : 'repaymentStep31', displayName:'融资方已回款,待贸易商放货'},
    {name : 'repaymentStep32', displayName:'贸易商已放货, 融资方部分回款已完成, 待港口放货确认'},
    {name : 'repaymentStep33', displayName:'贸易商已放货, 融资方全部回款已完成, 待港口放货确认'},
    {name : 'repaymentStep34', displayName:'港口已确认返回货物,融资方部分回款已完成, 待融资方继续回款'},
    {name : 'repaymentStep35', displayName:'港口已确认返回货物,融资方全部回款已完成, 待贸易商确认回款给资金方'},
    {name : 'repaymentStep53', displayName:'贸易商已扣押货物（处置货权）,融资方未回款, 待贸易商确认回款给资金方'},
    {name : 'repaymentStep36', displayName:'贸易商已确认回款给资金方,待贸易商财务放款'},
    {name : 'repaymentStep37', displayName:'贸易商财务已回款给资金方，流程结束'}
];

var statusObject = {};

status.forEach(function (item, index){
    statusObject[item.name] = item.displayName;
});

var actions = [
    {statusAt:"financingStep11", operator : 'trader', name : 'a11SelectHarborAndSupervisor', displayName : '完成选择港口,监管方和资金方'},

    {statusAt:"financingStep12", operator : 'financer', name : 'a12FinishedUpload', displayName : '确认完成上传资料并提交'},
    {statusAt:"financingStep12", operator : 'harbor', name : 'a13FinishedUpload', displayName : '确认完成上传资料并已确认货物数量'},
    {statusAt:"financingStep12", operator : 'supervisor', name : 'a14FinishedUpload', displayName : '确认完成上传资料并提交'},

    {statusAt:"financingStep12", operator : 'trader', name : 'a15Approved', displayName : '审核通过'},
    {statusAt:"financingStep12", operator : 'trader', name : 'a16NotApproved', displayName : '审核不通过'},

    {statusAt:"financingStep16", operator : 'traderAccountant', name : 'a17Approved', displayName : '确认放款'},

    {statusAt:"financingStep17", operator : 'fundProvider', name : 'a18Approved', displayName : '审核通过'},
    {statusAt:"financingStep17", operator : 'fundProvider', name : 'a19NotApproved', displayName : '审核不通过'},


    {statusAt:"financingStep18", operator : 'fundProviderAccountant', name : 'a20Approved', displayName : '确认放款'},
    {statusAt:"financingStep19", operator : 'fundProviderAccountant', name : 'a21auto', displayName : '自动确认收款1'},
    {statusAt:"financingStep20", operator : 'fundProviderAccountant', name : 'a22auto', displayName : '自动确认收款2'},


    {statusAt:"financingStep21", operator : 'financer', name : 'a31FirstReturnMoney', displayName : '确认回款'},
    {statusAt:"repaymentStep34", operator : 'financer', name : 'a32SecondReturnMoney', displayName : '确认回款'},

    {statusAt:"repaymentStep31", operator : 'trader', name : 'a32ReturnPortionCargo', displayName : ' 部分回款完成,确认放货'},
    {statusAt:"repaymentStep31", operator : 'trader', name : 'a33ReturnAllCargo', displayName : '全部回款完成,确认放货'},
    {statusAt:"repaymentStep32", operator : 'harbor', name : 'a34ConfirmPortionCargo', displayName : ' 部分回款完成,确认返回货物'},
    {statusAt:"repaymentStep33", operator : 'harbor', name : 'a35ConfirmAllCargo', displayName : '全部回款完成,确认返回货物'},

    {statusAt:"repaymentStep35", operator : 'trader', name : 'a36ReturnMoney', displayName : '确认回款给资金方'},
    {statusAt:"repaymentStep36", operator : 'traderAccountant', name : 'a37Approved', displayName : '放款给资金方'},

    {statusAt:"financingStep21", operator : 'trader', name : 'a37Punishment', displayName : '扣押货物(处置货权)'},
    {statusAt:"repaymentStep34", operator : 'trader', name : 'a38Punishment', displayName : '扣押货物(处置货权)'}
];

var actionObject = {};

actions.forEach(function (item, index){
    actionObject[item.name] = item;
});


var contractType = {
    contract : '合同',
    finance  : '财务单据',
    business : '业务单据类(质量和数量单据, 运输单据, 货转证明)'
}

var paymentType = [
    { name : 'repayment',  displayName : '还款'},
    { name : 'deposit',  displayName : '保证金'}
]
var paymentTypeObject = {};
var paymentTypeKeyObject = {};

paymentType.forEach(function (type, index){
    paymentTypeObject[type.name] = type.displayName;
    paymentTypeKeyObject[type.name] = type.name;
});


var depositType = {
    notified    : '保证金已通知',
    alreadyPaid : '保证金已缴纳',
    transferred : '保证金已到账'
}

exports.statusList   = status;
exports.statusObject = statusObject;
exports.actionList   = actions;
exports.actionObject = actionObject;
exports.contractType = contractType;
exports.paymentType  = paymentTypeObject;
exports.paymentTypeKey  = paymentTypeKeyObject;
exports.depositType  = depositType;





exports.getFinanceOrderList = function (query){

    var user = {};

    if (typeof query.userRole !== 'undefined'){

        if (query.userRole === role.financer) {
            user = {financerUserId : query.userId}
        }
        if (query.userRole === role.harbor) {
            user = {harborUserId : query.userId}
        }
        if (query.userRole === role.supervisor) {
            user = {supervisorUserId : query.userId}
        }
        if (query.userRole === role.trader) {
            user = {traderUserId : query.userId}
        }
        if (query.userRole === role.traderAccountant) {
            user = {traderAccountantUserId : query.userId}
        }
        if (query.userRole === role.fundProvider) {
            user = {fundProviderUserId : query.userId}
        }
        if (query.userRole === role.fundProviderAccountant) {
            user = {fundProviderAccountantUserId : query.userId}
        }

    }

    delete query.userRole;
    delete query.userId;

    var params = jQuery.extend({}, query, user);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.financeOrderList,
        method   : 'GET',
        data     : params

    });

};

exports.getFinanceOrderInfoById = function (id, query){

    var params = jQuery.extend({}, query);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.financeOrderList + '/' + id,
        method   : 'GET',
        data     : params

    });

};

exports.addNewFinanceOrder = function (order){

    var params = jQuery.extend({}, order);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.financeOrderList,
        method   : 'POST',
        data     : JSON.stringify(params)

    });

};

exports.auditFinanceOrder = function (orderId, userRole, actionName, additionalData){
    console.log("流程:%s, 角色 %s 发出的动作: %s", orderId, userRole, actionName)
    var params = jQuery.extend({}, {
        "taskId": orderId,
        "flowId": orderId,
        "orderId": orderId,
        "action": actionName,
        "operator": userRole
        // "harborUserId": "583ea0b1f17d22ecde1ecb17",
        // "supervisorUserId": "583fc370e6e14eedaa51d2a0",
        // "fundProviderUserId": "583fd13e75a02a0f2935374e",
        // "fundProviderAccountantUserId": "583fd178551ff10f40108c8c"
    });

    if (additionalData && additionalData.harborUserId) params.harborUserId = additionalData.harborUserId;
    if (additionalData && additionalData.supervisorUserId) params.supervisorUserId = additionalData.supervisorUserId;
    if (additionalData && additionalData.fundProviderUserId) params.fundProviderUserId = additionalData.fundProviderUserId;
    if (additionalData && additionalData.fundProviderAccountantUserId) params.fundProviderAccountantUserId = additionalData.fundProviderAccountantUserId;


    if (additionalData && additionalData.fileList) params.fileList = additionalData.fileList;
    if (additionalData && additionalData.harborConfirmAmount) params.harborConfirmAmount = additionalData.harborConfirmAmount;

    if (additionalData && additionalData.loanValue) params.loanValue = additionalData.loanValue;

    if (additionalData && additionalData.repaymentValue) params.repaymentValue = additionalData.redemptionValue;
    if (additionalData && additionalData.redemptionAmount) params.redemptionAmount = additionalData.redemptionAmount;
    if (additionalData && additionalData.redemptionAmountDeliveryId) params.redemptionAmountDeliveryId = additionalData.redemptionAmountDeliveryId;

    if (actionName === 'a15Approved' || actionName === 'a18Approved') {
        params.approveStatus = 1
    }
    if (actionName === 'a16NotApproved' || actionName === 'a19NotApproved') {
        params.approveStatus = 0
    }

    if (actionName === 'a17Approved' || actionName === 'a20Approved' || actionName === 'a36ReturnMoney' || actionName === 'a37Approved') {
        params.status = 1
    }

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.financeOrderList + '/task',
        method   : 'POST',
        data     :JSON.stringify(params)

    });

};


exports.updateFinanceOrderInfoById = function (id, order){

    var params = jQuery.extend({}, order);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.financeOrderList + '/' + id,
        method   : 'PATCH',
        data     : JSON.stringify(params)

    });

};




exports.getContractListByOrderId = function (orderId, query){

    var params = jQuery.extend({}, query, {orderId : orderId});

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.contractList,
        method   : 'GET',
        data     : params

    });
};
exports.getContractById = function (id, query){
    var params = jQuery.extend({}, query);

    window.location = url.financeOrderList + '/file/' + id;

};





exports.getPaymentOrderListByOrderId = function (orderId, query){

    var params = jQuery.extend({}, query, {orderId : orderId});

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.paymentOrderList,
        method   : 'GET',
        data     : params

    });
};
exports.addNewPaymentOrder = function (order){

    var params = jQuery.extend({}, order);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.paymentOrderList,
        method   : 'POST',
        data     : JSON.stringify(params)

    });

};
exports.updatePaymentOrderInfoById = function (id, order){

    var params = jQuery.extend({}, order);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.paymentOrderList + '/' + id,
        method   : 'PATCH',
        data     : JSON.stringify(params)

    });

};



exports.getDeliveryListByOrderId = function (orderId, query){

    var params = jQuery.extend({}, query, {orderId : orderId});

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.deliveryList,
        method   : 'GET',
        data     : params

    });
};
exports.addNewDelivery = function (order){

    var params = jQuery.extend({}, order);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.deliveryList,
        method   : 'POST',
        data     : JSON.stringify(params)

    });
};
exports.updateDeliveryInfoById = function (id, order){

    var params = jQuery.extend({}, order);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.deliveryList + '/' + id,
        method   : 'PATCH',
        data     : JSON.stringify(params)

    });

};
