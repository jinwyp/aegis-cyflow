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
    {name : 'financingStep13', displayName:'融资方, 港口和监管方完成上传合同,待贸易商审核'},
    // {name : 'financingStep14', displayName:'港口完成上传合同,待贸易商审核'}, // 不需要
    // {name : 'financingStep15', displayName:'监管方完成上传合同,待贸易商审核'}, // 不需要
    {name : 'TraderDisapprove', displayName:'贸易商审核不通过，流程结束'},
    {name : 'financingStep14', displayName:'贸易商审核通过,待贸易商财务放款建议'},
    {name : 'financingStep15', displayName:'贸易商财务放款建议审核通过,待资金方审核'},
    {name : 'FundProviderDisapprove', displayName:'资金方审核不通过，流程结束'},
    {name : 'financingStep16', displayName:'资金方审核通过,待资金方财务放款'},
    {name : 'financingStep17', displayName:'资金方财务已放款,待贸易商确认收款,银行转账中'},

    {name : 'financingStep18', displayName:'贸易商已自动确认收款,贸易商已自动打款给融资方, 待融资方确认收款,银行转账中'},

    {name : 'financingStep19', displayName:'融资方已自动确认收款,融资放款阶段完成,待融资方回款'},
    {name : 'repaymentStep20', displayName:'融资方已回款,待贸易商自动确认收款,银行转账中'},
    {name : 'repaymentStep21', displayName:'融资方已回款,待贸易商放货'},

    {name : 'repaymentStep22', displayName:'贸易商已放货, 融资方回款完成, 待港口放货确认'},
    {name : 'repaymentStep23', displayName:'港口已完成放货, 待贸易商确认还款是否完成'},



    // {name : 'repaymentStep32', displayName:'贸易商已放货, 融资方部分回款已完成, 待港口放货确认'},
    // {name : 'repaymentStep33', displayName:'贸易商已放货, 融资方全部回款已完成, 待港口放货确认'},
    // {name : 'repaymentStep34', displayName:'港口已确认返回货物,融资方部分回款已完成, 待融资方继续回款'},
    {name : 'repaymentStep24', displayName:'融资方全部回款已完成, 待贸易商确认回款给资金方'},
    {name : 'repaymentStep53', displayName:'贸易商已扣押货物（处置货权）,融资方未回款, 待贸易商确认回款给资金方'},
    {name : 'repaymentStep25', displayName:'贸易商已确认回款给资金方,待贸易商财务放款'},
    {name : 'repaymentStep26', displayName:'贸易商财务已放款给资金方，银行转账中'},
    {name : 'success', displayName:'贸易商财务已完成回款给资金方，流程结束'}
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

    {statusAt:"financingStep13", operator : 'trader', name : 'a15Approved', displayName : '审核通过'},
    {statusAt:"financingStep13", operator : 'trader', name : 'a16NotApproved', displayName : '审核不通过'},

    {statusAt:"financingStep14", operator : 'traderAccountant', name : 'a16traderRecommendAmount', displayName : '确认放款'},

    {statusAt:"financingStep15", operator : 'fundProvider', name : 'a17fundProviderAudit', displayName : '审核通过'},
    {statusAt:"financingStep15", operator : 'fundProvider', name : 'a19NotApproved', displayName : '审核不通过'},

    {statusAt:"financingStep16", operator : 'fundProviderAccountant', name : 'a18fundProviderAccountantAudit', displayName : '确认放款'},
    // {statusAt:"financingStep19", operator : 'fundProviderAccountant', name : 'a21auto', displayName : '自动确认收款1'},
    // {statusAt:"financingStep20", operator : 'fundProviderAccountant', name : 'a22auto', displayName : '自动确认收款2'},


    // {statusAt:"financingStep19", operator : 'financer', name : 'a31FirstReturnMoney', displayName : '确认还款'},
    {statusAt:"financingStep19", operator : 'financer', name : 'a19SecondReturnMoney', displayName : '确认还款'},

    {statusAt:"repaymentStep21", operator : 'trader', name : 'a20noticeHarborRelease', displayName : ' 回款完成,确认放货'},

    {statusAt:"repaymentStep22", operator : 'harbor', name : 'a21harborRelease', displayName : ' 部分回款完成,确认返回货物'},
    // {statusAt:"repaymentStep33", operator : 'harbor', name : 'a35ConfirmAllCargo', displayName : '全部回款完成,确认返回货物'},

    {statusAt:"repaymentStep23", operator : 'trader', name : 'a22traderAuditIfComplete', displayName : '确认回款部分完成, 需要融资方继续还款'},
    // {statusAt:"repaymentStep23", operator : 'trader', name : 'a22traderAuditIfComplete', displayName : '确认回款全部完成'},



    {statusAt:"repaymentStep24", operator : 'trader', name : 'a23ReturnMoney', displayName : '确认回款给资金方'},
    {statusAt:"repaymentStep25", operator : 'traderAccountant', name : 'a24AccountantReturnMoney', displayName : '确认放款给资金方'},

    {statusAt:"financingStep21", operator : 'trader', name : 'a37Punishment', displayName : '扣押货物(处置货权)'},
    {statusAt:"repaymentStep34", operator : 'trader', name : 'a38Punishment', displayName : '扣押货物(处置货权)'}
];

