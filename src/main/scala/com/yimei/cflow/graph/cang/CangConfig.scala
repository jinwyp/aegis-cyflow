package com.yimei.cflow.graph.cang

import com.yimei.cflow.api.models.flow.TaskInfo

/**
  * Created by hary on 16/12/15.
  */
object CangConfig {

  /**
    * 流程类型
    */
  val flow_cang = "cang"

  /**
    * 所有的数据点名称
    */
  val point_start = "startPoint"
  val point_traffickerAssignUsers = "traffickerAssignUsersPoint"
  val point_customerUploadContract = "customerUploadContractPoint"
  val point_supervisorUploadContract = "supervisorUploadContractPoint"
  val point_portUploadContract = "portUploadContractPoint"
  val point_traffickerAudit = "traffickerAuditPoint"
  val point_traffickerFinanceAudit = "traffickerFinanceAuditPoint"
  val point_fundProviderAudit = "fundProviderAuditPoint"
  val point_fundProviderFinanceLoad = "fundProviderFinanceLoadPoint"
  val point_serviceTransferAccountsToCustomer = "serviceTransferAccountsToCustomerPoint"
  val point_customerPayToTrafficker = "customerPayToTraffickerPoint"
  val point_traffickerNoticePortReleaseGoods = "traffickerNoticePortReleaseGoodsPoint"
  val point_portReleaseGoods = "portReleaseGoodsPoint"
  val point_traffickerAuditIfCompletePayment = "traffickerAuditIFCompletePaymentPoint"
  val point_traffickerConfirmPayToFundProvider = "traffickerConfirmPayToFundProviderPoint"
  val point_traffickerFinancePayToFundProvider = "traffickerFinancePayToFundProviderPoint"


  /**
    * 所有数据点的描述
    */
  val pointDescription = Map[String, String](
    point_start -> "进入仓押系统时,带入的数据",
    point_traffickerAssignUsers -> "贸易商分配 港口, 监管, 资金方时提交的数据",
    point_customerUploadContract -> "融资方上传合同,文件时提交的数据",
    point_supervisorUploadContract -> "监管员上传合同时提交的数据",
    point_portUploadContract -> "港口工作人员上传合同时提交的数据",
    point_traffickerAudit -> "贸易商审核时提交的数据",
    point_traffickerFinanceAudit -> "贸易商财务审核时提交的数据",
    point_fundProviderAudit -> "资金方工作人员审核时提交的数据",
    point_fundProviderFinanceLoad -> "资金方财务放款给贸易商时提交的数据",
    point_serviceTransferAccountsToCustomer -> "系统检测当资金方财务放的款到贸易商账上时, 转账给融资方时提交的数据",
    point_customerPayToTrafficker -> "融资方回款给贸易商时提交的数据",
    point_traffickerNoticePortReleaseGoods -> "贸易商通知港口放货时提交的数据",
    point_portReleaseGoods -> "港口放货时提交的数据",
    point_traffickerAuditIfCompletePayment -> "贸易商确认, 是否已经回款完成",
    point_traffickerConfirmPayToFundProvider -> "贸易商确认同意回款给资金方时提交的数据",
    point_traffickerFinancePayToFundProvider -> "贸易商财务付款给资金方时提交的数据"

  )

  /**
    * 数据采集点Actor名称
    */
  val data_AB = "AB"
  val data_start = "start"
  val data_transferAccountsToCustomer = "serviceTransferAccountsToCustomer"

  /**
    * 数据采集点与数据点对应关系
    */
  val dataPointMap: Map[String, TaskInfo] = Map(
    data_start -> TaskInfo("描述", Seq(point_start)),
    data_transferAccountsToCustomer -> TaskInfo("描述", Seq(point_serviceTransferAccountsToCustomer))
  )


  /**
    * 所有用户任务
    */
  val task_A = "A"

  val task_traffickerAssignUsers = "traffickerAssignUsersTask"
  val task_customerUploadContract = "customerUploadContractTask"
  val task_supervisorUploadContract = "supervisorUploadContractTask"
  val task_portUploadContract = "portUploadContractTask"
  val task_traffickerAudit = "traffickerAuditTask"
  val task_traffickerFinanceAudit = "traffickerFinanceAuditTask"
  val task_fundProviderAudit = "fundProviderAuditTask"
  val task_fundProviderFinanceLoad = "fundProviderFinanceLoadTask"
  val task_customerPayToTrafficker = "customerPayToTraffickerTask"
  val task_traffickerNoticePortReleaseGoods = "traffickerNoticePortReleaseGoodsTask"
  val task_portReleaseGoods = "portReleaseGoodsTask"
  val task_traffickerAuditIfCompletePayment = "traffickerAuditIFCompletePaymentTask"
  val task_traffickerConfirmPayToFundProvider = "traffickerConfirmPayToFundProviderTask"
  val task_traffickerFinancePayToFundProvider = "traffickerFinancePayToFundProviderTask"


