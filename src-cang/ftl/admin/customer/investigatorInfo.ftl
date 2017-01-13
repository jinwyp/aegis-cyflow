<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>

    <title>供应链金融 - 煤易贷 - 仓押管理平台</title>

<#include "../common/headcss.ftl" >
    <link rel="stylesheet" type="text/css" href="${staticPathAdmin}/css/stylesheets/page/home.css"/>
</head>
<body class="aside-collapsed">
<div class="wrapper">

    <!-- top navbar-->
<#include "../common/header.ftl" >


    <!-- sidebar-->
<#include "../common/leftmenu.ftl" >


    <!-- 贸易商财务   查看详情模块-->
    <section>
        <!-- Page content-->
        <div class="content-wrapper ms-controller" ms-controller="orderInfoController">

            <!--需要修改,暂不确定-->
            <h3>融资管理 - 详情 <a class="mb-sm btn btn-default pull-right" href="/warehouse/admin/home/finance">返回</a> </h3>


            <!--基本信息 显示 贸易商 与 资金方 -->
            <div class="panel panel-default" ms-if="@currentUser.role === @role.financer || @currentUser.role === @role.trader || @currentUser.role === @role.traderAccountant || @currentUser.role === @role.fundProvider || @currentUser.role === @role.fundProviderAccountant">
                <div class="panel-heading">基本信息</div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <tr>
                                <th class="text-right ">业务类型:</th>
                                <td>{{@currentOrder.SPData.orderType}}</td>

                                <th class="text-right">业务编号:</th>
                                <td>{{@currentOrder.SPData.businessCode}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">融资方:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.applyCompanyName}}</td>

                                <th class="text-right">我方签约单位全称:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.ourContractCompany}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">融资金额(万元):</th>
                                <td>{{@currentOrder.SPData.investigationInfo.financingAmount}}</td>

                                <th class="text-right">融资期限:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.financingPeriod}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">利率:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.interestRate}}</td>

                                <th class="text-right">业务开展时间:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.businessStartTime}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">上游签约单位:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.upstreamContractCompany}}</td>

                                <th class="text-right">下游签约单位:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.downstreamContractCompany}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">运输方:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.transportParty}}</td>

                                <th class="text-right">中转港口全称:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.transitPort}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">质量检验单位:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.qualityInspectionUnit}}</td>

                                <th class="text-right">数量检验单位:</th>
                                <td>{{@currentOrder.SPData.investigationInfo.quantityInspectionUnit}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">历史合作情况:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.SPData.investigationInfo.historicalCooperationDetail}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">业务主要信息:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.SPData.investigationInfo.mainBusinessInfo}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">货物流转:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.SPData.investigationInfo.businessTransferInfo}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">业务风险点:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.SPData.investigationInfo.businessRiskPoint}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">履约信用及能力评估:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.SPData.investigationInfo.performanceCreditAbilityEval}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">综合意见:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.SPData.investigationInfo.finalConclusion}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">附件上传:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.SPData.investigationInfo.applyCompanyName}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">是否需要补充材料说明:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.SPData.investigationInfo.applyCompanyName}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">审核意见:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.SPData.investigationInfo.applyCompanyName}}</td>
                            </tr>

                        </table>

                    </div>
                </div>
            </div>
        </div>
    </section>



    <!-- Page footer-->
<#include "../common/footer.ftl" >

</div>



<#if env == 'dev' || env == 'staging' || env == 'prod' >
<!-- 生产环境使用 bundle.js 文件 -->
<script src="${staticPathAdmin}/js/common.bundle.js"></script>
<script src="${staticPathAdmin}/js/financeOrderInfo.bundle.js"></script>
<#else>
<script src="${staticPathAdmin}/js/common.bundle.js"></script>
<script src="${staticPathAdmin}/js/financeOrderInfo.bundle.js"></script>

<!-- 开发环境下 IE8 环境使用 /page-temp-bundle/ 文件 -->

<!--[if lt IE 9]>
<script src="${staticPathAdmin}/js/page-temp-bundle/common.bundle.js"></script>
<script src="${staticPathAdmin}/js/page-temp-bundle/financeOrderInfo.bundle.js"></script>

<![endif]-->

</#if>



</body>
</html>
