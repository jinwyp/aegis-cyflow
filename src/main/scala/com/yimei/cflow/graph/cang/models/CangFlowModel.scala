package com.yimei.cflow.graph.cang.models

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol
import BaseFormatter._;

object CangFlowModel extends DefaultJsonProtocol {

  case class FileObj(name: String, url: String, createTime: Option[Timestamp])
  implicit val fileObjFormat = jsonFormat3(FileObj)

  /** 进入仓押系统,初始化, 开始流程 **/
  /** 基本信息 **/
  case class StartFlowBasicInfo(applyUserId: Long,                 //申请人-融资方 信息
                                applyUserName: String,
                                applyUserPhone: String,
                                applyCompanyId: Long,
                                applyCompanyName: String,
                                businessCode: String,              //业务编号
                                financeCreateTime: Timestamp,      //审批开始时间
                                financeEndTime: Timestamp,         //审批结束时间
                                downstreamCompanyName: String,     //下游签约公司-公司名称
                                financingAmount: BigDecimal,       //拟融资金额
                                financingDays: Int,                //融资天数
                                interestRate: BigDecimal,          //利率
                                coalType: String,                  //煤炭种类,品种
                                coalIndex_NCV: Int,
                                coalIndex_RS: BigDecimal,
                                coalIndex_ADV: BigDecimal,         //煤炭 热值,硫分,空干基挥发分
                                stockPort: String,                 //库存港口
                                coalAmount: BigDecimal,            //总质押吨数
                                upstreamContractNo: String,        //上游合同编号
                                downstreamContractNo: String,      //下游合同编号
                                auditFileList: List[FileObj])      //审批文件列表
  implicit val startFlowBasicInfoFormat = jsonFormat21(StartFlowBasicInfo)

  /** 尽调报告信息 **/
  case class StartFlowInvestigationInfo(financeId: Long,                      //金融单id
                                        applyCompanyName: String,             //申请公司/融资方
                                        ourContractCompany: String,           //我方签约公司
                                        upstreamContractCompany:String,       //上游签约单位
                                        downstreamContractCompany: String,    //下游签约单位
                                        terminalServer:String,                //终端用户
                                        transportParty: String,               //运输方
                                        transitPort: String,                  //中转港口
                                        qualityInspectionUnit: String,        //质量检验单位
                                        quantityInspectionUnit: String,       //数量检验单位
                                        financingAmount: BigDecimal,          //融资金额
                                        financingPeriod: Int,                 //融资期限
                                        interestRate: BigDecimal,             //利率
                                        businessStartTime: Timestamp,         //业务开始时间
                                        historicalCooperationDetail: String,  //历史合作情况
                                        mainBusinessInfo: String,             //业务主要信息
                                        businessTransferInfo: String,         //业务流转信息
                                        businessRiskPoint: String,            //业务风险点
                                        performanceCreditAbilityEval: String, //履约信用及能力评估
                                        finalConclusion: String)              //综合意见/最终结论
  implicit val startFlowInvestigationInfoFormat = jsonFormat20(StartFlowInvestigationInfo)

  /** 监管报告信息 **/
  case class StartFlowSupervisorInfo(financeId: Long,                     //金融单id
                                     storageLocation: String,             //煤炭仓储地
                                     storageProperty: String,             //仓储性质
                                     storageAddress: String,              //仓储地地址
                                     historicalCooperationDetail: String, //历史合作情况
                                     operatingStorageDetail: String,      //经营及堆存情况
                                     portStandardDegree: String,          //保管及进出口流程规范程度
                                     supervisionCooperateDetail: String,  //监管配合情况
                                     supervisionScheme: String,           //监管方案
                                     finalConclusion: String)                                  //最终结论/综合意见
  implicit val startFlowSupervisorInfoFormat = jsonFormat10(StartFlowSupervisorInfo)

  case class StartFlow(basicInfo: StartFlowBasicInfo,
                       investigationInfo: StartFlowInvestigationInfo,
                       supervisorInfo: StartFlowSupervisorInfo)
  implicit val startFlowFormat = jsonFormat3(StartFlow)