  /**
    * 所有用户任务与数据点的对应关系
    */
  val taskPointMap: Map[String, TaskInfo] = Map(
    task_traffickerAssignUsers -> TaskInfo("描述", Seq(point_traffickerAssignUsers)),
    task_customerUploadContract -> TaskInfo("描述", Seq(point_customerUploadContract)),
    task_supervisorUploadContract -> TaskInfo("描述", Seq(point_supervisorUploadContract)),
    task_portUploadContract -> TaskInfo("描述", Seq(point_portUploadContract)),
    task_traffickerAudit -> TaskInfo("描述", Seq(point_traffickerAudit)),
    task_traffickerFinanceAudit -> TaskInfo("描述", Seq(point_traffickerFinanceAudit)),
    task_fundProviderAudit -> TaskInfo("描述", Seq(point_fundProviderAudit)),
    task_fundProviderFinanceLoad -> TaskInfo("描述", Seq(point_fundProviderFinanceLoad)),
    task_customerPayToTrafficker -> TaskInfo("描述", Seq(point_customerPayToTrafficker)),
    task_traffickerNoticePortReleaseGoods -> TaskInfo("描述", Seq(point_traffickerNoticePortReleaseGoods)),
    task_portReleaseGoods -> TaskInfo("描述", Seq(point_portReleaseGoods)),
    task_traffickerAuditIfCompletePayment -> TaskInfo("描述", Seq(point_traffickerAuditIfCompletePayment)),
    task_traffickerConfirmPayToFundProvider -> TaskInfo("描述", Seq(point_traffickerConfirmPayToFundProvider)),
    task_traffickerFinancePayToFundProvider -> TaskInfo("描述", Seq(point_traffickerFinancePayToFundProvider))
  
  )


  /**
    * 所有决策点名称
    */
  val V0 = "V0"
  val judge_afterStart = "judge_afterStart"
  val judge_afterTraffickerAssignUsers = "judge_afterTraffickerAssignUsers"
  val judge_afterCustomerUploadContract = "judge_afterCustomerUploadContract"
  val judge_afterPortUploadContract = "judge_afterPortUploadContract"
  val judge_afterSupervisorUploadContract = "judge_afterSupervisorUploadContract"
  val judge_afterTraffickerAudit = "judge_afterTraffickerAudit"
  val judge_afterTraffickerFinanceAudit = "judge_afterTraffickerFinanceAudit"
  val judge_afterFundProviderAudit = "judge_afterFundProviderAudit"
  val judge_afterFundProviderFinanceLoad = "judge_afterFundProviderFinanceLoad"
  val judge_afterCustomerPayToTrafficker = "judge_afterCustomerPaymentToTrafficker"
  val judge_afterTraffickerNoticePortReleaseGoods = "judge_afterTraffickerNoticePortReleaseGoods"
  val judge_afterPortReleaseGoods = "judge_afterPortReleaseGoods"
  val judge_afterTraffickerAuditIfCompletePayment = "judge_afterTraffickerAuditCompletePayment"
  val judge_afterTraffickerConfirmPayToFundProvider = "judge_afterTraffickerConfirmPayToFundProvider"
  val judge_afterTraffickerFinancePayToFundProvider = "judge_afterTraffickerFinancePayToFundProvider"


