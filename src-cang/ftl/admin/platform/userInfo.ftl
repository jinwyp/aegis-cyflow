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
        <div class="content-wrapper ms-controller" ms-controller="userAddController">

            <h3 ms-visible="@pageShowStatus === 'info'">用户管理 - 查看用户信息 </h3>
            <h3 ms-visible="@pageShowStatus === 'add'">用户管理 - 添加用户 </h3>
            <h3 ms-visible="@pageShowStatus === 'edit'">用户管理 - 修改用户信息 </h3>

            <!-- START panel-->
            <div class="panel panel-default " >
                <!--<div class="panel-heading">Form elements</div>-->
                <div class="panel-body">
                    <form class="form-horizontal" data-parsley-validate="" novalidate="" ms-validate="@addValidate">
                        <fieldset>
                            <!--<legend>Classic inputs</legend>-->
                            <div class="form-group" ms-class="[@errorInputName.indexOf('inputUsername')>-1 && 'has-error' ]">
                                <label class="col-sm-2 control-label "><span class=" marginR">*</span>用户帐号:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="inputUsername" class="form-control" placeholder="请输入用户账号" ms-visible="@pageShowStatus === 'add' "
                                           ms-duplex="@currentUser.username" ms-rules='{required:true}' data-required-message="请输入用户账号" >
                                    <p class="form-control-static " ms-visible="@pageShowStatus === 'info' || @pageShowStatus === 'edit'">{{@currentUser.username}}</p>
                                </div>
                                <div class="col-sm-5 help-block" ms-visible="@errorInputName.indexOf('inputUsername')>-1">{{@errorMessage.inputUsername}}</div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="form-group" ms-class="[@errorInputName.indexOf('inputCompanyName')>-1 && 'has-error' ]">
                                <label for="inputCompanyName" class="col-sm-2 control-label "><span class=" marginR">*</span>公司名称:</label>
                                <div class="col-sm-4">
                                    <select name="account" class="form-control" id="inputCompanyName" ms-visible="@pageShowStatus === 'add' "
                                            ms-duplex="@currentCompanyJSON"  data-duplex-changed="@selectCompany">
                                        <option value="" > - </option>
                                        <option ms-for="company in @companyList" ms-attr="{value : @jsonStringfy(company)}">{{company.companyName}}
                                        </option>
                                    </select>
                                    <p class="form-control-static " ms-visible="@pageShowStatus === 'info' || @pageShowStatus === 'edit'">{{@currentUser.companyName}}</p>
                                </div>
                                <div class="col-sm-5 help-block addCompanyNameErrMess" ms-visible="@errorInputName.indexOf('inputCompanyName')>-1">{{@errorMessage.inputCompanyName}}</div>
                            </div>
                        </fieldset>
                        <fieldset >
                            <div class="form-group" ms-class="[@errorInputName.indexOf('inputUserRole')>-1 && 'has-error' ]">
                                <label class="col-sm-2 control-label "><span class=" marginR">*</span>用户类型:</label>
                                <div class="col-sm-4" ms-visible="@currentCompany.partyClass === @role.trader || @currentCompany.partyClass === @role.fundProvider ">
                                    <select name="account" class="form-control" id="inputUserRole" ms-visible="@pageShowStatus === 'add' || @pageShowStatus === 'edit'"
                                            ms-duplex="@currentUser.role">
                                        <option ms-for="role in @userRoleTrader" ms-attr="{value: role.name}" ms-visible="@currentCompany.partyClass === 'trader'">{{role.displayName}} </option>
                                        <option ms-for="role in @userRoleFundProvider" ms-attr="{value: role.name}" ms-visible="@currentCompany.partyClass === 'fundProvider'">{{role.displayName}} </option>
                                    </select>
                                </div>
                                <div class="col-sm-5 help-block" ms-visible="@errorInputName.indexOf('inputUserRole')>-1">{{@errorMessage.inputUserRole}}</div>

                                <div class="col-sm-5" ms-visible="@currentCompany.partyClass !== @role.trader && @currentCompany.partyClass !== @role.fundProvider ">
                                    <p class="form-control-static ">{{@currentUser.role | rolename}} </p>
                                </div>

                            </div>
                        </fieldset>
                        <!--<fieldset ms-visible="@currentUser.role === @role.traderAccountant">-->
                            <!--<div class="form-group" ms-class="[!@isMYSCWValid && 'has-error']">-->
                                <!--<label class="col-sm-2 control-label "><span class=" marginR">*</span>选择贸易商:</label>-->
                                <!--<div class="col-sm-4">-->
                                    <!--<select name="account" class="form-control m-b" id="inputMYSFinance" ms-visible="@pageShowStatus === 'add' || @pageShowStatus === 'edit'"-->
                                            <!--ms-duplex="@currentUser.belongToUser" ms-blur="@isValid">-->
                                        <!--<option value="" > - </option>-->
                                        <!--<option ms-for="trader in @traderList" ms-attr="{value: trader._id}" >{{trader.username}} </option>-->
                                    <!--</select>-->
                                    <!--<span class="help-block m-b-none">*&nbsp;如没有选择的贸易商，请先添加贸易商</span>-->
                                <!--</div>-->
                                <!--<div class="col-sm-5 help-block" ms-visible="!@isMYSCWValid">请选择贸易商</div>-->
                            <!--</div>-->
                        <!--</fieldset>-->
                        <!--<fieldset ms-visible="@currentUser.role === @role.fundProviderAccountant">-->
                            <!--<div class="form-group" ms-class="[!@isMYSCWValid && 'has-error']">-->
                                <!--<label class="col-sm-2 control-label "><span class=" marginR">*</span>选择资金方:</label>-->
                                <!--<div class="col-sm-4">-->
                                    <!--<select name="account" class="form-control m-b" id="inputZJFFinance" ms-visible="@pageShowStatus === 'add' || @pageShowStatus ==='edit'"-->
                                            <!--ms-duplex="@currentUser.belongToUser" ms-blur="@isValid">-->
                                        <!--<option value="" > - </option>-->
                                        <!--<option ms-for="fundProvider in @fundProviderList" ms-attr="{value: fundProvider._id}" >{{fundProvider.username}} </option>-->
                                    <!--</select>-->
                                    <!--<span class="help-block m-b-none">*&nbsp;如没有选择的资金方，请先添加资金方</span>-->
                                <!--</div>-->
                                <!--<div class="col-sm-5 help-block"  ms-visible="!@isMYSCWValid">请选择资金方</div>-->
                            <!--</div>-->
                        <!--</fieldset>-->

                        <fieldset>
                            <div class="form-group" ms-class="[@errorInputName.indexOf('inputFullName')>-1 && 'has-error']">
                                <label class="col-sm-2 control-label "><span class=" marginR">*</span>姓名:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="inputFullName" class="form-control" ms-visible="@pageShowStatus === 'add' || @pageShowStatus === 'edit' " ms-duplex="@currentUser.fullName"
                                           ms-rules='{required:true}' data-required-message="请输入姓名">
                                    <p class="form-control-static " ms-visible=" @pageShowStatus === 'info'">{{@currentUser.name}}</p>
                                </div>
                                <div class="col-sm-5 help-block" ms-visible="@errorInputName.indexOf('inputFullName')>-1">{{@errorMessage.inputFullName}}</div>
                            </div>
                        </fieldset>

                        <fieldset>
                            <div class="form-group" ms-class="[@errorInputName.indexOf('inputEmail')>-1 && 'has-error']">
                                <label class="col-sm-2 control-label "><span class=" marginR">*</span>邮箱:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="inputEmail" class="form-control" ms-visible="@pageShowStatus === 'add' || @pageShowStatus === 'edit' " ms-duplex="@currentUser.email"
                                           ms-rules='{required:true, email:true}' data-required-message="请输入公司邮箱" data-email-message="邮箱格式不正确">
                                    <p class="form-control-static " ms-visible=" @pageShowStatus === 'info'">{{@currentUser.email}}</p>
                                    <span class="help-block m-b-none" ms-visible="@pageShowStatus === 'edit' || @pageShowStatus === 'add'">*&nbsp;为用户发送与找回密码的有效途径</span>
                                </div>
                                <div class="col-sm-5" ms-visible="@pageShowStatus === 'info'">
                                    <button class="btn btn-primary" ms-click="@resetPassword">重置密码</button>
                                    <span class=" marginL">重置后，该密码将发送至该邮箱</span>
                                </div>
                                <div class="col-sm-5 help-block" ms-visible="@errorInputName.indexOf('inputEmail')>-1">{{@errorMessage.inputEmail}}</div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="form-group" ms-class="[@errorInputName.indexOf('inputMobilePhone')>-1 && 'has-error'] ">
                                <label class="col-sm-2 control-label "><span class=" marginR">*</span>手机号码:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="inputMobilePhone" placeholder="请输入手机号码" class="form-control" ms-visible="@pageShowStatus === 'add' || @pageShowStatus === 'edit' " ms-duplex="@currentUser.phone"
                                           ms-rules="{ required:true, pattern: /^1[358][0123456789]\d{8}$/ }" data-required-message="请输入手机号码" data-pattern-message="手机号码不正确">
                                    <p class="form-control-static " ms-visible="@pageShowStatus === 'info'">{{@currentUser.phone}}</p>
                                </div>
                                <div class="col-sm-5 help-block" ms-visible="@errorInputName.indexOf('inputMobilePhone')>-1">{{@errorMessage.inputMobilePhone}}</div>
                            </div>
                        </fieldset>
                        <fieldset class="last-child" ms-visible="@pageShowStatus === 'info' || @pageShowStatus === 'edit'">
                            <div class="form-group">
                                <label class="col-sm-2 control-label ">创建时间:</label>
                                <div class="col-sm-5">
                                    <p class="form-control-static " >{{@currentUser.createdAt || '--'}}</p>
                                </div>
                            </div>
                        </fieldset>

                        <div class="btn-edit text-center">
                            <button class="btn btn-default btn-lg btn-primary" type="submit" ms-click="@addUser()" ms-visible="@pageShowStatus === 'add'">保存</button>
                            <button class="btn btn-default btn-lg btn-primary" type="submit" ms-click="@editUser()" ms-visible="@pageShowStatus ===  'edit'">保存</button>
                            <a class="btn btn-default btn-lg marginL" href="/warehouse/admin/home/userlist">返回</a>
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
    <script src="${staticPathAdmin}/js/adminUserInfo.bundle.js"></script>
<#else>
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/adminUserInfo.bundle.js"></script>

<!-- 开发环境下 IE8 环境使用 /page-temp-bundle/ 文件 -->

<!--[if lt IE 9]>
<script src="${staticPathAdmin}/js/page-temp-bundle/common.bundle.js"></script>
<script src="${staticPathAdmin}/js/page-temp-bundle/adminUserInfo.bundle.js"></script>

<![endif]-->

</#if>



</body>
</html>
