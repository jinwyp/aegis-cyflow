package com.yimei.cflow.graph.cang.models

import java.sql.Timestamp

import com.yimei.cflow.api.models.user.{UserProtocol, State => UserState}
import com.yimei.cflow.graph.cang.config.Config
import spray.json.DefaultJsonProtocol

object CangFlowModel extends DefaultJsonProtocol with UserProtocol with Config {

  case class FileObj(id: String, originName: String, fileType:Int, busiType:String = default, role:Option[String])
  implicit val fileObjFormat = jsonFormat5(FileObj)

  case class StartFlowResult(success: Boolean)
  implicit val startFlowResultFormat = jsonFormat1(StartFlowResult)

  /** 进入仓押系统,初始化, 开始流程 **/
  /** 基本信息 **/
  case class StartFlowBasicInfo(applyUserId: String,               //申请人-融资方 信息
                                applyUserName: Option[String],
                                applyUserPhone: String,
                                applyCompanyId: String,
                                applyCompanyName: String,
                                businessCode: String,              //业务编号
                                financeCreateTime: Timestamp,      //审批开始时间
                                financeEndTime: Timestamp,         //审批完成时间
                                downstreamCompanyName: String,     //下游签约公司-公司名称
                                financingAmount: BigDecimal,       //拟融资金额 - 融资方想要融资金额
                                financingDays: Int,                //融资天数
                                interestRate: BigDecimal,          //利率
                                coalType: String,                  //煤炭种类,品种
                                coalIndex_NCV: Int,
                                coalIndex_RS: BigDecimal,
                                coalIndex_ADV: BigDecimal,         //煤炭 热值,硫分,空干基挥发分
                                stockPort: String,                 //库存港口
                                coalAmount: BigDecimal,            //总质押吨数
                                upstreamContractNo: String,        //上游合同编号
                                downstreamContractNo: String)      //下游合同编号
  implicit val startFlowBasicInfoFormat = jsonFormat20(StartFlowBasicInfo)

  /** 尽调报告信息 **/
  case class StartFlowInvestigationInfo(applyCompanyName: String,                         //申请公司/融资方
                                        ourContractCompany: String,                       //我方签约公司
                                        upstreamContractCompany:String,                   //上游签约单位
                                        downstreamContractCompany: String,                //下游签约单位
                                        terminalServer: Option[String],                   //终端用户
                                        transportParty: Option[String],                   //运输方
                                        transitPort: Option[String],                      //中转港口
                                        qualityInspectionUnit: Option[String],            //质量检验单位
                                        quantityInspectionUnit: Option[String],           //数量检验单位
                                        financingAmount: BigDecimal,                      //融资金额
                                        financingPeriod: Int,                             //融资期限
                                        interestRate: BigDecimal,                         //利率
                                        businessStartTime: Option[Timestamp],             //业务开始时间
                                        historicalCooperationDetail: Option[String],      //历史合作情况
                                        mainBusinessInfo: Option[String],                 //业务主要信息
                                        businessTransferInfo: Option[String],             //业务流转信息
                                        businessRiskPoint: Option[String],                //业务风险点
                                        performanceCreditAbilityEval: Option[String],     //履约信用及能力评估
                                        finalConclusion: Option[String],                  //综合意见/最终结论
                                        supplyMaterialIntroduce: Option[String],          //补充材料说明
                                        approveState: String)                             //审核状态
  implicit val startFlowInvestigationInfoFormat = jsonFormat21(StartFlowInvestigationInfo)

  /** 监管报告信息 **/
  case class StartFlowSupervisorInfo(storageLocation: Option[String],             //煤炭仓储地
                                     storageProperty: Option[String],             //仓储性质
                                     storageAddress: Option[String],              //仓储地地址
                                     historicalCooperationDetail: Option[String], //历史合作情况
                                     operatingStorageDetail: Option[String],      //经营及堆存情况
                                     portStandardDegree: Option[String],          //保管及进出口流程规范程度
                                     supervisionCooperateDetail: Option[String],  //监管配合情况
                                     supervisionScheme: Option[String],           //监管方案
                                     finalConclusion: Option[String],             //最终结论/综合意见
                                     supplyMaterialIntroduce: Option[String],     //补充材料说明
                                     approveState: String)                        //审核状态
  implicit val startFlowSupervisorInfoFormat = jsonFormat11(StartFlowSupervisorInfo)

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
                                   fundProviderCompanyId: String,           //资金方公司id
                                   fundProviderUserId:String,
                                   fundProviderAccountantUserId:String)
  implicit val traffickerAssignUsersFormat = jsonFormat9(TraffickerAssignUsers)

  /** 监管方上传合同 **/
  /** 融资方上传 合同, 财务, 业务 文件 **/
  case class UploadContract(taskId: String,
                            flowId: String,
                            fileList: List[String])
  implicit val customerUploadContractFormat = jsonFormat3(UploadContract)

