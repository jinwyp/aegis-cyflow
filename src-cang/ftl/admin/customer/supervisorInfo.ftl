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
                                <td>{{@currentOrder.orderType | typename}}</td>

                                <th class="text-right">业务编号:</th>
                                <td>{{@currentOrder.orderNo}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">煤炭仓储地:</th>
                                <td>{{@currentOrder.orderType | typename}}</td>

                                <th class="text-right">仓库性质:</th>
                                <td>{{@currentOrder.orderNo}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">仓库地址:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">我方与港口的历史合作状况:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">经营及堆存状况:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">保管及进出库流程规范程度:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">监管配合状况:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">监管方案:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">结论:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">附件上传:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">是否需要补充材料的说明:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                            <tr>
                                <th class="text-right ">审核意见:</th>
                                <#--<td colspan="3">费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段费附加赛的减肥了圣诞节福利卡时间段</td>-->
                                <td colspan="3">{{@currentOrder.orderType | typename}}</td>
                            </tr>

                        </table>

                    </div>
                </div>
            </div>


            <!-- 动作按钮 -->
            <div class="row" ms-if="@currentUser.role === @role.financer ">
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a12FinishedUpload.statusAt && !@currentOrder.statusChild1Financer" ms-click="doAction(@action.a12FinishedUpload.name)">{{@action.a12FinishedUpload.displayName}}</button>
                </div>

                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a31FirstReturnMoney.statusAt" ms-click="doAction(@action.a31FirstReturnMoney.name)">{{@action.a31FirstReturnMoney.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a32SecondReturnMoney.statusAt" ms-click="doAction(@action.a32SecondReturnMoney.name)">{{@action.a32SecondReturnMoney.displayName}}</button>
                </div>

            <#--<div class="col-sm-2"><button type="button" class="mb-sm btn btn-danger">Success</button></div>-->
            <#--<div class="col-sm-2"><button type="button" class="mb-sm btn btn-info">Success</button></div>-->
            </div>


            <div class="row" ms-if="@currentUser.role === @role.trader ">
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a11SelectHarborAndSupervisor.statusAt" ms-click="doAction(@action.a11SelectHarborAndSupervisor.name)">{{@action.a11SelectHarborAndSupervisor.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a15Approved.statusAt && @currentOrder.statusChild2Harbor && @currentOrder.statusChild3Supervisor" ms-click="doAction(@action.a15Approved.name)">{{@action.a15Approved.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-danger" ms-if="@currentOrder.status === @action.a16NotApproved.statusAt" ms-click="doAction(@action.a16NotApproved.name)">{{@action.a16NotApproved.displayName}}</button>
                </div>

                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-info" ms-if="@currentOrder.status === @action.a32ReturnPortionCargo.statusAt" ms-click="doAction(@action.a32ReturnPortionCargo.name)">{{@action.a32ReturnPortionCargo.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a33ReturnAllCargo.statusAt" ms-click="doAction(@action.a33ReturnAllCargo.name)">{{@action.a33ReturnAllCargo.displayName}}</button>
                </div>

                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a37Punishment.statusAt" ms-click="doAction(@action.a37Punishment.name)">{{@action.a37Punishment.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a38Punishment.statusAt" ms-click="doAction(@action.a38Punishment.name)">{{@action.a38Punishment.displayName}}</button>
                </div>

                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a36ReturnMoney.statusAt" ms-click="doAction(@action.a36ReturnMoney.name)">{{@action.a36ReturnMoney.displayName}}</button>
                </div>

            </div>


            <div class="row" ms-if="@currentUser.role === @role.traderAccountant ">
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a17Approved.statusAt" ms-click="doAction(@action.a17Approved.name)">{{@action.a17Approved.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a37Approved.statusAt" ms-click="doAction(@action.a37Approved.name)">{{@action.a37Approved.displayName}}</button>
                </div>
            </div>


            <div class="row" ms-if="@currentUser.role === @role.harbor">
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a13FinishedUpload.statusAt && !@currentOrder.statusChild2Harbor" ms-click="doAction(@action.a13FinishedUpload.name)">{{@action.a13FinishedUpload.displayName}}</button>
                </div>

                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a34ConfirmPortionCargo.statusAt" ms-click="doAction(@action.a34ConfirmPortionCargo.name)">{{@action.a34ConfirmPortionCargo.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a35ConfirmAllCargo.statusAt" ms-click="doAction(@action.a35ConfirmAllCargo.name)">{{@action.a35ConfirmAllCargo.displayName}}</button>
                </div>
            </div>


            <div class="row" ms-if="@currentUser.role === @role.supervisor ">
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a14FinishedUpload.statusAt && !@currentOrder.statusChild3Supervisor" ms-click="doAction(@action.a14FinishedUpload.name)">{{@action.a14FinishedUpload.displayName}}</button>
                </div>
            </div>


            <div class="row" ms-if="@currentUser.role === @role.fundProvider ">
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a18Approved.statusAt" ms-click="doAction(@action.a18Approved.name)">{{@action.a18Approved.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-danger" ms-if="@currentOrder.status === @action.a19NotApproved.statusAt" ms-click="doAction(@action.a19NotApproved.name)">{{@action.a19NotApproved.displayName}}</button>
                </div>
            </div>


            <div class="row" ms-if="@currentUser.role === @role.fundProviderAccountant ">
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a20Approved.statusAt" ms-click="doAction(@action.a20Approved.name)">{{@action.a20Approved.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a21auto.statusAt" ms-click="doAction(@action.a21auto.name)">{{@action.a21auto.displayName}}</button>
                </div>
                <div class="col-sm-2">
                    <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a22auto.statusAt" ms-click="doAction(@action.a22auto.name)">{{@action.a22auto.displayName}}</button>
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
