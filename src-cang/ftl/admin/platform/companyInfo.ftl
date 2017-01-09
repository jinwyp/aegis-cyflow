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
        <div class="content-wrapper ms-controller" ms-controller="companyAddController">

            <h3 ms-visible="@pageShowStatus === 'info'">公司管理 - 查看公司信息 </h3>
            <h3 ms-visible="@pageShowStatus === 'add'">公司管理 - 添加公司信息 </h3>
            <h3 ms-visible="@pageShowStatus === 'edit'">公司管理 - 修改公司信息 </h3>

            <!-- START panel-->
            <div class="panel panel-default " >
                <!--<div class="panel-heading">Form elements</div>-->
                <div class="panel-body">
                    <form class="form-horizontal" data-parsley-validate="" novalidate="" ms-validate="@addValidate">

                        <div class="form-group" ms-class="[@errorInputName.indexOf('inputCompanyName')>-1 && 'has-error' ]">
                            <label for="inputCompanyName" class="col-sm-2 control-label"><span class=" marginR">*</span>公司名称:</label>
                            <div class="col-sm-5">
                                <input id="inputCompanyName" type="text" class="form-control" placeholder="请输入公司名称" ms-visible="@pageShowStatus === 'add' || @pageShowStatus === 'edit'"
                                       ms-duplex="@currentCompany.companyName" ms-rules='{required:true}' data-required-message="请输入公司名称">

                                <p class="form-control-static " ms-visible="@pageShowStatus === 'info'">{{@currentCompany.companyName}}</p>
                            </div>
                            <div class="col-sm-5 help-block" ms-visible="@errorInputName.indexOf('inputCompanyName')>-1">{{@errorMessage.inputCompanyName}}</div>
                        </div>

                        <div class="form-group" ms-class="[@errorInputName.indexOf('inputPartyClass')>-1 && 'has-error' ]">
                            <label class="col-sm-2 control-label "><span class=" marginR">*</span>类型:</label>
                            <div class="col-sm-5">
                                <select name="account" class="form-control" id="inputPartyClass" ms-visible="@pageShowStatus === 'add' "
                                        ms-duplex="@currentCompany.partyClass" ms-rules='{required:true}' data-required-message="请选择用户类型">
                                    <option value="" > - </option>
                                    <option ms-for="role in @roleList" ms-attr="{value: role.name}" >{{role.displayName}} </option>
                                </select>
                                <p class="form-control-static " ms-visible="@pageShowStatus === 'info'">{{@currentCompany.role | rolename}}</p>
                            </div>
                            <div class="col-sm-5 help-block" ms-visible="@errorInputName.indexOf('inputPartyClass')>-1">{{@errorMessage.inputPartyClass}}</div>
                        </div>


                        <div class="form-group">
                            <div class="col-sm-offset-4 col-sm-4">
                                <button class="btn btn-default btn-lg btn-primary" type="submit" ms-click="@addCompany()" ms-visible="@pageShowStatus === 'add'">保存</button>
                                <button class="btn btn-default btn-lg btn-primary" type="submit" ms-click="@editCompany()" ms-visible="@pageShowStatus ===  'edit'">保存</button>
                                <a class="btn btn-default btn-lg marginL" href="/warehouse/admin/home/companylist">返回</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <!-- END panel-->
        </div>
    </section>



    <!-- Page footer-->
    <#include "../common/footer.ftl" >

</div>



<#if env == 'dev' || env == 'staging' || env == 'prod' >
    <!-- 生产环境使用 bundle.js 文件 -->
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/adminCompanyInfo.bundle.js"></script>
<#else>
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/adminCompanyInfo.bundle.js"></script>

<!-- 开发环境下 IE8 环境使用 /page-temp-bundle/ 文件 -->

<!--[if lt IE 9]>
<script src="${staticPathAdmin}/js/page-temp-bundle/common.bundle.js"></script>
<script src="${staticPathAdmin}/js/page-temp-bundle/adminCompanyInfo.bundle.js"></script>

<![endif]-->

</#if>



</body>
</html>
