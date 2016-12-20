package com.yimei.cflow.graph.cang.models.validator

import com.wix.accord.Validator
import com.wix.accord.dsl._
import com.yimei.cflow.graph.cang.models.CangFlowModel.{CustomerUploadContract, FileObj, PortUploadContract, StartFlow, SupervisorUploadContract, TraffickerAssignUsers}

object CangFlowValidator {
  /** 文件 **/
  implicit val fileObjValidator: Validator[FileObj] =
    validator[FileObj] {
      fileObj =>
        fileObj.name as "文件名称" is notEmpty
        fileObj.url as "文件路径" is notEmpty
        fileObj.name.length is between(1, 100)
        fileObj.url.length is between(1, 200)
    }


  /** 开始流程 **/
  implicit val startFlowValidator: Validator[StartFlow] =
    validator[StartFlow] {
      startFlow =>
        startFlow.applyCompanyId as "融资方公司id" must > (0L)
        startFlow.applyCompanyName as "融资方公司名称" is notEmpty
        startFlow.applyUserId as "融资方用户id" must > (0L)
        startFlow.applyUserName as "融资方用户姓名" is notEmpty
        startFlow.applyUserPhone as "融资方用户手机号" is notEmpty
        startFlow.auditFileList.each is valid
        startFlow.businessCode as "业务编号" is notEmpty
        startFlow.coalAmount as "总质押吨数" must > (BigDecimal.valueOf(0))
        startFlow.coalIndex_ADV as "空干基挥发分" must(between(BigDecimal.valueOf(0.01), BigDecimal.valueOf(50)))
        startFlow.coalIndex_NCV as "热值" must(between(1, 7500))
        startFlow.coalIndex_RS as "硫分" must(between(BigDecimal.valueOf(0.01), BigDecimal.valueOf(10)))
        startFlow.coalType as "煤炭种类" is notEmpty
        startFlow.coalType.length as "煤炭种类字段长度" must(between(1, 100))
        startFlow.downstreamCompanyName as "下游签约单位公司名称" is notEmpty
        startFlow.downstreamCompanyName.length as "下游签约单位公司名称字段长度" must(between(1, 100))
        startFlow.financeCreateTime as "审批开始时间" is notNull
        startFlow.financeEndTime as "审批完成时间" is notNull
        startFlow.financingAmount as "拟融资金额" is notNull
        startFlow.financingDays as "拟融资天数" must > (0)
        startFlow.interestRate as "利率" is notNull
        startFlow.stockPort as "库存港口" is notEmpty
        startFlow.stockPort.length as "库存港口字段长度" must(between(1, 200))
    }


  /** 贸易商选择 港口, 监管, 资金方 业务人员, 财务 **/
  implicit val traffickerAssignUsersValidator: Validator[TraffickerAssignUsers] =
    validator[TraffickerAssignUsers] {
      traffickerAssignUsers =>
        traffickerAssignUsers.taskId as "任务id" is notEmpty
        traffickerAssignUsers.portCompanyId as "港口公司id" is notEmpty
        traffickerAssignUsers.portUserId as "港口用户id" is notEmpty
        traffickerAssignUsers.supervisorCompanyId as "监管公司id" is notEmpty
        traffickerAssignUsers.supervisorUserId as "监管用户id" is notEmpty
        traffickerAssignUsers.fundProviderCompanyId as "资金方公司id" is notEmpty
        traffickerAssignUsers.fundProviderUserId as "资金方用户id" is notEmpty
        traffickerAssignUsers.fundProviderFinanceUserId as "资金方财务id" is notEmpty
    }

  /** 融资方上传 合同, 财务, 业务 文件 **/
  implicit val customerUploadContractValidator: Validator[CustomerUploadContract] =
    validator[CustomerUploadContract] {
      customerUploadContract =>
        customerUploadContract.taskId as "任务id" is notEmpty
        customerUploadContract.businessFileList.each is valid
        customerUploadContract.contractFileList.each is valid
        customerUploadContract.financeFileList.each is valid
    }

  /** 监管方上传合同 **/
  implicit val supervisorUploadContractValidator: Validator[SupervisorUploadContract] =
    validator[SupervisorUploadContract] {
      supervisorUploadContract =>
        supervisorUploadContract.taskId as "任务id" is notEmpty
        supervisorUploadContract.contractFileList.each is valid
    }

  /** 方扣上传合同 **/
  implicit val portUploadContractValidator: Validator[PortUploadContract] =
    validator[PortUploadContract] {
      portUploadContract =>
        portUploadContract.taskId as "任务id" is notEmpty
        portUploadContract.confirmCoalAmount as "确认吨数" is notNull
        portUploadContract.contractFileList.each is valid
    }

}