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
                <div class="content-wrapper ms-controller" ms-controller="flowController">
                    <h3>流程图</h3>

                    <!-- START panel-->
                    <div class="panel panel-default">
                        <div class="panel-heading"></div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-12 flow">

                                    <div class="circle" ms-for="(index, vbox) in @flowData.vbox" ms-css="{top: index * 150}" ms-click="@clickVbox(vbox)">
                                        <span class="circle-text"> {{vbox.id}}</span>

                                        <div class="line" ms-for="(index2, line) in @vbox.edge" ms-css="{top: 50, left : index2 * 50 + 25, height : index2 * 150 + 100}">
                                            <span> {{line.id}}</span>
                                        </div>
                                    </div>

                                    <div class="pull-right flow-panel">
                                        <h4>当前选中节点 {{@currentVbox.id || '无'}}</h4>
                                        <form class="form-horizontal">
                                            <div class="form-group">
                                                <div class="col-lg-12">
                                                    <input type="text" placeholder="线名称" class="form-control" ms-duplex="@addLine.id">
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <div class="col-lg-12">
                                                    <input type="text" placeholder="连接到节点" class="form-control" ms-duplex="@addLine.to">
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <div class="col-lg-offset-2 col-lg-10">
                                                    <button type="submit" class="btn btn-sm btn-default" ms-click="@addVbox($event )">添加线</button>
                                                </div>
                                            </div>

                                        </form>

                                    </div>

                                </div>


                            </div>
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
    <script src="${staticPathAdmin}/js/demoFlow.bundle.js"></script>
<#else>
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/demoFlow.bundle.js"></script>
</#if>



</body>
</html>
