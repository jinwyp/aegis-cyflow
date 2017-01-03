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
                <div class="content-wrapper ms-controller" ms-controller="passwordController">

                    <h3>基本信息 </h3>

                    <!-- START panel-->
                    <div class="panel panel-default " >
                        <!--<div class="panel-heading">Form elements</div>-->
                        <div class="panel-body">
                            <form method="get" action="/" class="form-horizontal" ms-validate="@validate">
                                
                                <fieldset>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">用户帐号:</label>
                                        <div class="col-sm-5">
                                            <p class="form-control-static">{{@currentUser.username}}</p>
                                        </div>
                                        <div class="col-sm-5 text-danger"></div>
                                    </div>
                                </fieldset>

                                <fieldset>
                                    <div class="form-group">
                                        <label for="input-id-1" class="col-sm-2 control-label">公司名称</label>
                                        <div class="col-sm-5">
                                            <p class="form-control-static" >{{@currentUser.companyName}}</p>
                                        </div>
                                        <div class="col-sm-5 text-danger"></div>
                                    </div>
                                </fieldset>

                                <fieldset>
                                    <div class="form-group">
                                        <label for="input-id-1" class="col-sm-2 control-label">用户类型</label>
                                        <div class="col-sm-5">
                                            <p class="form-control-static" >{{@currentUser.role | rolename}}</p>
                                        </div>
                                        <div class="col-sm-5 text-danger"></div>
                                    </div>
                                </fieldset>

                                <fieldset>
                                    <div class="form-group" ms-class="[@errorInputName.indexOf('inputEmail')>-1 && 'has-error']">
                                        <label class="col-sm-2 control-label">公司邮箱</label>
                                        <div class="col-sm-5">
                                            <input type="text" id="inputEmail" class="form-control" placeholder="请输入公司邮箱" ms-duplex="@currentUser.email" ms-rules="{required:true, email:true}"
                                                data-required-message="请输入公司邮箱" data-email-message="邮箱格式错误">
                                        </div>
                                        <div class="col-sm-5 text-danger" ms-visible="@errorInputName.indexOf('inputEmail')>-1">{{@errorMessage.inputEmail}}</div>
                                    </div>
                                </fieldset>
                                <fieldset class="last-child">
                                    <div class="form-group" ms-class="[@errorInputName.indexOf('inputPhone')>-1 && 'has-error']">
                                        <label class="col-sm-2 control-label">手机号码</label>
                                        <div class="col-sm-5">
                                            <input type="text" id="inputPhone" class="form-control" placeholder="请输入手机号码" ms-duplex="@currentUser.mobilePhone" ms-rules="{required:true, pattern: /^1[358][0123456789]\d{8}$/ }"
                                                   data-required-message="请输入手机号码" data-pattern-message="手机号码格式错误">
                                        </div>
                                        <div class="col-sm-5 text-danger" ms-visible="@errorInputName.indexOf('inputPhone')>-1">{{@errorMessage.inputPhone}}</div>
                                    </div>
                                </fieldset>

                                <div class="btn-edit text-center">
                                    <button class="btn btn-default btn-lg btn-primary" type="submit">保存</button>
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
    <script src="${staticPathAdmin}/js/adminSessionUser.bundle.js"></script>
<#else>
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/adminSessionUser.bundle.js"></script>

<!-- 开发环境下 IE8 环境使用 /page-temp-bundle/ 文件 -->

<!--[if lt IE 9]>
<script src="${staticPathAdmin}/js/page-temp-bundle/common.bundle.js"></script>
<script src="${staticPathAdmin}/js/page-temp-bundle/adminSessionUser.bundle.js"></script>

<![endif]-->

</#if>


</body>
</html>
