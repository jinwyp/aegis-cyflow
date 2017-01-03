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
                    <h3>修改密码</h3>

                    <div class="col-sm-12">
                        <!-- START panel-->
                        <div class="panel panel-default">
                            <div class="panel-heading"></div>
                            <div class="panel-body">
                                <form class="form-horizontal" ms-validate="@validate">
                                    <div class="form-group" ms-class="[@errorInputName.indexOf('inputOldPwd')>-1 && 'has-error']">
                                        
                                        <label class="col-sm-2 control-label">当前密码</label>
                                        <div class="col-sm-5">
                                            <input type="password" id="inputOldPwd" placeholder="请输入旧密码" class="form-control" ms-duplex="@modifyPassword.oldPwd" ms-rules="{required:true}"
                                                   data-required-message="请输入旧密码">
                                        </div>
                                        <div class="col-sm-5" ms-visible="@errorInputName.indexOf('inputOldPwd')>-1">{{@errorMessage.inputOldPwd}}</div>
                                    </div>

                                    <div class="form-group" ms-class="[@errorInputName.indexOf('inputNewPwd')>-1 && 'has-error']">
                                        <label class="col-sm-2 control-label">新密码</label>
                                        <div class="col-sm-5">
                                            <input type="password" id="inputNewPwd" placeholder="请输入新密码" class="form-control" ms-duplex="@modifyPassword.newPwd" ms-rules="{required:true, minlength:6, maxlength:20}"
                                                   data-required-message="请输入新密码" data-minlength-message="密码长度必须为6-20位字符" data-maxlength-message="密码长度必须为6-20位字符">
                                        </div>
                                        <div class="col-sm-5" ms-visible="@errorInputName.indexOf('inputNewPwd')>-1">{{@errorMessage.inputNewPwd}}</div>
                                    </div>

                                    <div class="form-group" ms-class="[@errorInputName.indexOf('inputRePwd')>-1 && 'has-error']">
                                        <label class="col-sm-2 control-label">确认密码</label>
                                        <div class="col-sm-5">
                                            <input type="password" id="inputRePwd" placeholder="请再次输入新密码" class="form-control" ms-duplex="@modifyPassword.rePwd" ms-rules="{required:true, minlength:6, maxlength:20, equalto:'inputNewPwd'}"
                                                   data-required-message="请再次输入新密码" data-minlength-message="密码长度必须为6-20位字符" data-maxlength-message="密码长度必须为6-20位字符" data-equalto-message="两次密码输入不一致">
                                        </div>
                                        <div class="col-sm-5" ms-visible="@errorInputName.indexOf('inputRePwd')>-1">{{@errorMessage.inputRePwd}}</div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-offset-2 col-sm-10">
                                            <button type="submit" class="btn btn-sm btn-default btn-lg btn-primary">确认</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <!-- END panel-->
                    </div>
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