var actionObject = {};

actions.forEach(function (item, index){
    actionObject[item.name] = item;
});


var contractType = {
    default : '-',
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
var depositTypeKeyObject = {
    notified    : 'notified',
    alreadyPaid : 'alreadyPaid',
    transferred : 'transferred'
};


exports.statusList   = status;
exports.statusObject = statusObject;
exports.actionList   = actions;
exports.actionObject = actionObject;
exports.contractType = contractType;
exports.paymentType  = paymentTypeObject;
exports.paymentTypeKey  = paymentTypeKeyObject;
exports.depositType  = depositType;
exports.depositTypeKey  = depositTypeKeyObject;





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

exports.auditFinanceOrder = function (flowId, taskName, taskId, actionName, additionalData){
    console.log("在流程 %s, 中的任务 %s , 发出的动作: %s", flowId, taskName, actionName)
    var params = jQuery.extend({}, {
        "taskId": taskId,
        "flowId": flowId
    });

    if (additionalData && additionalData.harborUserId) params.harborUserId = additionalData.harborUserId;
    if (additionalData && additionalData.harborCompanyId) params.harborCompanyId = additionalData.harborCompanyId;
    if (additionalData && additionalData.supervisorUserId) params.supervisorUserId = additionalData.supervisorUserId;
    if (additionalData && additionalData.supervisorCompanyId) params.supervisorCompanyId = additionalData.supervisorCompanyId;
    if (additionalData && additionalData.fundProviderUserId) params.fundProviderUserId = additionalData.fundProviderUserId;
    if (additionalData && additionalData.fundProviderCompanyId) params.fundProviderCompanyId = additionalData.fundProviderCompanyId;
    if (additionalData && additionalData.fundProviderAccountantUserId) params.fundProviderAccountantUserId = additionalData.fundProviderAccountantUserId;
    if (additionalData && additionalData.fundProviderAccountantCompanyId) params.fundProviderAccountantCompanyId = additionalData.fundProviderAccountantCompanyId;


    if (additionalData && additionalData.fileList) params.fileList = additionalData.fileList;
    if (additionalData && additionalData.harborConfirmAmount) params.harborConfirmAmount = additionalData.harborConfirmAmount;

    if (additionalData && additionalData.fundProviderInterestRate) params.fundProviderInterestRate = additionalData.fundProviderInterestRate;
    if (additionalData && additionalData.loanValue) params.loanValue = additionalData.loanValue;

    if (additionalData && additionalData.repaymentValue) params.repaymentValue = additionalData.repaymentValue;
    if (additionalData && additionalData.redemptionAmount) params.redemptionAmount = additionalData.redemptionAmount;
    if (additionalData && additionalData.goodsReceiveCompanyName) params.goodsReceiveCompanyName = additionalData.goodsReceiveCompanyName;

    if (additionalData && additionalData.approvedStatus === 0) params.approvedStatus = 0;
    if (additionalData && additionalData.approvedStatus === 1) params.approvedStatus = 1;

    if (additionalData && additionalData.status === 0) params.status = 0;
    if (additionalData && additionalData.status === 1) params.status = 1;


    if (additionalData && additionalData.redemptionAmountDeliveryId) params.redemptionAmountDeliveryId = additionalData.redemptionAmountDeliveryId;




    if (actionName === 'a18fundProviderAccountantAudit' || actionName === 'a21harborRelease' || actionName === 'a23ReturnMoney' || actionName === 'a24AccountantReturnMoney') {
        params.status = 1
    }

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.financeOrderList + '/action/' + taskName,
        method   : 'POST',
        data     :JSON.stringify(params)

    });

};






exports.addNewDepositOrder = function (order){

    var params = jQuery.extend({}, order);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.depositList,
        method   : 'POST',
        data     : JSON.stringify(params)

    });

};


exports.updateDepositOrderInfoById = function (order){

    var params = jQuery.extend({}, order);

    var urlTemp = url.depositList + '/' + order.flowId + '?state=' + order.state

    if (order.amount){
        urlTemp = urlTemp + '&amount=' + order.amount
    }
    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : urlTemp,
        method   : 'PUT',
        data     : JSON.stringify(params)

    });

};



exports.getFileById = function (id, query){
    var params = jQuery.extend({}, query);

    window.location = url.files + '/' + id;

};