//  /** 监管方上传合同 **/
//  case class SupervisorUploadContract(taskId: String,
//                                      FileList: List[FileObj])
//  implicit val supervisorUploadContractFormat = jsonFormat2(SupervisorUploadContract)

  /** 港口上传合同, 填写确认吨数 **/
  case class HarborUploadContract(taskId: String,
                                  flowId: String,
                                  harborConfirmAmount: BigDecimal,
                                  fileList: List[String])
  implicit val portUploadContractFormat = jsonFormat4(HarborUploadContract)

  /** 贸易商审核 **/
  case class TraderAudit(taskId: String,
                         flowId: String,
                         approvedStatus: Int,
                         fundProviderInterestRate: BigDecimal)
  implicit val traffickerAuditFormat = jsonFormat4(TraderAudit)

  /** 贸易商财务给出 放款金额 **/
  case class TraderRecommendAmount(taskId: String,
                                   flowId: String,
                                   loanValue: BigDecimal)
  implicit val traffickerFinanceAuditFormat = jsonFormat3(TraderRecommendAmount)

  /** 资金方审核 **/
  case class FundProviderAudit(flowId:String,
                               taskId: String,
                               approvedStatus: Int)
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
                              repaymentValue: BigDecimal)
  implicit val customerPaymentToTraffickerFormat = jsonFormat3(FinancerToTrader)

  /** 贸易商通知港口放货 **/
  case class TraffickerNoticePortReleaseGoods(taskId: String,
                                              flowId: String,
                                              redemptionAmount: BigDecimal,
                                              goodsReceiveCompanyName: String,
                                              fileList: List[String])
  implicit val traffickerNoticePortReleaseGoodsFormat = jsonFormat5(TraffickerNoticePortReleaseGoods)

  /** 港口放货 **/
  case class PortReleaseGoods(taskId: String,
                              flowId: String,
                              status: Int)
  implicit val portReleaseGoodsFormat = jsonFormat3(PortReleaseGoods)

  /** 贸易商审核是否已经回款完成 **/
  case class TraffickerAuditIfCompletePayment(taskId: String,
                                              flowId: String,
                                              status: Int)
  implicit val traffickerAuditIfCompletePaymentFormat = jsonFormat3(TraffickerAuditIfCompletePayment)

  /** 贸易商同意付款给资金方 **/
  case class TraffickerConfirmPayToFundProvider(taskId: String,
                                                flowId: String,
                                                status: Int)
  implicit val traffickerConfirmPayToFundProviderFormat = jsonFormat3(TraffickerConfirmPayToFundProvider)

  /** 贸易商财务放款给资金方,流程结束 **/
  case class TraffickerFinancePayToFundProvider(taskId: String,
                                                flowId: String,
                                                status: Int)
  implicit val traffickerFinancePayToFundProviderFormat = jsonFormat3(TraffickerFinancePayToFundProvider)


  //####################
  /**
    * 审批带来的信息
    */
  case class SPData(financeCreateTime: Timestamp,      //审批开始时间
                    financeEndTime: Timestamp,         //审批结束时间(仓压开始时间）
                    orderType:String,                  //审批类型
                    businessCode:String,                    //审批号
                    downstreamCompanyName:String,      //下游采购方
                    stockPort:String,                     //库存港口
                    coalAmount:BigDecimal,             //质押总数量（吨）
                    financingAmount : BigDecimal,       //拟融资金额（万元) 拟融资金额 - 融资方想要融资金额,不是实际融资金额
                    financingDays : Int,                //融资期限（天）
                    interestRate : BigDecimal,          // 利率
                    coalType : String ,                 //煤种
                    coalIndex_NCV: Int,
                    coalIndex_RS: BigDecimal,
                    coalIndex_ADV: BigDecimal,         //煤炭 热值,硫分,空干基挥发分
                    investigationInfo:StartFlowInvestigationInfo,    //尽调报告信息
                    supervisorInfo:StartFlowSupervisorInfo          //监管报告信息
                   )
  implicit val spDataFormat = jsonFormat16(SPData)

  /**
    *用户信息
    */
  case class UserInfo(userId:String,
                      userName:String,
                      phone:Option[String],
                      email:Option[String],
                      name:String,
                      companyName:String,
                      companyId:String
                     )
  implicit val userInfoFormat = jsonFormat7(UserInfo)

  /**
    * 仓压用户信息
    */
  case class CYPartyMember(   harbor: Option[UserInfo],                       //港口
                              supervisor:Option[UserInfo],             //监管
                              fundProvider: Option[UserInfo],           //资金方业务
                              fundProviderAccountant:Option[UserInfo],      //资金方财务
                              trader:Option[UserInfo],                  //贸易方业务
                              traderAccountant:Option[UserInfo],        //贸易方财务
                              financer:UserInfo               //融资方
                            )
  implicit val cyPartyMemberFormat = jsonFormat7(CYPartyMember)


  /**
    *保证金记录
    */
  case class DepositRecord(expectedAmount:BigDecimal,                                 //保证金金额\
                    actuallyAmount: BigDecimal,
                     memo:String,                                   //流水号
                     status:String,                                     //保证金状态
                     ts_c: Timestamp                                    //创建时间
                    )
  implicit val depositFormat = jsonFormat5(DepositRecord)

  /**
    * 还款交易记录
    */
  case class Repayment(repaymentValue:BigDecimal,                      //还款金额(万元？）todo 确认
                       interestBearingCapital:BigDecimal,               //本次还款计息本金
                       nextInterestBearingCapital:BigDecimal,           //下次计息本金
                       days:Long,                                        //计息天数
                       interest:BigDecimal,                             //利息
                       transactionDate:Timestamp                        //交易时间
                      )
  implicit val repaymentFormat = jsonFormat6(Repayment)

  /**
    *港口记录
    */
  case class Delivery(redemptionAmount:BigDecimal,                        //放货吨数
                      deliveryTime:Timestamp ,                          //放货时间
                      fileList:List[FileObj],                           //放货文件
                      sender:Option[String],                                    //放货方
                      goodsReceiveCompanyName:String                    //收货方
                     )
  implicit val deliveryFormat = jsonFormat5(Delivery)


  /**
    *流程数据
    */
  case class FlowData(
                    currentTask:UserState,
                    cargoOwner:String,                          //货权（贸易商审核通过前为融资方，然后为贸易方）
                    status:String,                                        //当前所在vertices
                    loanValue:Option[BigDecimal],                         //实际放款金额
                    depositValue:Option[BigDecimal],                      //保证金金额
                    loanFundProviderInterestRate:Option[BigDecimal],      //资金方借款的利率
                    harborConfirmAmount:Option[BigDecimal],               //港口确认吨数
                    redemptionAmount:Option[BigDecimal],                  //已赎回吨数
                    returnValue:Option[BigDecimal],                       //已归还金额
                    redemptionAmountLeft:Option[BigDecimal],              //待赎回吨数
                    repaymentValue:Option[BigDecimal],                    //待还款
                    depositList:Option[List[DepositRecord]],                    //保证金记录
                    repaymentList:Option[List[Repayment]],                //还款交易记录
                    deliveryList:Option[List[Delivery]],                  //放货记录
                    fileList:List[FileObj],                        //该流程对应全部文件
                    loanActualArrivalDate:Option[Timestamp],        //实际放款时间
                    LastRepaymentDate:Option[Timestamp],            //实际结息时间
                    totalInterest:Option[BigDecimal]                //总利息
                   )
  implicit val flowDataFormat = jsonFormat18(FlowData)


  /**
    * 仓压数据
    */
  case class CYData( spData:SPData,                           //审核带来的数据
                     cyPartyMember:CYPartyMember,             //仓压用户信息
                     flowData:FlowData,                       //流程数据
                     flowId:String,                            //流程Id
                     currentSessionUserTaskId:String,
                     currentSessionUserTaskTaskName:String
                   )

  implicit val cyDataFormat = jsonFormat6(CYData)



  case class PayRequest( srcUserType:String,
                         srcCompanyId:String,
                         targetUserType:String,
                         targetCompanyId:String,
                         amount:BigDecimal
                       )

  implicit val payRequestFormat = jsonFormat5(PayRequest)

  case class PayResponse(transactionId:Option[String],
                         status:Int,               // 0:失败，1：处理中， 2：成功
                         message:Option[String]
                        )
  implicit val payResponseFormat = jsonFormat3(PayResponse)

  case class PayQueryResponse(status:Int, message:Option[String]) // 0:失败，1：处理中， 2：成功
  implicit val payQueryResponseFormat = jsonFormat2(PayQueryResponse)

}