  /**
    * 所有决策点描述
    */
  val judgeDescription = Map[String, String](
    judge_afterStart -> "流程开始后, 产生1个任务, 任务到贸易商, 待贸易商分配人员.",
    judge_afterTraffickerAssignUsers -> "贸易商分配好人员后, 产生3个任务, 3个任务分别到达港口人员,待港口人员, 监管人员, 融资方上传合同.",
    judge_afterCustomerUploadContract -> "融资方上传合同,文件,提交后, 流程到达贸易商.",
    judge_afterPortUploadContract -> "港口方上传合同,填写确认吨数,提交后, 流程到达贸易商.",
    judge_afterSupervisorUploadContract -> "监管费上传完合同, 提交后,流程到达贸易商.",
    judge_afterTraffickerAudit -> "贸易商审核后, 如果审核不通过, 流程直接结束, 如果审核通过, 产生1个任务, 流程到达贸易商财务.",
    judge_afterTraffickerFinanceAudit -> "贸易商财务确认放款金额, 给出放款建议, 提交后, 产生1个任务, 流程到达资金方.",
    judge_afterFundProviderAudit -> "资金方审核后, 如果审核不通过, 流程直接结束, 如果审核通过, 产生1个任务, 流程到达资金方财务, 待资金方财务放款.",
    judge_afterFundProviderFinanceLoad -> "资金方财务放款完成后, 流程进入回款环节, 产生1个任务, 流程到达融资方, 待融资方回款给贸易商.",
    judge_afterCustomerPayToTrafficker -> "融资方回款, 付款给贸易商后, 产生1个任务, 流程到达贸易商, 待贸易商通知港口放货.",
    judge_afterTraffickerNoticePortReleaseGoods -> "贸易商通知港口放款后, 产生1个任务, 流程到达港口, 待港口放货.",
    judge_afterPortReleaseGoods -> "港口放货后, 产生1个任务, 流程到达贸易商, 待贸易商审核是否已经回款完成.",
    judge_afterTraffickerAuditIfCompletePayment -> "贸易商审核是否已经回款完成, 如果回款未完成, 流程到达融资方, 继续回款, 如果已经回款完成, 流程再次到贸易商, 待贸易商确认付款给资金方.",
    judge_afterTraffickerConfirmPayToFundProvider -> "贸易商确认同意回款给资金方后, 产生1个任务, 流程到达贸易商财务, 待贸易商财务付款给资金方.",
    judge_afterTraffickerFinancePayToFundProvider -> "贸易商财务付款给资金方后, 流程走完, 结束."

  )


  /**
    * 所有数据 key 值
    */
  val key_initData = "initData"
  val key_traffickerUserId = "traffickerUserId"
  val key_traffickerFinanceUserId = "traffickerFinanceUserId"
  val key_portUserId = "portUserId"
  val key_supervisorUserId = "supervisorUserId"
  val key_fundProviderUserId = "fundProviderUserId"
  val key_fundProviderFinanceUserId = "fundProviderFinanceUserId"
  val key_customerUploadContractFile = "customerUploadContractFile"
  val key_supervisorUploadContractFile = "supervisorUploadContractFile"
  val key_portUploadContractFile = "portUploadContractFile"
  val key_traffickerAuditData = "traffickerAuditData"
  val key_traffickerFinanceAuditData = "traffickerFinanceAuditData"
  val key_fundProviderAuditData = "fundProviderAuditData"
  val key_fundProviderFinanceLoadStatus = "fundProviderFinanceLoadStatus"
  val key_serviceTransferAccountsToCustomerStatus = "serviceTransferAccountsToCustomerStatus"
  val key_customerPaymentToTraffickerData = "customerPaymentToTraffickerData"
  val key_traffickerNoticePortReleaseGoodsData = "traffickerNoticePortReleaseGoodsData"
  val key_portReleaseGoodsData = "portReleaseGoodsData"
  val key_traffickerAuditIfCompletePayment = "traffickerAuditIfCompletePayment"
  val key_traffickerConfirmPayToFundProviderStatus = "traffickerConfirmPayToFundProviderStatus"
  val key_traffickerFinancePayToFundProviderStatus = "traffickerFinancePayToFundProviderStatus"

  val keyDescription = Map[String, String](
    key_initData -> "初始化数据",
    key_traffickerUserId -> "初始化时, 带入流程的贸易商用户id",
    key_traffickerFinanceUserId -> "初始化时, 带入流程的贸易商财务人员用户id",
    key_portUserId -> "贸易商选择的港口用户id",
    key_supervisorUserId -> "贸易商选择的监管用户id",
    key_fundProviderUserId -> "贸易商选择的资金方用户id",
    key_fundProviderFinanceUserId -> "贸易商选择的资金方财务人员用户id",
    key_customerUploadContractFile -> "融资方上传的合同,财务等文件",
    key_supervisorUploadContractFile -> "监管方上传的合同文件",
    key_portUploadContractFile -> "港口上传的合同文件, 输入的确认吨数",
    key_traffickerAuditData -> "贸易商审核提交的数据, 审核状态id, 货权接收方公司名称",
    key_traffickerFinanceAuditData -> "贸易商财务审核提交的数据, 放款金额, 放款建议",
    key_fundProviderAuditData -> "资金方审核提交的数据, 审核状态id",
    key_fundProviderFinanceLoadStatus -> "资金方财务放款状态id, 0 or 1",
    key_serviceTransferAccountsToCustomerStatus -> "系统转账给融资方状态id, 0 or 1",
    key_customerPaymentToTraffickerData -> "融资方付款给贸易商提交的数据, 回款本金, 回款利息",
    key_traffickerNoticePortReleaseGoodsData -> "贸易商通知港口放货数据, 放货吨数, 货权接收方",
    key_portReleaseGoodsData -> "港口每次放货状态数据, 第几次, 状态",
    key_traffickerAuditIfCompletePayment -> "贸易商每次确认是否回款完成状态数据, 第几次, 状态",
    key_traffickerConfirmPayToFundProviderStatus -> "贸易商确认同意回款给资金方状态, 0 or 1",
    key_traffickerFinancePayToFundProviderStatus -> "贸易商财务回款给资金方状态, 0 or 1"
  )

