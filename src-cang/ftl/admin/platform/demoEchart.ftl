<!DOCTYPE html >
<html lang="zh-cmn-Hans">
<head>

    <title>供应链金融 - 煤易贷 - 仓押管理平台</title>

<#include "../common/headcss.ftl" >
    <link rel="stylesheet" type="text/css" href="${staticPathAdmin}/css/stylesheets/page/home.css"/>
</head>
<body>
<div class="wrapper">

    <!-- top navbar-->
<#include "../common/header.ftl" >


    <!-- sidebar-->
<#include "../common/leftmenu.ftl" >


    <!-- Main section-->
    <section>
        <!-- Page content-->
        <div class="content-wrapper">
            <h3>echart demo
                <small>Standard and custom elements for any form</small>
            </h3>

            <!-- START panel-->
            <div class="panel panel-default">
                <div class="panel-heading">货值趋势图</div>
                <div class="panel-body">
                    <div id="main" style="width: 100%;height: 400px;border: 1px solid gray;"></div>
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
    <script src="${staticPathAdmin}/js/demoEcharts.bundle.js"></script>
<#else>
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/demoEcharts.bundle.js"></script>
</#if>



</body>
</html>
