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
                <div class="panel-heading">监管报告</div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <tr>
                                <th class="text-right ">业务类型:</th>
                                <td>{{@currentOrder.spData.orderType}}</td>

                                <th class="text-right">业务编号:</th>
                                <td>{{@currentOrder.spData.businessCode}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">煤炭仓储地:</th>
                                <td>{{@currentOrder.spData.supervisorInfo.storageLocation}}</td>

                                <th class="text-right">仓库性质:</th>
                                <td>{{@currentOrder.spData.supervisorInfo.storageProperty}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">仓库地址:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.storageAddress}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">我方与港口的历史合作状况:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.historicalCooperationDetail}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">经营及堆存状况:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.operatingStorageDetail}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">保管及进出库流程规范程度:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.portStandardDegree}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">监管配合状况:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.supervisionCooperateDetail}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">监管方案:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.supervisionScheme}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">结论:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.finalConclusion}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">附件上传:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.applyCompanyName}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">是否需要补充材料的说明:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.applyCompanyName}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">审核意见:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.spData.supervisorInfo.applyCompanyName}}</td>
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
