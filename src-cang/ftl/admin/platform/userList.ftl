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
            <h3>用户管理</h3>

            <!-- START row-->
            <div class="row">
                <div class="col-md-12">
                    <!-- START panel-->
                    <div class="panel panel-default ms-controller" ms-controller="userListController">
                        <div class="panel-heading">用户列表</div>

                        <div class="panel-body">
                            <form role="form" class="form-inline">
                                <div class="form-group marginL">
                                    <label for="input-email" >用户帐号:&nbsp;&nbsp;</label>
                                    <input id="input-email" type="text" placeholder="用户帐号" class="form-control" ms-duplex="@searchQuery.username">
                                </div>

                                <div class="form-group marginL">
                                    <label for="input-password" >公司名称:&nbsp;&nbsp;</label>
                                    <input id="input-password" type="text" placeholder="公司名称" class="form-control" ms-duplex="@searchQuery.companyName">
                                </div>

                                <div class="form-group marginL">
                                    <button class="btn btn-default btn-primary" ms-click="@clickSearchButton($event)">查询</button>
                                </div>
                                <div class="form-group marginL">
                                    <a href="/warehouse/admin/home/user/add" class="btn btn-default btn-info">添加用户</a>
                                </div>

                            </form>

                        </div>

                        <div class="panel-body">
                            <!-- START table-responsive-->
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover text-center">
                                    <tr>
                                        <td class="text-center">用户账号</td>
                                        <td class="text-center">手机</td>
                                        <td class="text-center">邮箱地址</td>
                                        <td class="text-center">所属公司名称<br/>(全称)</td>
                                        <td class="text-center">用户类型</td>
                                        <td class="text-center">操作</td>
                                    </tr>
                                    <tr ms-for="(index, user) in @userList">
                                        <td><a ms-attr="{href:'/warehouse/admin/home/user/'+ user._id }" >{{ user.username || '--'}}</a></td>
                                        <td>{{ user.mobilePhone || '--'}}</td>
                                        <td>{{ user.email || '--'}}</td>
                                        <td>{{ user.companyName || '--'}}</td>
                                        <td>{{user.role | rolename}}</td>

                                        <td>
                                            <span class="btn btn-default resetPassword" ms-click="@clickResetPassword">重置密码</span>
                                            <a class="btn btn-default marginL" ms-attr="{href:'/warehouse/admin/home/user/'+ user.userId + '/edit'}">编辑</a>
                                            <span class="btn btn-default marginL" ms-click="@clickDelete">删除</span>
                                        </td>
                                    </tr>

                                </table>

                            </div>
                            <!-- END table-responsive-->
                        </div>

                        <div class="panel-footer">

                            <xmp is="ms-pagination2" ms-widget="@configPagination"></xmp>

                            <!--

                            <div class="table-pagination">
                                <span class="table-pagination-first" ms-click="@pageFirst"></span>
                                <span class="table-pagination-prev" ms-click="@pagePrevious"></span>
                                <span class="separator"></span>

                                <span class="table-pagination-input" >页数:
                                    <input class="ui-pg-input ui-corner-all" type="text" size="2" maxlength="2" ms-duplex="@pagination.currentPage"> of {{@pagination.totalPage}}
                                </span>
                                <span class="separator"></span>
                                <span class="table-pagination-next" ms-click="@pageNext"></span>
                                <span class="table-pagination-last" ms-click="@pageLast"></span>

                                <select class="select ui-pg-selbox ui-widget-content ui-corner-all " role="listbox" title="Records per Page" ms-duplex="@pagination.countPerPage">
                                    <option role="option" value="10">10</option>
                                    <option role="option" value="20">20</option>
                                    <option role="option" value="30">30</option>
                                    <option role="option" value="50">50</option>
                                    <option role="option" value="80">80</option>
                                </select>

                                <span class="table-pagination-total">当前 {{@pagination.from}} - {{@pagination.to}},  共 {{@pagination.total}} 条 </span>
                            </div>
                            -->

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
    <script src="${staticPathAdmin}/js/adminUserManageList.bundle.js"></script>
<#else>
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/adminUserManageList.bundle.js"></script>

<!-- 开发环境下 IE8 环境使用 /page-temp-bundle/ 文件 -->

<!--[if lt IE 9]>
<script src="${staticPathAdmin}/js/page-temp-bundle/common.bundle.js"></script>
<script src="${staticPathAdmin}/js/page-temp-bundle/adminUserManageList.bundle.js"></script>

<![endif]-->


</#if>



</body>
</html>
