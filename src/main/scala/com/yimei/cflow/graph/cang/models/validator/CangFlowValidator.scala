package com.yimei.cflow.graph.cang.models.validator

import com.wix.accord.Validator
import com.wix.accord.dsl._
import com.yimei.cflow.graph.cang.models.CangFlowModel.{CustomerPaymentToTrafficker, CustomerUploadContract, FileObj, FundProviderAudit, FundProviderFinanceLoad, PortReleaseGoods, PortUploadContract, StartFlow, SupervisorUploadContract, TraffickerAssignUsers, TraffickerAudit, TraffickerAuditIfCompletePayment, TraffickerConfirmPayToFundProvider, TraffickerFinanceAudit, TraffickerFinancePayToFundProvider, TraffickerNoticePortReleaseGoods}

object CangFlowValidator {

  /** 文件 **/
  implicit val fileObjValidator: Validator[FileObj] =
    validator[FileObj] {
      fileObj =>
        fileObj.name as "文件名称" is notEmpty
        fileObj.url as "文件路径" is notEmpty
        fileObj.name.length as "文件名称字段长度" is between(1, 100)
        fileObj.url.length as "文件路径字段长度" is between(1, 200)
    }


  /** 开始流程 **/
  implicit val startFlowValidator: Validator[StartFlow] =
    validator[StartFlow] {
      startFlow =>
        startFlow.applyCompanyId as "融资方公司id" min(1)
        startFlow.applyCompanyName as "融资方公司名称" is notEmpty
        startFlow.applyCompanyName.length as "融资方公司名称字段长度" is between(1, 100)
        startFlow.applyUserId as "融资方用户id" min(1)
        startFlow.applyUserName as "融资方用户姓名" is notEmpty
        startFlow.applyUserName.length as "融资方用户姓名字段长度" is between(1, 10)
        startFlow.applyUserPhone as "融资方用户手机号" is notEmpty
        startFlow.applyUserPhone.length as "融资方用户手机号字段长度" is between(10, 20)
        startFlow.auditFileList.each is valid
        startFlow.businessCode as "业务编号" is notEmpty
        startFlow.businessCode.length as "业务编号字段长度" max(20)
        startFlow.coalAmount as "总质押吨数" is between(BigDecimal.valueOf(1), BigDecimal.valueOf(100000000))
        startFlow.coalIndex_ADV as "空干基挥发分" must(between(BigDecimal.valueOf(0.01), BigDecimal.valueOf(50)))
        startFlow.coalIndex_NCV as "热值" is between(1, 7500)
        startFlow.coalIndex_RS as "硫分" is between(BigDecimal.valueOf(0.01), BigDecimal.valueOf(10))
        startFlow.coalType as "煤炭种类" is notEmpty
        startFlow.coalType.length as "煤炭种类字段长度" must(between(1, 100))
        startFlow.downstreamCompanyName as "下游签约单位公司名称" is notEmpty
        startFlow.downstreamCompanyName.length as "下游签约单位公司名称字段长度" is between(1, 100)
        startFlow.financeCreateTime as "审批开始时间" is notNull
        startFlow.financeEndTime as "审批完成时间" is notNull
        startFlow.financingAmount as "拟融资金额" is notNull
        startFlow.financingAmount as "拟融资金额" is between(BigDecimal.valueOf(1), BigDecimal.valueOf(100000000))
        startFlow.financingDays as "拟融资天数" min(1)
        startFlow.interestRate as "利率" is notNull
        startFlow.interestRate as "利率" is between(BigDecimal.valueOf(0), BigDecimal.valueOf(100))
        startFlow.stockPort as "库存港口" is notEmpty
        startFlow.stockPort.length as "库存港口字段长度" is between(1, 200)
    }


  /** 贸易商选择 港口, 监管, 资金方 业务人员, 财务 **/
  implicit val traffickerAssignUsersValidator: Validator[TraffickerAssignUsers] =
    validator[TraffickerAssignUsers] {
      traffickerAssignUsers =>
        traffickerAssignUsers.taskId as "任务id" is notEmpty
        traffickerAssignUsers.taskId.length as "任务id字段" max(10)
        traffickerAssignUsers.portCompanyId as "港口公司id" is notEmpty
        traffickerAssignUsers.portUserId as "港口用户id" is notEmpty
        traffickerAssignUsers.supervisorCompanyId as "监管公司id" is notEmpty
        traffickerAssignUsers.supervisorUserId as "监管用户id" is notEmpty
        traffickerAssignUsers.fundProviderCompanyId as "资金方公司id" is notEmpty
    }

