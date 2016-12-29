package com.yimei.cflow.graph.cang.models.validator

import com.wix.accord.Validator
import com.wix.accord.dsl._
import com.yimei.cflow.graph.cang.models.CangFlowModel.{CustomerPaymentToTrafficker, FileObj, FundProviderAudit, FundProviderFinanceLoad, HarborUploadContract, PortReleaseGoods, StartFlowBasicInfo, StartFlowInvestigationInfo, StartFlowSupervisorInfo, TraffickerAssignUsers, TraffickerAudit, TraffickerAuditIfCompletePayment, TraffickerConfirmPayToFundProvider, TraffickerFinanceAudit, TraffickerFinancePayToFundProvider, TraffickerNoticePortReleaseGoods, UploadContract}

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

  /** 开始流程 - 基本信息 **/
  implicit val startFlowBasicInfoValidator: Validator[StartFlowBasicInfo] =
    validator[StartFlowBasicInfo] {
      basicInfo =>
        basicInfo.applyCompanyId as "融资方公司id" min(1)
        basicInfo.applyCompanyName as "融资方公司名称" is notEmpty
        basicInfo.applyCompanyName.length as "融资方公司名称字段长度" is between(1, 100)
        basicInfo.applyUserId as "融资方用户id" min(1)
        basicInfo.applyUserName as "融资方用户姓名" is notEmpty
        basicInfo.applyUserName.length as "融资方用户姓名字段长度" is between(1, 10)
        basicInfo.applyUserPhone as "融资方用户手机号" is notEmpty
        basicInfo.applyUserPhone.length as "融资方用户手机号字段长度" is between(10, 20)
        basicInfo.auditFileList.each is valid
        basicInfo.businessCode as "业务编号" is notEmpty
        basicInfo.businessCode.length as "业务编号字段长度" max(20)
        basicInfo.coalAmount as "总质押吨数" is between(BigDecimal.valueOf(1), BigDecimal.valueOf(100000000))
        basicInfo.coalIndex_ADV as "空干基挥发分" must(between(BigDecimal.valueOf(0.01), BigDecimal.valueOf(50)))
        basicInfo.coalIndex_NCV as "热值" is between(1, 7500)
        basicInfo.coalIndex_RS as "硫分" is between(BigDecimal.valueOf(0.01), BigDecimal.valueOf(10))
        basicInfo.coalType as "煤炭种类" is notEmpty
        basicInfo.coalType.length as "煤炭种类字段长度" must(between(1, 100))
        basicInfo.downstreamCompanyName as "下游签约单位公司名称" is notEmpty
        basicInfo.downstreamCompanyName.length as "下游签约单位公司名称字段长度" is between(1, 100)
        basicInfo.financeCreateTime as "审批开始时间" is notNull
        basicInfo.financeEndTime as "审批完成时间" is notNull
        basicInfo.financingAmount as "拟融资金额" is notNull
        basicInfo.financingAmount as "拟融资金额" is between(BigDecimal.valueOf(1), BigDecimal.valueOf(100000000))
        basicInfo.financingDays as "拟融资天数" min(1)
        basicInfo.financingDays as "拟融资天数" max(3650)
        basicInfo.interestRate as "利率" is notNull
        basicInfo.interestRate as "利率" is between(BigDecimal.valueOf(0), BigDecimal.valueOf(100))
        basicInfo.stockPort as "库存港口" is notEmpty
        basicInfo.stockPort.length as "库存港口字段长度" is between(1, 200)
        basicInfo.upstreamContractNo as "上游合同编号" is notEmpty
        basicInfo.upstreamContractNo.length as "上游合同编号字段长度" is between(1, 30)
        basicInfo.downstreamContractNo as "下游合同编号" is notEmpty
        basicInfo.downstreamCompanyName.length as "下游合同编号字段长度" is between(1, 30)
    }

  /** 开始流程 - 尽调报告信息 **/
  implicit val startFlowInvestigationInfoValidator: Validator[StartFlowInvestigationInfo] =
    validator[StartFlowInvestigationInfo] {
      investigationInfo =>
        investigationInfo.applyCompanyName as "融资方公司名称" is notEmpty
        investigationInfo.applyCompanyName.length as "融资方公司名称字段长度" is between(1, 100)
        investigationInfo.businessRiskPoint.length as "业务风险点字段长度" is between(1, 5000)
        investigationInfo.businessTransferInfo.length as "业务流转信息字段长度" is between(1, 5000)
        investigationInfo.downstreamContractCompany.length as "下游签约单位名称字段长度" is between(1, 100)
        investigationInfo.finalConclusion.length as "综合意见字段长度" is between(1, 5000)
        investigationInfo.financingAmount as "尽调报告中融资金额" is between(BigDecimal.valueOf(1), BigDecimal.valueOf(100000000))
        investigationInfo.financingPeriod as "融资期限" max(3650)
        investigationInfo.historicalCooperationDetail.length as "历史合作情况字段长度" is between(1, 5000)
        investigationInfo.interestRate as "利率" is notNull
        investigationInfo.interestRate as "利率" is between(BigDecimal.valueOf(0), BigDecimal.valueOf(100))
        investigationInfo.mainBusinessInfo.length as "业务主要信息" is between(1, 5000)
        investigationInfo.ourContractCompany.length as "我方签约公司字段长度" is between(1, 100)
        investigationInfo.performanceCreditAbilityEval.length as "履约信用及能力评估字段长度" is between(1, 5000)
        investigationInfo.qualityInspectionUnit.length as "质量检验单位字段长度" is between(1, 100)
        investigationInfo.quantityInspectionUnit.length as "数量检验单位字段长度" is between(1, 100)
        investigationInfo.terminalServer.length as "终端客户字段长度" is between(1, 100)
        investigationInfo.transitPort.length as "中转港口字段长度" is between(1, 100)
        investigationInfo.transportParty.length as "中转方字段长度" is between(1, 100)
        investigationInfo.upstreamContractCompany.length as "上游签约单位名称字段长度" is between(1, 100)
    }

  /** 开始流程 - 监管报告信息 **/
  implicit val startFlowSupervisorInfoValidator: Validator[StartFlowSupervisorInfo] =
    validator[StartFlowSupervisorInfo] {
      supervisorInfo =>
        supervisorInfo.finalConclusion.length as "综合意见字段长度" is between(1, 5000)
        supervisorInfo.historicalCooperationDetail.length as "历史合作情况字段长度" is between(1, 5000)
        supervisorInfo.operatingStorageDetail.length as "经营及堆存情况字段长度" is between(1, 5000)
        supervisorInfo.storageAddress.length as "仓储地地址字段长度" is between(1, 200)
        supervisorInfo.storageLocation.length as "仓储地字段长度" is between(1, 100)
        supervisorInfo.storageProperty.length as "仓储性质字段长度" is between(1, 100)
        supervisorInfo.portStandardDegree.length as "保管及进出口流程规范程度字段长度" is between(1, 5000)
        supervisorInfo.supervisionCooperateDetail.length as "监管配合情况字段长度" is between(1, 5000)
        supervisorInfo.supervisionScheme.length as "监管方案字段长度" is between(1, 5000)
    }

  /** 贸易商选择 港口, 监管, 资金方 业务人员, 财务 **/
  implicit val traffickerAssignUsersValidator: Validator[TraffickerAssignUsers] =
    validator[TraffickerAssignUsers] {
      traffickerAssignUsers =>
        traffickerAssignUsers.taskId as "任务id" is notEmpty
        traffickerAssignUsers.taskId.length as "任务id字段" max(10)
        traffickerAssignUsers.harborCompanyId as "港口公司id" is notEmpty
        traffickerAssignUsers.harborUserId as "港口用户id" is notEmpty
        traffickerAssignUsers.supervisorCompanyId as "监管公司id" is notEmpty
        traffickerAssignUsers.supervisorUserId as "监管用户id" is notEmpty
        traffickerAssignUsers.fundProviderCompanyId as "资金方公司id" is notEmpty
    }

  /** 融资方上传 合同, 财务, 业务 文件 **/
  /** 监管方上传合同 **/
  implicit val customerUploadContractValidator: Validator[UploadContract] =
    validator[UploadContract] {
      customerUploadContract =>
        customerUploadContract.taskId as "任务id" is notEmpty
        customerUploadContract.taskId.length as "任务id字段" max(10)
        customerUploadContract.FileList.each is valid
    }


//  implicit val supervisorUploadContractValidator: Validator[SupervisorUploadContract] =
//    validator[SupervisorUploadContract] {
//      supervisorUploadContract =>
//        supervisorUploadContract.taskId as "任务id" is notEmpty
//        supervisorUploadContract.taskId.length as "任务id字段" max(10)
//        supervisorUploadContract.FileList.each is valid
//    }

  /** 港口上传合同 **/
  implicit val portUploadContractValidator: Validator[HarborUploadContract] =
    validator[HarborUploadContract] {
      portUploadContract =>
        portUploadContract.taskId as "任务id" is notEmpty
        portUploadContract.taskId.length as "任务id字段" max(10)
        portUploadContract.confirmCoalAmount as "确认吨数" is notNull
        portUploadContract.confirmCoalAmount as "确认吨数" is between(BigDecimal.valueOf(1), BigDecimal.valueOf(100000000))
        portUploadContract.FileList.each is valid
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