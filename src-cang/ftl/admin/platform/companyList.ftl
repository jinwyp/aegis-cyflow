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


    <!-- Main section-->
    <section>
        <!-- Page content-->
        <div class="content-wrapper">
            <h3>公司管理</h3>

            <!-- START row-->
            <div class="row">
                <div class="col-md-12">
                    <!-- START panel-->
                    <div class="panel panel-default ms-controller" ms-controller="companyListController">
                        <div class="panel-heading">公司列表</div>

                        <div class="panel-body">
                            <form role="form" class="form-inline">

                                <div class="form-group marginL">
                                    <label for="input-password" class="">公司名称:&nbsp;&nbsp;</label>
                                    <input id="input-password"  class="form-control" type="text" placeholder="公司名称" ms-duplex="@searchQuery.companyName">
                                </div>
                                <div class="form-group marginL">
                                    <button class="btn btn-default btn-primary" ms-click="@clickSearchButton($event)">查询</button>
                                </div>
                                <div class="form-group marginL">
                                    <a href="/warehouse/admin/home/company/add" class="btn btn-default btn-info">添加公司</a>
                                </div>
                            </form>
                        </div>

                        <div class="panel-body">
                            <!-- START table-responsive-->
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover text-center">
                                    <tr>
                                        <td class="text-center">公司名称<br/>(全称)</td>
                                        <td class="text-center">用户类型</td>
                                        <td class="text-center">操作</td>
                                    </tr>
                                    <tr ms-for="(index, company) in @companyList">

                                        <td>{{ company.companyName || '--'}}</td>
                                        <td>{{ company.partyClass | rolename}}</td>

                                        <td>
                                            <a class="btn btn-default marginL" ms-attr="{href:'/warehouse/admin/home/company/'+ company.instanceId + '/edit'}">编辑</a>
                                        </td>
                                    </tr>

                                </table>

                            </div>
                            <!-- END table-responsive-->
                        </div>

                        <div class="panel-footer">

                            <xmp is="ms-pagination2" ms-widget="@configPagination"></xmp>

                        </div>
                    </div>
                    <!-- END panel-->
                </div>

            </div>
            <!-- END row-->


        </div>
    </section>


    <!-- Page footer-->
    <#include "../common/footer.ftl" >


</div>

<#include "../common/modal.ftl" >

<#if env == 'dev' || env == 'staging' || env == 'prod' >
    <!-- 生产环境使用 bundle.js 文件 -->
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/adminCompanyList.bundle.js"></script>
<#else>
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/adminCompanyList.bundle.js"></script>

<!-- 开发环境下 IE8 环境使用 /page-temp-bundle/ 文件 -->

<!--[if lt IE 9]>
<script src="${staticPathAdmin}/js/page-temp-bundle/common.bundle.js"></script>
<script src="${staticPathAdmin}/js/page-temp-bundle/adminCompanyList.bundle.js"></script>

<![endif]-->


</#if>



</body>
</html>
