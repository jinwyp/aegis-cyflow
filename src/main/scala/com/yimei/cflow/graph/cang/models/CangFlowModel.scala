package com.yimei.cflow.graph.cang.models

import java.sql.Timestamp
import spray.json.DefaultJsonProtocol
import BaseFormatter._
import com.yimei.cflow.graph.cang.config.Config

object CangFlowModel extends DefaultJsonProtocol with Config {

  case class FileObj(name: String, originName: String, url: String, fileType:String = default )
  implicit val fileObjFormat = jsonFormat4(FileObj)

  /** 进入仓押系统,初始化, 开始流程 **/
  /** 基本信息 **/
  case class StartFlowBasicInfo(applyUserId: String,                 //申请人-融资方 信息
                                applyUserName: String,
                                applyUserPhone: String,
                                applyCompanyId: String,
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
  case class StartFlowInvestigationInfo(applyCompanyName: String,             //申请公司/融资方
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
  implicit val startFlowInvestigationInfoFormat = jsonFormat19(StartFlowInvestigationInfo)

  /** 监管报告信息 **/
  case class StartFlowSupervisorInfo(storageLocation: String,             //煤炭仓储地
                                     storageProperty: String,             //仓储性质
                                     storageAddress: String,              //仓储地地址
                                     historicalCooperationDetail: String, //历史合作情况
                                     operatingStorageDetail: String,      //经营及堆存情况
                                     portStandardDegree: String,          //保管及进出口流程规范程度
                                     supervisionCooperateDetail: String,  //监管配合情况
                                     supervisionScheme: String,           //监管方案
                                     finalConclusion: String)             //最终结论/综合意见
  implicit val startFlowSupervisorInfoFormat = jsonFormat9(StartFlowSupervisorInfo)

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
  case class TraffickerAssignUsers(flowId:String,                             //流程Id
                                   taskId: String,
                                   harborUserId: String,                       //港口业务人员 用户id
                                   harborCompanyId: String,                    //港口公司id
                                   supervisorUserId: String,                 //监管业务人员 用户id
                                   supervisorCompanyId: String,              //监管公司id
                                   fundProviderCompanyId: String)            //资金方公司id
  implicit val traffickerAssignUsersFormat = jsonFormat7(TraffickerAssignUsers)

  /** 监管方上传合同 **/
  /** 融资方上传 合同, 财务, 业务 文件 **/
  case class UploadContract(taskId: String,
                            flowId: String,
                            fileList: List[FileObj])
  implicit val customerUploadContractFormat = jsonFormat3(UploadContract)

//  /** 监管方上传合同 **/
//  case class SupervisorUploadContract(taskId: String,
//                                      FileList: List[FileObj])
//  implicit val supervisorUploadContractFormat = jsonFormat2(SupervisorUploadContract)

  /** 港口上传合同, 填写确认吨数 **/
  case class HarborUploadContract(taskId: String,
                                  flowId: String,
                                  confirmCoalAmount: BigDecimal,
                                  fileList: List[FileObj])
  implicit val portUploadContractFormat = jsonFormat4(HarborUploadContract)

  /** 贸易商审核 **/
  case class TraderAudit(taskId: String,
                         flowId: String,
                         status: Int,
                         fundProviderInterestRate: BigDecimal)
  implicit val traffickerAuditFormat = jsonFormat4(TraderAudit)

  /** 贸易商财务给出 放款金额 **/
  case class TraderRecommendAmount(taskId: String,
                                    flowId: String,
                                    recommendAmount: BigDecimal)
  implicit val traffickerFinanceAuditFormat = jsonFormat3(TraderRecommendAmount)

  /** 资金方审核 **/
  case class FundProviderAudit(flowId:String,
                               taskId: String,
                               status: Int)
  implicit val fundProviderAuditFormat = jsonFormat3(FundProviderAudit)

  /** 资金方财务付款 **/
  case class FundProviderAccountantAudit(taskId: String,
                                     flowId: String,
                                     status: Int
                                    )
  implicit val fundProviderFinanceLoadFormat = jsonFormat3(FundProviderAccountantAudit)

  /** 融资方付款给贸易商 **/
  case class FinancerToTrader(taskId: String,
                                         flowId: String,
                                         repaymentAmount: BigDecimal)
  implicit val customerPaymentToTraffickerFormat = jsonFormat3(FinancerToTrader)

  /** 贸易商通知港口放货 **/
  case class TraffickerNoticePortReleaseGoods(taskId: String,
                                              flowId: String,
                                              releaseAmount: BigDecimal,
                                              goodsReceiveCompanyName: String,
                                              goodsFileList: List[FileObj])
  implicit val traffickerNoticePortReleaseGoodsFormat = jsonFormat5(TraffickerNoticePortReleaseGoods)

  /** 港口放货 **/
  case class PortReleaseGoods(taskId: String,
                              flowId: String,
                              status: Int)
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
