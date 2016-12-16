package com.yimei.cflow.graph.cang

/**
  * Created by hary on 16/12/15.
  */
object CangConfig {

  /**
    * 所有的数据点名称
    */
  val point_A = "pa"
  val point_B = "pb"
  val point_X = "px"
  val point_Y = "py"

  val point_start = "startDataPoint"
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
    point_A -> "xxxxx",
    point_B -> "xxxxx",

    point_start -> "进入仓押系统时,带入的数据",
    point_traffickerAssignUsers -> "贸易商分配 港口, 监管, 资金方时提交的数据",
    point_customerUploadContract -> "融资方上传合同,文件时提交的数据",
    point_supervisorUploadContract -> "监管员上传合同时提交的数据",
    point_portUploadContract -> "港口工作人员上传合同时提交的数据",
    point_traffickerAudit -> "贸易商工作人员审核时提交的数据",
    point_traffickerFinanceAudit -> "贸易商财务审核时提交的数据",
    point_fundProviderAudit -> "资金方工作人员审核时提交的数据",
    point_fundProviderFinanceLoad -> "资金方财务放款给贸易商时提交的数据",
    point_serviceTransferAccountsToCustomer -> "系统检测当资金方财务放款到贸易商账上时, 转账给融资方时提交的数据",
    point_customerPayToTrafficker -> "融资方回款给贸易商时提交的数据",
    point_traffickerNoticePortReleaseGoods -> "贸易商工作人员通知港口放货时提交的数据",
    point_portReleaseGoods -> "港口放货时提交的数据",
    point_traffickerAuditIfCompletePayment -> "贸易商确认, 是否已经回款完成",
    point_traffickerConfirmPayToFundProvider -> "贸易商工作人员确认同意回款给资金方时提交的数据",
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
  val dataPointMap: Map[String, Array[String]] = Map(
    data_AB -> Array(point_A, point_B),

    data_start -> Array(point_start),
    data_transferAccountsToCustomer -> Array(point_serviceTransferAccountsToCustomer)

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
  val taskPointMap: Map[String, Array[String]] = Map(
    task_A -> Array(point_X,point_Y),

    task_traffickerAssignUsers -> Array(point_traffickerAssignUsers),
    task_customerUploadContract -> Array(point_customerUploadContract),
    task_supervisorUploadContract -> Array(point_supervisorUploadContract),
    task_portUploadContract -> Array(point_portUploadContract),
    task_traffickerAudit -> Array(point_traffickerAudit),
    task_traffickerFinanceAudit -> Array(point_traffickerFinanceAudit),
    task_fundProviderAudit -> Array(point_fundProviderAudit),
    task_fundProviderFinanceLoad -> Array(point_fundProviderFinanceLoad),
    task_customerPayToTrafficker -> Array(point_customerPayToTrafficker),
    task_traffickerNoticePortReleaseGoods -> Array(point_traffickerNoticePortReleaseGoods),
    task_portReleaseGoods -> Array(point_portReleaseGoods),
    task_traffickerAuditIfCompletePayment -> Array(point_traffickerAuditIfCompletePayment),
    task_traffickerConfirmPayToFundProvider -> Array(point_traffickerConfirmPayToFundProvider),
    task_traffickerFinancePayToFundProvider -> Array(point_traffickerFinancePayToFundProvider)
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
    *  所有决策点描述
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
    judge_afterTraffickerFinancePayToFundProvider -> "贸易商确认同意回款给资金方后, 产生1个任务, 流程到达贸易商财务, 待贸易商财务付款给资金方.",
    judge_afterTraffickerFinancePayToFundProvider -> "贸易商财务付款给资金方后, 流程走完, 结束."

  )

}
