<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>
    <title>供应链金融 - 煤易贷 - 仓押管理平台</title>
    <#include "common/headcss.ftl" >
    <link rel="stylesheet" type="text/css" href="${staticPathAdmin}/css/stylesheets/page/login.css"/>
</head>
<body>

<div class="wrapper">
    <div class="row">
        <div class="col-md-2 col-md-offset-4">
            <img src="${staticPathAdmin}/css/images/login_left.png" alt="Image" >
        </div>
        <div class="col-md-3">
            <img src="${staticPathAdmin}/css/images/login_right.png" alt="Image" >
        </div>
    </div>


    <div class="block-center mt-xl wd-xl">
        <!-- START panel-->
        <div class="panel panel-dark panel-flat">
            <div class="panel-body ms-controller" ms-controller="loginController">

                <h4 class="text-center pv">仓押系统登录</h4>

                <form role="form" data-parsley-validate="" novalidate="" class="mb-lg" ms-validate="@loginValidate">
                    <div class="form-group has-feedback " ms-class="[@errorInputName.indexOf('inputUsername')>-1 && 'has-error',  @successInputName.indexOf('inputUsername')>-1 &&'has-success'] ">
                        <input id="inputUsername" type="text" placeholder="用户名" class="form-control" ms-duplex="@user.username" ms-rules='{required:true, minlength:2, maxlength:20}'
                               data-required-message="请输入用户名" data-minlength-message="用户名长度不能少于2位" data-maxlength-message="用户名长度不能大于20位">
                        <span class="fa fa-user form-control-feedback text-muted"></span>
                        <span class="help-block" ms-visible="@errorInputName.indexOf('inputUsername')>-1">{{@errorMessage.inputUsername}}</span>

                    </div>

                    <!--  <div class="form-group has-feedback " ms-class="[@errorInputName.indexOf('inputEmail')>-1 && 'has-error',  @successInputName.indexOf('inputEmail')>-1 &&'has-success'] ">
                        <input id="inputEmail" type="email" placeholder="邮箱" class="form-control" ms-duplex="@user.email | change" ms-rules='{required:true, email:true}'
                               data-required-message="请输入邮箱" data-email-message="邮箱格式不正确">
                        <span class="fa fa-user form-control-feedback text-muted"></span>
                        <span class="help-block" ms-visible="@errorInputName.indexOf('inputEmail')>-1">{{@errorMessage.inputEmail}}</span>
                    </div> -->

                    <div class="form-group has-feedback" ms-class="[@errorInputName.indexOf('inputPassword')>-1 && 'has-error',  @successInputName.indexOf('inputPassword')>-1 &&'has-success'] ">
                        <input id="inputPassword" type="password" placeholder="密码" class="form-control" ms-duplex="@user.password | change" ms-rules='{required:true,minlength:6,maxlength:20}'
                               data-required-message="请输入密码" data-minlength-message="密码长度不能少于6位" data-maxlength-message="密码长度不能大于20位">
                        <span class="fa fa-lock form-control-feedback text-muted"></span>
                        <span class="help-block" ms-visible="@errorInputName.indexOf('inputPassword')>-1">{{@errorMessage.inputPassword}}</span>
                    </div>

                    <span id="error" class="help-block">{{@errorMessage.ajax}}</span>
                    <button type="submit" class="btn btn-block btn-primary mt-lg">登录</button>
                </form>
            </div>
        </div>
        <!-- END panel-->
    </div>

</div>



<#if env == 'dev' || env == 'staging' || env == 'prod' >
<!-- 生产环境使用 bundle.js 文件 -->
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/adminLogin.bundle.js"></script>
<#else>
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/adminLogin.bundle.js"></script>

<!-- 开发环境下 IE8 环境使用 /page-temp-bundle/ 文件 -->

<!--[if lt IE 9]>
<script src="${staticPathAdmin}/js/page-temp-bundle/common.bundle.js"></script>
<script src="${staticPathAdmin}/js/page-temp-bundle/adminLogin.bundle.js"></script>

<![endif]-->


</#if>




</body>
</html>
