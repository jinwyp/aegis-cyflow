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
  case class TraffickerAuditPage(applyCompanyName: String,                 //融资方公司名称
                                 businessCode: String,                     //业务编号
                                 coalType: String,                         //煤炭种类
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
  implicit val traffickerAuditPageFormat = jsonFormat19(TraffickerAuditPage)

  /** 贸易商财务审核,给出放款建议页面 **/
  case class TraffickerFinanceAuditPage(applyCompanyName: String,                 //融资方公司名称
                                        businessCode: String,                     //业务编号
                                        financeCreateTime: Timestamp,             //审批开始时间
                                        financingAmount: BigDecimal,              //拟融资金额
                                        financingDays: Int,                       //融资天数
                                        paidDeposit: BigDecimal,                  //已经缴纳保证金
                                        confirmFinancingAmount: BigDecimal,       //放款金额/实际融资金额
                                        alreadyPayPrinciple: BigDecimal,          //已回款本金
                                        waitPayPrinciple: BigDecimal,             //待回款本金
                                        status: String)                           //当前状态
  implicit val traffickerFinanceAuditPageFormat = jsonFormat10(TraffickerFinanceAuditPage)

  /** 资金方审核页面 **/
  case class FundProviderAuditPage(applyCompanyName: String,                 //融资方公司名称
                                   businessCode: String,                     //业务编号
                                   coalType: String,                         //煤炭种类
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
  implicit val fundProviderAuditPageFormat = jsonFormat19(FundProviderAuditPage)

  /** 资金方财务查看,付款页面 **/
  case class FundProviderFinancePayPage(applyCompanyName: String,            //融资方公司名称
                                        businessCode: String,                //业务编号
                                        financeCreateTime: Timestamp,        //审批开始时间
                                        financingAmount: BigDecimal,         //拟融资金额
                                        financingDays: Int,                  //融资天数
                                        paidDeposit: BigDecimal,             //已经缴纳保证金
                                        confirmFinancingAmount: BigDecimal,  //放款金额/实际融资金额
                                        alreadyPayPrinciple: BigDecimal,     //已回款本金
                                        waitPayPrinciple: BigDecimal,        //待回款本金
                                        status: String)                      //当前状态
  implicit val fundProviderFinancePayPageFormat = jsonFormat10(FundProviderFinancePayPage)

  /** 融资方列表 **/
  case class CustomerList(businessCode: String,                     //业务编号
                          downstreamCompanyName: String,            //下游签约公司-公司名称
                          financeCreateTime: Timestamp,             //审批开始时间
                          stockPort: String,                        //库存港口
                          coalAmount: BigDecimal,                   //总质押吨数
                          waitRedeemAmount: BigDecimal,             //待赎回数量
                          confirmFinancingAmount: BigDecimal,       //融资金额
                          paidDeposit: BigDecimal,                  //已经缴纳保证金
                          alreadyPayPrinciple: BigDecimal,          //已回款本金
                          waitPayPrinciple: BigDecimal,             //待回款本金
                          capitalCost: BigDecimal,                  //资金成本
                          financingDays: Int,                       //融资天数
                          coalType: String,                         //煤炭种类
                          coalIndex_NCV: Int,
                          coalIndex_RS: BigDecimal,
                          coalIndex_ADV: BigDecimal,                //煤炭 热值,硫分,空干基挥发分
                          status: String)                           //当前状态
  implicit val customerListFormat = jsonFormat17(CustomerList)

  /** 港口列表 **/
  case class PortList(businessCode: String,                     //业务编号
                      currentOwner: String,                     //当前货主
                      coalAmount: BigDecimal,                   //总质押吨数(原有总库存)
                      actualAmount: BigDecimal,                 //实际库存,  抵押前，数值等于原有总库存，抵押后，数值等于 待赎回库存
                      status: String)                           //当前状态
  implicit val portListFormat = jsonFormat5(PortList)

  /** 监管列表 **/
  case class SupervisorList(businessCode: String,                     //业务编号
                            currentOwner: String,                     //当前货主
                            coalAmount: BigDecimal,                   //总质押吨数(原有总库存)
                            actualAmount: BigDecimal,                 //实际库存,  抵押前，数值等于原有总库存，抵押后，数值等于 待赎回库存
                            status: String)                           //当前状态
  implicit val supervisorListFormat = jsonFormat5(SupervisorList)

  /** 贸易商列表 **/
  case class TraffickerList(applyCompanyName: String,                 //融资方公司名称
                            downstreamCompanyName: String,            //下游签约公司-公司名称
                            businessCode: String,                     //业务编号
                            financeStartTime: Timestamp,              //审批开始时间
                            stockPort: String,                        //库存港口
                            coalAmount: BigDecimal,                   //总质押吨数
                            waitRedeemAmount: BigDecimal,             //待赎回数量
                            coalType: String,                         //煤炭种类
                            coalIndex_NCV: Int,
                            coalIndex_RS: BigDecimal,
                            coalIndex_ADV: BigDecimal,                //煤炭 热值,硫分,空干基挥发分
                            contractNo: String,                       //合同编号 上游合同编号-下游合同编号
                            confirmFinancingAmount: BigDecimal,       //放款总金额
                            interestRate: BigDecimal,                 //融资方利率
                            interestIncome: BigDecimal,               //融资方利息收入
                            paidDeposit: BigDecimal,                  //已经缴纳保证金
                            alreadyPayPrinciple: BigDecimal,          //已回款本金
                            loanTime: Timestamp,                      //放款时间
                            fundProviderCompanyName: String,          //资金方公司名称
                            fundProviderInterestRate: BigDecimal,     //资金方利率
                            fundProviderInterestIncome: BigDecimal,   //资金方利息
                            status: String)                           //当前状态
  implicit val traffickerListFormat = jsonFormat22(TraffickerList)

  /** 贸易商财务列表 **/
  case class TraffickerFinanceList(applyCompanyName: String,                 //融资方公司名称
                                   confirmFinancingAmount: BigDecimal,       //放款总金额
                                   alreadyPayPrinciple: BigDecimal,          //已回款本金
                                   waitPayPrinciple: BigDecimal,             //待回款本金
                                   status: String)                           //当前状态
  implicit val traffickerFinanceListFormat = jsonFormat5(TraffickerFinanceList)

}
