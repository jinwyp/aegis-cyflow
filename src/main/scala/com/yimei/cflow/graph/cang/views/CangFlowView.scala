package com.yimei.cflow.graph.cang.views

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol
import com.yimei.cflow.graph.cang.models.BaseFormatter._
import com.yimei.cflow.graph.cang.models.CangFlowModel.FileObjList

object CangFlowView extends DefaultJsonProtocol {


  /** 贸易商选择港口, 监管, 资金方页面 **/
  case class AssignUser(userId: String, userName: String, companyId: String, companyName: String)
  implicit val assignUserFormat = jsonFormat4(AssignUser)

  case class AssignUser2(userId1: String, userId2: String, companyId: String, companyName: String)
  implicit val assignUser2Format = jsonFormat4(AssignUser2)

  case class AssignUserPage(portList: List[AssignUser], supervisorList: List[AssignUser], fundProviderList: List[AssignUser2])
  implicit val assignUserPageFormat = jsonFormat3(AssignUserPage)

  /** 监管员上传合同页面 **/
  case class SupervisorUploadContractPage(businessCode: String,      //业务编号
                                          status: String,            //当前状态
                                          currentOwner: String,      //当前货主
                                          coalType: String,          //煤炭种类
                                          stockPort: String)         //库存港口
  implicit val supervisorUploadContractPageFormat = jsonFormat5(SupervisorUploadContractPage)

  /** 港口上传合同页面 **/
  case class PortUploadContractPage(businessCode: String,      //业务编号
                                    status: String,            //当前状态
                                    currentOwner: String,      //当前货主
                                    coalType: String,          //煤炭种类
                                    stockPort: String)         //库存港口
  implicit val portUploadContractPageFormat = jsonFormat5(PortUploadContractPage)

  /** 贸易商审核页面 **/
  case class TraffickerAuditPage(customerCompanyName: String, businessCode: String, coalType: String,
                                 financeCreateTime: Timestamp,             //审批开始时间
                                 financingAmount: BigDecimal,              //拟融资金额
                                 financingDays: Int,                       //融资天数
                                 confirmFinancingAmount: BigDecimal,        //实际放款金额
                                 paidDeposit: BigDecimal,                  //已经缴纳保证金
                                 coalAmount: BigDecimal,                   //总质押吨数
                                 alreadyRedeemAmount: BigDecimal,          //已赎回数量
                                 waitRedeemAmount: BigDecimal,             //待赎回数量
                                 confirmCoalAmount: BigDecimal,            //港口确认吨数
                                 stockPort: String,                        //库存港口
                                 customerContractFileList: FileObjList,    //融资方上传合同文件列表
                                 customerFinanceFileList: FileObjList,     //融资方上传财务文件列表
                                 customerBusinessFileList: FileObjList,    //融资方上传业务文件列表
                                 supervisorContractFileList: FileObjList,  //监管方上传合同文件列表
                                 portContractFileList: FileObjList,        //港口上传合同文件列表
                                 status: String)                           //当前状态

  /** 贸易商财务审核,给出放款建议页面 **/
  case class TraffickerFinanceAuditPage(customerCompanyName: String, businessCode: String,
                                        financeCreateTime: Timestamp,             //审批开始时间
                                        financingAmount: BigDecimal,              //拟融资金额
                                        financingDays: Int,                       //融资天数
                                        paidDeposit: BigDecimal,                  //已经缴纳保证金
                                        confirmFinancingAmount: BigDecimal,       //放款金额/实际融资金额
                                        alreadyPayPrinciple: BigDecimal,          //已回款本金
                                        waitPayPrinciple: BigDecimal,             //待回款本金
                                        status: String)                           //当前状态

  /** 资金方审核页面 **/
  case class FundProviderAuditPage(customerCompanyName: String, businessCode: String, coalType: String,
                                   financeCreateTime: Timestamp,             //审批开始时间
                                   financingAmount: BigDecimal,              //拟融资金额
                                   financingDays: Int,                       //融资天数
                                   confirmFinancingAmount: BigDecimal,       //实际放款金额
                                   paidDeposit: BigDecimal,                  //已经缴纳保证金
                                   coalAmount: BigDecimal,                   //总质押吨数
                                   alreadyRedeemAmount: BigDecimal,          //已赎回数量
                                   waitRedeemAmount: BigDecimal,             //待赎回数量
                                   confirmCoalAmount: BigDecimal,            //港口确认吨数
                                   stockPort: String,                        //库存港口
                                   customerContractFileList: FileObjList,    //融资方上传合同文件列表
                                   customerFinanceFileList: FileObjList,     //融资方上传财务文件列表
                                   customerBusinessFileList: FileObjList,    //融资方上传业务文件列表
                                   supervisorContractFileList: FileObjList,  //监管方上传合同文件列表
                                   portContractFileList: FileObjList,        //港口上传合同文件列表
                                   status: String)                           //当前状态

  /** 资金方财务查看,付款页面 **/
  case class FundProviderFinancePayPage(customerCompanyName: String, businessCode: String,
                                        financeCreateTime: Timestamp,        //审批开始时间
                                        financingAmount: BigDecimal,         //拟融资金额
                                        financingDays: Int,                  //融资天数
                                        paidDeposit: BigDecimal,             //已经缴纳保证金
                                        confirmFinancingAmount: BigDecimal,  //放款金额/实际融资金额
                                        alreadyPayPrinciple: BigDecimal,     //已回款本金
                                        waitPayPrinciple: BigDecimal,        //待回款本金
                                        status: String)                      //当前状态




}