  /**
    * 贸易商选择
    * 港口 业务人员,
    * 监管 业务人员,
    * 资金方 业务人员, 财务
    */
  case class TraffickerAssignUsers(taskId: String,
                                   portUserId: String,                       //港口业务人员 用户id
                                   portCompanyId: String,                    //港口公司id
                                   supervisorUserId: String,                 //监管业务人员 用户id
                                   supervisorCompanyId: String,              //监管公司id
                                   fundProviderCompanyId: String)            //资金方公司id
  implicit val traffickerAssignUsersFormat = jsonFormat6(TraffickerAssignUsers)

  /** 融资方上传 合同, 财务, 业务 文件 **/
  case class CustomerUploadContract(taskId: String,
                                    contractFileList: List[FileObj],
                                    financeFileList: List[FileObj],
                                    businessFileList: List[FileObj])
  implicit val customerUploadContractFormat = jsonFormat4(CustomerUploadContract)

  /** 监管方上传合同 **/
  case class SupervisorUploadContract(taskId: String,
                                      contractFileList: List[FileObj])
  implicit val supervisorUploadContractFormat = jsonFormat2(SupervisorUploadContract)

  /** 港口上传合同, 填写确认吨数 **/
  case class PortUploadContract(taskId: String,
                                confirmCoalAmount: BigDecimal,
                                contractFileList: List[FileObj])
  implicit val portUploadContractFormat = jsonFormat3(PortUploadContract)

  /** 贸易商审核 **/
  case class TraffickerAudit(taskId: String,
                             statusId: Int,
                             fundProviderInterestRate: BigDecimal)
  implicit val traffickerAuditFormat = jsonFormat3(TraffickerAudit)

  /** 贸易商财务给出放款建议, 放款金额 **/
  case class TraffickerFinanceAudit(taskId: String,
                                    confirmFinancingAmount: BigDecimal,
                                    financingAdvice: String)
  implicit val traffickerFinanceAuditFormat = jsonFormat3(TraffickerFinanceAudit)

  /** 资金方审核 **/
  case class FundProviderAudit(taskId: String, statusId: Int)
  implicit val fundProviderAuditFormat = jsonFormat2(FundProviderAudit)

  /** 资金方财务付款 **/
  case class FundProviderFinanceLoad(taskId: String, statusId: Int)
  implicit val fundProviderFinanceLoadFormat = jsonFormat2(FundProviderFinanceLoad)

  /** 融资方付款给贸易商 **/
  case class CustomerPaymentToTrafficker(taskId: String, statusId: Int, paymentPrinciple: BigDecimal, createTime: Option[Timestamp])
  implicit val customerPaymentToTraffickerFormat = jsonFormat4(CustomerPaymentToTrafficker)

  /** 贸易商通知港口放货 **/
  case class TraffickerNoticePortReleaseGoods(taskId: String,
                                              releaseAmount: BigDecimal,
                                              goodsReceiveCompanyName: String,
                                              goodsFileList: List[FileObj],
                                              createTime: Option[Timestamp])
  implicit val traffickerNoticePortReleaseGoodsFormat = jsonFormat5(TraffickerNoticePortReleaseGoods)

  /** 港口放货 **/
  case class PortReleaseGoods(taskId: String, statusId: Int, createTime: Option[Timestamp])
  implicit val portReleaseGoodsFormat = jsonFormat3(PortReleaseGoods)

  /** 贸易商审核是否已经回款完成 **/
  case class TraffickerAuditIfCompletePayment(taskId: String, statusId: Int, createTime: Option[Timestamp])
  implicit val traffickerAuditIfCompletePaymentFormat = jsonFormat3(TraffickerAuditIfCompletePayment)

  /** 贸易商同意付款给资金方 **/
  case class TraffickerConfirmPayToFundProvider(taskId: String, statusId: Int)
  implicit val traffickerConfirmPayToFundProviderFormat = jsonFormat2(TraffickerConfirmPayToFundProvider)

  /** 贸易商财务放款给资金方,流程结束 **/
  case class TraffickerFinancePayToFundProvider(taskId: String, statusId: Int)
  implicit val traffickerFinancePayToFundProviderFormat = jsonFormat2(TraffickerFinancePayToFundProvider)



}
