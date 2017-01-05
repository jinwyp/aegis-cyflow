<!-- sidebar-->
<aside class="aside">
    <!-- START Sidebar (left)-->
    <div class="aside-inner">
        <nav data-sidebar-anyclick-close="" class="sidebar">
            <!-- START sidebar nav-->
            <ul class="nav">
                <!-- START user info-->
                <li class="has-user-block">
                    <div id="user-block" class="collapse">
                        <div class="item user-block">
                             <!--User picture-->
                            <div class="user-block-picture">
                                <div class="user-block-status">
                                    <img src="${staticPathAdmin}/css/images/avatar.jpg" alt="Avatar" width="60" height="60" class="img-thumbnail img-circle">
                                    <div class="circle circle-success circle-lg"></div>
                                </div>
                            </div>
                             <!--Name and Job-->
                            <div class="user-block-info">
                                <span class="user-block-name">Hello, Mike</span>
                                <span class="user-block-role">Designer</span>
                            </div>
                        </div>
                    </div>
                </li>
                <!-- END user info-->


                <!-- Iterates over all sidebar items-->
                <li class="nav-heading ">
                    <span data-localize="sidebar.heading.HEADER">仓押管理系统</span>
                </li>


                <li >
                    <a href="#financeorder" title="Layouts" data-toggle="collapse">
                        <em class="icon-note"></em>
                        <span>融资管理</span>
                    </a>
                    <ul id="financeorder" class="nav sidebar-subnav collapse">
                        <li class="sidebar-subnav-header">融资管理</li>
                        <li >
                            <a href="/warehouse/admin/home/finance" id="11">
                                <span>业务列表</span>
                            </a>
                        </li>
                    </ul>
                </li>


                <li >
                    <a href="#sessioninfo" title="Layouts" data-toggle="collapse">
                        <em class="icon-layers"></em>
                        <span>个人信息</span>
                    </a>
                    <ul id="sessioninfo" class="nav sidebar-subnav collapse">
                        <li class="sidebar-subnav-header">个人信息</li>
                        <li >
                            <a href="/warehouse/admin/home/session/info" id="21">
                                <span>基本信息</span>
                            </a>
                        </li>
                        <li >
                            <a href="/warehouse/admin/home/session/password" id="22">
                                <span>修改密码</span>
                            </a>
                        </li>
                    </ul>
                </li>



                <!--
                <li class="nav-heading ">
                    <span data-localize="sidebar.heading.COMPONENTS">Components</span>
                </li>
                -->

                <li class="admin-user-management hidden">
                    <a href="#system" title="Forms" data-toggle="collapse">
                        <em class="icon-grid"></em>
                        <span data-localize="sidebar.nav.form.FORM">系统设置</span>
                    </a>
                    <ul id="system" class="nav sidebar-subnav collapse">
                        <li class="sidebar-subnav-header">系统设置</li>
                        <li>
                            <a href="/warehouse/admin/home/userlist" id="31" >
                                <span>用户管理</span>
                            </a>
                        </li>
                        <li>
                            <a href="/warehouse/admin/home/companylist" id="32" >
                                <span>公司管理</span>
                            </a>
                        </li>
                    </ul>
                </li>







                <li >
                    <a href="#dashboard" title="Dashboard" data-toggle="collapse">
                        <!--<div class="pull-right label label-info">3</div>-->
                        <em class="icon-speedometer"></em>
                        <span data-localize="sidebar.nav.DASHBOARD">控制台</span>
                    </a>
                    <ul id="dashboard" class="nav sidebar-subnav collapse">
                        <li class="sidebar-subnav-header">Dashboard</li>
                        <li >
                            <a href="/warehouse/admin/home/demo/dashboard" title="范例 - 搜索列表">
                                <span>范例 - 搜索列表</span>
                            </a>
                        </li>
                        <li >
                            <a href="/warehouse/admin/home/demo/echart" title="范例 - Echart">
                                <span>范例 - Echart</span>
                            </a>
                        </li>
                    </ul>
                </li>

            </ul>
            <!-- END sidebar nav-->
        </nav>
    </div>
    <!-- END Sidebar (left)-->
</aside>