  /**
    * 流程所有状态 status
    */
//  val status_waitTraffickerAssignUsers = Map[String, String]("status" -> "financingStep11")
//  val status_waitThreePartyUploadContractFile = Map[String, String]("status" -> "financingStep12", "subStatus1" -> "0", "subStatus2" -> "0", "subStatus3" -> "0")
//  val status_waitTraffickerAudit = Map[String, String]("status" -> "financingStep13")
//  val status_waitForTraffickerFinanceAudit = Map[String, String]("status" -> "financingStep14")
//  val status_waitForFundProviderAudit = Map[String, String]("status" -> "financingStep15")
//  val status_waitForFundProviderFinanceLoad = Map[String, String]("status" -> "financingStep16")
//  val status_waitServiceTransferToCustomer = Map[String, String]("status" -> "financingStep17")
//
//  val status_waitCustomerPayToTrafficker = Map[String, String]("status" -> "repaymentStep31")
//  val status_waitCustomerContinuePayToTrafficker = Map[String, String]("status" -> "repaymentStep32")
//  val status_waitTraffickerNoticePortReleaseGoods = Map[String, String]("status" -> "repaymentStep33")
//  val status_waitPortReleaseGoods = Map[String, String]("status" -> "repaymentStep34")
//  val status_waitTraffickerAuditIfCompletePayment = Map[String, String]("status" -> "repaymentStep35")
//  val status_waitTraffickerConfirmPayToFundProvider = Map[String, String]("status" -> "repaymentStep36")
//  val status_waitTraffickerFinancePayToFundProvider = Map[String, String]("status" -> "repaymentStep37")
//
//  val status_traffickerAuditNotPass = Map[String, String]("status" -> "financingStep51")
//  val status_fundProviderAuditNotPass = Map[String, String]("status" -> "financingStep52")
//  val status_completed = Map[String, String]("status" -> "financingStep53")
//  val status_endByUnusualSituation = Map[String, String]("status" -> "financingStep54")
//
//  val statusDescription = Map[Map[String, String], String](
//    status_waitTraffickerAssignUsers -> "等待贸易商选择港口,监管和监管方",
//    status_waitThreePartyUploadContractFile -> "等待融资方,港口,监管上传合同和单据等文件, 此状态包含3个子状态, subStatus1: 融资方上传合同状态, 0:未上传, 1:已上传, subStatus2: 港口上传合同状态, subStatus3: 监管方上传合同状态",
//    status_waitTraffickerAudit -> "等待贸易商审核",
//    status_traffickerAuditNotPass -> "贸易商审核不通过",
//    status_waitForTraffickerFinanceAudit -> "等待贸易商财务审核, 给出确认金额等",
//    status_waitForFundProviderAudit -> "等待资金方审核",
//    status_fundProviderAuditNotPass -> "资金方审核不通过",
//    status_waitForFundProviderFinanceLoad -> "等待资金方财务放款",
//    status_waitServiceTransferToCustomer -> "资金方财务放款后, 系统检测前是否到贸易商账上, 等待系统转账给融资方",
//    status_waitCustomerPayToTrafficker -> "等待融资方付款给贸易商",
//    status_waitCustomerContinuePayToTrafficker -> "等待融资方继续付款给贸易商, 不是第一次付款",
//    status_waitTraffickerNoticePortReleaseGoods -> "等待贸易商通知港口放货",
//    status_waitPortReleaseGoods -> "等待港口放货",
//    status_waitTraffickerAuditIfCompletePayment -> "等待贸易商确认是否已经回款完成",
//    status_waitTraffickerConfirmPayToFundProvider -> "等待贸易商确认是否付款给资金方",
//    status_waitTraffickerFinancePayToFundProvider -> "等待贸易商财务付款给资金方",
//    status_completed -> "流程完成",
//    status_endByUnusualSituation -> "非正常情况结束, 处置货权"
//  )




}