  /** 融资方上传 合同, 财务, 业务 文件 **/
  implicit val customerUploadContractValidator: Validator[CustomerUploadContract] =
    validator[CustomerUploadContract] {
      customerUploadContract =>
        customerUploadContract.taskId as "任务id" is notEmpty
        customerUploadContract.taskId.length as "任务id字段" max(10)
        customerUploadContract.businessFileList.each is valid
        customerUploadContract.contractFileList.each is valid
        customerUploadContract.financeFileList.each is valid
    }

  /** 监管方上传合同 **/
  implicit val supervisorUploadContractValidator: Validator[SupervisorUploadContract] =
    validator[SupervisorUploadContract] {
      supervisorUploadContract =>
        supervisorUploadContract.taskId as "任务id" is notEmpty
        supervisorUploadContract.taskId.length as "任务id字段" max(10)
        supervisorUploadContract.contractFileList.each is valid
    }

  /** 港口上传合同 **/
  implicit val portUploadContractValidator: Validator[PortUploadContract] =
    validator[PortUploadContract] {
      portUploadContract =>
        portUploadContract.taskId as "任务id" is notEmpty
        portUploadContract.taskId.length as "任务id字段" max(10)
        portUploadContract.confirmCoalAmount as "确认吨数" is notNull
        portUploadContract.confirmCoalAmount as "确认吨数" is between(BigDecimal.valueOf(1), BigDecimal.valueOf(100000000))
        portUploadContract.contractFileList.each is valid
    }

  /** 贸易商审核 **/
  implicit val traffickerAuditValidator: Validator[TraffickerAudit] =
    validator[TraffickerAudit] {
      traffickerAssignUsers =>
        traffickerAssignUsers.taskId as "任务id" is notEmpty
        traffickerAssignUsers.taskId.length as "任务id字段" max(10)
        traffickerAssignUsers.statusId as "审核状态id" min(0)
        traffickerAssignUsers.statusId as "审核状态id" max(1)
        traffickerAssignUsers.fundProviderInterestRate as "资金方利率" is notNull
        traffickerAssignUsers.fundProviderInterestRate as "资金方利率" is between(BigDecimal.valueOf(0), BigDecimal.valueOf(100))
    }

  /** 贸易商财务给出放款建议 **/
  implicit val traffickerFinanceAuditValidator: Validator[TraffickerFinanceAudit] =
    validator[TraffickerFinanceAudit] {
      traffickerFinanceAudit =>
        traffickerFinanceAudit.taskId as "任务id" is notEmpty
        traffickerFinanceAudit.taskId.length as "任务id字段" max(10)
        traffickerFinanceAudit.confirmFinancingAmount as "确认放款金额" is notNull
        traffickerFinanceAudit.confirmFinancingAmount as "确认放款金额" is between(BigDecimal.valueOf(1), BigDecimal.valueOf(100000000))
        traffickerFinanceAudit.financingAdvice.length as "放款建议字段长度" is between(0, 500)
    }

  /** 资金方审核 **/
  implicit val fundProviderAuditValidator: Validator[FundProviderAudit] =
    validator[FundProviderAudit] {
      fundProviderAudit =>
        fundProviderAudit.taskId as "任务id" is notEmpty
        fundProviderAudit.taskId.length as "任务id字段" max(10)
        fundProviderAudit.statusId as "审核状态id" min(0)
        fundProviderAudit.statusId as "审核状态id" max(0)
    }

  /** 资金方财务放款 **/
  implicit val fundProviderFinanceLoadValidator: Validator[FundProviderFinanceLoad] =
    validator[FundProviderFinanceLoad] {
      fundProviderFinanceLoad =>
        fundProviderFinanceLoad.taskId as "任务id" is notEmpty
        fundProviderFinanceLoad.taskId.length as "任务id字段" max(10)
        fundProviderFinanceLoad.statusId as "放款状态id" min(0)
        fundProviderFinanceLoad.statusId as "放款状态id" max(1)
    }

