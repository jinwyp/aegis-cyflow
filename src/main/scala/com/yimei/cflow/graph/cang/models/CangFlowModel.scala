package com.yimei.cflow.graph.cang.models

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol
import BaseFormatter._

object CangFlowModel extends DefaultJsonProtocol {

  case class FileObj(name: String, url: String, createTime: Option[Timestamp])
  implicit val fileObjFormat = jsonFormat3(FileObj)

  case class FileObjList(fileList: List[FileObj])
  implicit val fileObjListFormat = jsonFormat1(FileObjList)

  /** 进入仓押系统,初始化 **/
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
  case class TraffickerAssignUsers(portSalesmanUserId: String,                //港口业务人员 用户id
                                   portCompanyId: String,                     //港口公司id
                                   supervisorSalesmanUserId: String,          //监管业务人员 用户id
                                   supervisorCompanyId: String,               //监管公司id
                                   fundProviderSalesmanUserId: String,        //资金方业务人员 用户id
                                   fundProviderFinanceUserId: String,         //资金方财务 用户id
                                   fundProviderCompanyId: String)             //资金方公司id
  implicit val traffickerAssignUsersFormat = jsonFormat7(TraffickerAssignUsers)

  /** 融资方上传 合同, 财务, 业务 文件 **/
  case class CustomerUploadFile(contractFileList: FileObjList,
                                financeFileList: FileObjList,
                                businessFileList: FileObjList)
  implicit val customerUploadFileFormat = jsonFormat3(CustomerUploadFile)






}
