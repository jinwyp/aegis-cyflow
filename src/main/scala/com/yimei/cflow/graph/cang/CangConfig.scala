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
    task_traffickerConfirmPayToFundProvider -> Array(point_traffickerConfirmPayToFundProvider),
    task_traffickerFinancePayToFundProvider -> Array(point_traffickerFinancePayToFundProvider)
  )


  /**
    * 所有决策点名称
    */
  val V0 = "V0"


  /**
    *  所有决策点描述
    */
  val judgeDescription = Map[String, String](

  )

}