  /** 融资方付款给贸易商 **/
  implicit val customerPaymentToTraffickerValidator: Validator[CustomerPaymentToTrafficker] =
    validator[CustomerPaymentToTrafficker] {
      customerPaymentToTrafficker =>
        customerPaymentToTrafficker.taskId as "任务id" is notEmpty
        customerPaymentToTrafficker.taskId.length as "任务id字段" max(10)
        customerPaymentToTrafficker.statusId as "付款状态id" min(0)
        customerPaymentToTrafficker.statusId as "付款状态id" max(1)
        customerPaymentToTrafficker.paymentPrinciple as "付款本金" is notNull
    }

  /** 贸易商通知港口放货 **/
  implicit val traffickerNoticePortReleaseGoodsValidator: Validator[TraffickerNoticePortReleaseGoods] =
    validator[TraffickerNoticePortReleaseGoods] {
      traffickerNoticePortReleaseGoods =>
        traffickerNoticePortReleaseGoods.taskId as "任务id" is notEmpty
        traffickerNoticePortReleaseGoods.taskId.length as "任务id字段" max(10)
        traffickerNoticePortReleaseGoods.goodsFileList.each is valid
        traffickerNoticePortReleaseGoods.releaseAmount as "放货吨数" is notNull
        traffickerNoticePortReleaseGoods.releaseAmount as "放货吨数" is between(BigDecimal.valueOf(0), BigDecimal.valueOf(100000000))
        traffickerNoticePortReleaseGoods.goodsReceiveCompanyName as "接收方公司名称" is notEmpty
    }

  /** 港口放货 **/
  implicit val portReleaseGoodsValidator: Validator[PortReleaseGoods] =
    validator[PortReleaseGoods] {
      portReleaseGoods =>
        portReleaseGoods.taskId as "任务id" is notEmpty
        portReleaseGoods.taskId.length as "任务id字段" max(10)
        portReleaseGoods.statusId as "放货状态id" min(0)
        portReleaseGoods.statusId as "放货状态id" max(1)
    }

  /** 贸易商审核是否回款完成 **/
  implicit val traffickerAuditIfCompletePaymentValidator: Validator[TraffickerAuditIfCompletePayment] =
    validator[TraffickerAuditIfCompletePayment] {
      traffickerAuditIfCompletePayment =>
        traffickerAuditIfCompletePayment.taskId as "任务id" is notEmpty
        traffickerAuditIfCompletePayment.taskId.length as "任务id字段" max(10)
        traffickerAuditIfCompletePayment.statusId as "审核状态id" min(0)
        traffickerAuditIfCompletePayment.statusId as "审核状态id" max(1)
    }

  /** 贸易商同意付款给资金方 **/
  implicit val traffickerConfirmPayToFundProviderValidator: Validator[TraffickerConfirmPayToFundProvider] =
    validator[TraffickerConfirmPayToFundProvider] {
      traffickerConfirmPayToFundProvider =>
        traffickerConfirmPayToFundProvider.taskId as "任务id" is notEmpty
        traffickerConfirmPayToFundProvider.taskId.length as "任务id字段" max(10)
        traffickerConfirmPayToFundProvider.statusId as "审核状态id" min(0)
        traffickerConfirmPayToFundProvider.statusId as "审核状态id" max(1)
    }

  /** 贸易商财务放款给资金方,流程结束 **/
  implicit val traffickerFinancePayToFundProviderValidator: Validator[TraffickerFinancePayToFundProvider] =
    validator[TraffickerFinancePayToFundProvider] {
      traffickerFinancePayToFundProvider =>
        traffickerFinancePayToFundProvider.taskId as "任务id" is notEmpty
        traffickerFinancePayToFundProvider.taskId.length as "任务id字段" max(10)
        traffickerFinancePayToFundProvider.statusId as "付款状态id" min(0)
        traffickerFinancePayToFundProvider.statusId as "付款状态id" max(1)
    }

}