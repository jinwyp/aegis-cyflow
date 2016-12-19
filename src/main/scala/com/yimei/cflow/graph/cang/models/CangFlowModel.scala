package com.yimei.cflow.graph.cang.models

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol
import BaseFormatter._;

object CangFlowModel extends DefaultJsonProtocol {


  case class FileObj(name: String, url: String, createTime: Option[Timestamp])
  implicit val fileObjFormat = jsonFormat3(FileObj)

  case class FileObjList(fileList: List[FileObj])
  implicit val fileObjListFormat = jsonFormat1(FileObjList)

  /** 进入仓押系统,初始化, 开始流程 **/
  case class StartFlow(applyUserId: BigInt, applyUserName: String, applyUserPhone: String, applyCompanyId: BigInt,
                       applyCompanyName: String,         //申请人-融资方 信息
                       financeCreateTime: Timestamp,     //审批开始时间
                       financeEndTime: Timestamp,        //审批结束时间
                       downstreamCompanyName: String,    //下游签约公司-公司名称
                       financingAmount: BigDecimal,      //拟融资金额
                       financingDays: Int,               //融资天数
                       interestRate: BigDecimal,         //利率
                       coalType: String,                 //煤炭种类,品种
                       coalIndex_NCV: Int,
                       coalIndex_RS: BigDecimal,
                       coalIndex_ADV: BigDecimal,        //煤炭 热值,硫分,空干基挥发分
                       stockPort: String,                //库存港口
                       coalAmount: BigDecimal,           //总质押吨数
                       auditFileList: FileObjList)       //审批文件列表
  implicit val startFlowFormat = jsonFormat18(StartFlow)

  /**
    * 贸易商选择
    * 港口 业务人员,
    * 监管 业务人员,
    * 资金方 业务人员, 财务
    */
  case class TraffickerAssignUsers(portUserId: String,                       //港口业务人员 用户id
                                   portCompanyId: String,                    //港口公司id
                                   supervisorUserId: String,                 //监管业务人员 用户id
                                   supervisorCompanyId: String,              //监管公司id
                                   fundProviderUserId: String,               //资金方业务人员 用户id
                                   fundProviderFinanceUserId: String,        //资金方财务 用户id
                                   fundProviderCompanyId: String)            //资金方公司id
  implicit val traffickerAssignUsersFormat = jsonFormat7(TraffickerAssignUsers)

  /** 融资方上传 合同, 财务, 业务 文件 **/
  case class CustomerUploadContract(contractFileList: FileObjList,
                                    financeFileList: FileObjList,
                                    businessFileList: FileObjList)
  implicit val customerUploadContractFormat = jsonFormat3(CustomerUploadContract)

  /** 监管方上传合同 **/
  case class SupervisorUploadContract(contractFileList: FileObjList)
  implicit val supervisorUploadContractFormat = jsonFormat1(SupervisorUploadContract)

  /** 港口上传合同, 填写确认吨数 **/
  case class PortUploadContract(confirmCoalAmount: BigDecimal, contractFileList: FileObjList)
  implicit val portUploadContractFormat = jsonFormat2(PortUploadContract)

  /** 贸易商审核 **/
  case class TraffickerAudit(statusId: Int, fundProviderInterestRate: BigDecimal)
  implicit val traffickerAuditFormat = jsonFormat2(TraffickerAudit)

  /** 贸易商财务给出放款建议, 放款金额 **/
  case class TraffickerFinanceAudit(confirmFinancingAmount: BigDecimal, financingAdvice: String)
  implicit val traffickerFinanceAuditFormat = jsonFormat2(TraffickerFinanceAudit)

  /** 资金方审核 **/
  case class FundProviderAudit(statusId: Int)
  implicit val fundProviderAuditFormat = jsonFormat1(FundProviderAudit)

  /** 资金方财务付款 **/
  case class FundProviderFinanceLoad(statusId: Int)
  implicit val fundProviderFinanceLoadFormat = jsonFormat1(FundProviderFinanceLoad)

  /** 融资方付款给贸易商 **/
  case class CustomerPaymentToTrafficker(paymentPrinciple: BigDecimal, createTime: Option[Timestamp])
  implicit val customerPaymentToTraffickerFormat = jsonFormat2(CustomerPaymentToTrafficker)

  /** 贸易商通知港口放货 **/
  case class TraffickerNoticePortReleaseGoods(releastAmount: BigDecimal, goodsReceiveCompanyName: String, goodsFileList: FileObjList, createTime: Option[Timestamp])
  implicit val traffickerNoticePortReleaseGoodsFormat = jsonFormat4(TraffickerNoticePortReleaseGoods)

  /** 港口放货 **/
  case class PortReleaseGoods(statusId: Int, createTime: Option[Timestamp])
  implicit val portReleaseGoodsFormat = jsonFormat2(PortReleaseGoods)

  /** 贸易商同意付款给资金方 **/
  case class TraffickerConfirmPayToFundProvider(statusId: Int)
  implicit val traffickerConfirmPayToFundProviderFormat = jsonFormat1(TraffickerConfirmPayToFundProvider)

  /** 贸易商财务放款给资金方,流程结束 **/
  case class TraffickerFinancePayToFundProvider(statusId: Int)
  implicit val traffickerFinancePayToFundProviderFormat = jsonFormat1(TraffickerFinancePayToFundProvider)



}
