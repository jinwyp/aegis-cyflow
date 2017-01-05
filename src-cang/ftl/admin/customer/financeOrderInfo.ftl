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


            <!-- 贸易商财务   查看详情模块-->
            <section>
                <!-- Page content-->
                <div class="content-wrapper ms-controller" ms-controller="orderInfoController">

                    <!--需要修改,暂不确定-->
                    <h3>融资管理 - 详情 <a class="mb-sm btn btn-default pull-right" href="/warehouse/admin/home/finance">返回</a> </h3>


                    <!--基本信息 显示 贸易商 与 资金方 -->
                    <div class="panel panel-default" ms-if="@currentUser.role === @role.financer || @currentUser.role === @role.trader || @currentUser.role === @role.traderAccountant || @currentUser.role === @role.fundProvider || @currentUser.role === @role.fundProviderAccountant">
                        <div class="panel-heading">基本信息</div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <tr>
                                        <th class="text-right ">融资类型:</th>
                                        <td>{{@currentOrder.orderType | typename}}</td>

                                        <th class="text-right">业务编号:</th>
                                        <td>{{@currentOrder.orderNo}}</td>

                                        <th class="text-right">申请时间:</th>
                                        <td>{{@currentOrder.requestTime | date("yyyy-MM-dd")}}</td>
                                    </tr>

                                    <tr>
                                        <th class="text-right">融资用户:</th>
                                        <td><span ms-if="@currentOrder.financerUser">{{@currentOrder.financerUser.username}}</span></td>

                                        <th class="text-right">库存港口:</th>
                                        <td><span ms-if="@currentOrder.harborUser">{{@currentOrder.harborUser.username}}</span></td>

                                        <th class="text-right">当前货主:</th>
                                        <td><span ms-if="@currentOrder.cargoOwner">{{@currentOrder.cargoOwner}}</span></td>
                                    </tr>

                                    <tr>
                                        <th class="text-right">融资金额(万元):</th>
                                        <td>{{@currentOrder.mortgageValue}}</td>

                                        <th class="text-right">融资期限(天):</th>
                                        <td>{{@currentOrder.mortgageDeadline}}</td>

                                        <th class="text-right">已缴纳保证金(万元):</th>
                                        <td>{{@currentOrder.depositValue || 0}}</td>
                                    </tr>

                                    <tr>
                                        <th class="text-right">放款总金额(万元):</th>
                                        <td>{{@currentOrder.loanValue || 0}}</td>

                                        <th class="text-right">已回款金额(万元):</th>
                                        <td>{{@currentOrder.returnValue || 0 }}</td>

                                        <th class="text-right">待还款(万元):</th>
                                        <td>{{@currentOrder.repaymentValue || 0}}</td>
                                    </tr>

                                    <tr>
                                        <th class="text-right">质押总数量(吨):</th>
                                        <td>{{@currentOrder.mortgageAmount}}</td>

                                        <th class="text-right">已赎回数量(吨):</th>
                                        <td>{{@currentOrder.redemptionAmount || 0}} </td>

                                        <th class="text-right">待赎回数量(吨):</th>
                                        <td>{{@currentOrder.redemptionAmountLeft || 0}} </td>
                                    </tr>


                                    <tr>
                                        <th class="text-right">港口已确认数量(吨):</th>
                                        <td>{{@currentOrder.harborConfirmAmount || 0}} </td>

                                        <th class="text-right">监管已确认数量(吨):</th>
                                        <td>{{@currentOrder.harborConfirmAmount || 0}} </td>

                                        <th class="text-right">煤种:</th>
                                        <td>{{@currentOrder.infoCoalType || '--'}} </td>
                                    </tr>


                                    <tr>
                                        <th class="text-right">热值:</th>
                                        <td>{{@currentOrder.infoCalorificValue || '--' }} </td>

                                        <th class="text-right">收到基硫分:</th>
                                        <td>{{@currentOrder.infoSulfur || '--' }} </td>

                                        <th class="text-right">空干基挥发分:</th>
                                        <td>{{@currentOrder.infoVolatile || '--'}} </td>
                                    </tr>


                                </table>

                            </div>
                        </div>
                    </div>


                    <!--基本信息 显示 港口 与 监管 -->
                    <div class="panel panel-default" ms-if="@currentUser.role === @role.harbor || @currentUser.role === @role.supervisor ">
                        <div class="panel-heading">基本信息</div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <tr>
                                        <th class="text-right ">融资类型:</th>
                                        <td>{{@currentOrder.orderType | typename}}</td>

                                        <th class="text-right">业务编号:</th>
                                        <td>{{@currentOrder.orderNo}}</td>

                                        <th class="text-right">申请时间:</th>
                                        <td>{{@currentOrder.requestTime | date("yyyy-MM-dd")}}</td>
                                    </tr>

                                    <tr>
                                        <th class="text-right">融资用户:</th>
                                        <td><span ms-if="@currentOrder.financerUser">{{@currentOrder.financerUser.username}}</span></td>

                                        <th class="text-right">库存港口:</th>
                                        <td><span ms-if="@currentOrder.harborUser">{{@currentOrder.harborUser.username}}</span></td>

                                        <th class="text-right">当前货主:</th>
                                        <td><span ms-if="@currentOrder.cargoOwner">{{@currentOrder.cargoOwner}}</span></td>
                                    </tr>

                                    <tr>
                                        <th class="text-right">港口已确认数量(吨):</th>
                                        <td>{{@currentOrder.harborConfirmAmount || 0}} </td>

                                        <th class="text-right">监管已确认数量(吨):</th>
                                        <td>{{@currentOrder.harborConfirmAmount || 0}} </td>

                                        <th class="text-right">煤种:</th>
                                        <td>{{@currentOrder.infoCoalType || '--'}} </td>
                                    </tr>

                                </table>
                            </div>
                        </div>
                    </div>


                    <!--审批详情 审批记录 流程图显示 -->
                    <div class="panel panel-default " >
                        <div class="panel-heading">审批详情</div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <tr>
                                        <th class="text-right">当前状态:</th>
                                        <td>{{@currentOrder.status | statusname}} </td>
                                    </tr>

                                    <tr>
                                        <th class="text-right">子状态:</th>
                                        <td>
                                            <span ms-visible="@currentOrder.statusChild1Financer"> {{@currentOrder.statusChild1Financer | statusname}} <br> </span>
                                            <span ms-visible="@currentOrder.statusChild2Harbor"> {{@currentOrder.statusChild2Harbor | statusname}} <br> </span>
                                            <span ms-visible="@currentOrder.statusChild3Supervisor"> {{@currentOrder.statusChild3Supervisor | statusname}} </span>
                                        </td>
                                    </tr>

                                    <tr ms-for="(index, audit) in @currentOrder.auditHistory">
                                        <th class="text-right">审批时间:</th>
                                        <td>{{audit.updatedAt | date("yyyy-MM-dd:HH:mm:ss ") }}</td>

                                        <th class="text-right">审批人:</th>
                                        <td>{{audit.username}}</td>
                                    </tr>
                                </table>

                            </div>
                        </div>
                    </div>










                    <!--货值趋势图-->
                    <div class="panel panel-default" >
                        <div class="panel-heading">货值趋势图</div>
                        <div class="panel-body">
                            <div id="main" style="width: 100%;height: 400px;border: 1px solid gray;"></div>
                        </div>
                    </div>






                    <!-- 贸易商通知融资方缴纳保证金-->
                    <div class="panel panel-default" ms-if="@currentUser.role === @role.trader" >
                        <div class="panel-heading">通知融资方缴纳保证金 </div>
                        <div class="panel-body">
                            <form class="form-horizontal" novalidate>

                                <div class="form-group" ms-class="[@errorDepositValue && 'has-error']">
                                    <label class="col-sm-3 control-label">保证金金额(万元):</label>
                                    <div class="col-sm-3">
                                        <input type="text" class="form-control" ms-duplex-number="@inputDepositValue" >
                                    </div>
                                    <div class="col-sm-5" ms-visible="@errorDepositValue">
                                        <span class="help-block">*&nbsp;金额数量不正确, 最少10万元!</span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-sm-offset-3 col-sm-5">
                                        <button class="btn btn-default btn-lg btn-primary" ms-click="@addNotifyDeposit($event)">提交保证金缴纳通知</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>


                    <!-- 融资方查看缴纳保证金通知 -->
                    <div class="panel panel-default" ms-if="@currentUser.role === @role.financer || @currentUser.role === @role.trader" >
                        <div class="panel-heading" ms-visible="@currentUser.role === @role.financer" >需要缴纳的保证金 </div>
                        <div class="panel-heading" ms-visible="@currentUser.role === @role.trader" >保证金缴纳通知记录 </div>
                        <div class="panel-body">
                            <table class="table table-hover table-striped table-bordered">
                                <tr>
                                    <th>创建时间</th>
                                    <th>要缴纳的金额(万元)</th>
                                    <th>交易流水号</th>
                                    <th>缴纳时间</th>
                                    <th>当前状态</th>
                                    <th>操作</th>
                                </tr>

                                <tr ms-for="(index, paymentOrder) in @depositList">
                                    <td>{{ paymentOrder.createdAt | date("yyyy-MM-dd:HH:mm:ss ") }}</td>
                                    <td>{{ paymentOrder.depositValue}}</td>
                                    <td>{{ paymentOrder.paymentNo || '--'}}</td>
                                    <td> <span ms-visible="paymentOrder.confirmDate">{{ paymentOrder.confirmDate | date("yyyy-MM-dd:HH:mm:ss ") }}</span></td>
                                    <td>{{ paymentOrder.depositType | deposittype}}</td>
                                    <td>
                                        <div ms-visible="@currentUser.role === @role.financer && paymentOrder.depositType ==='notified' ">
                                            <input type="text" class="payment-no" placeholder="交易流水号" ms-duplex="@inputPaymentOrderNo">
                                            <button class="btn btn-info" type="button" ms-click="@savePaymentOrder(paymentOrder._id)">确认已缴</button>
                                            <span class="text-danger" ms-visible="@errorPaymentOrderNo">流水号长度少于10位!</span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>



                    <!--还款金额流水记录-->
                    <div class="panel panel-default " >
                        <div class="panel-heading">还款交易记录</div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-hover table-striped table-bordered">
                                    <tr>
                                        <th>交易日期</th>
                                        <th>交易流水号</th>
                                        <th>赎货还款金额(万元)</th>
                                        <th>下次还款计息本金</th>
                                        <th>本次还款计息本金</th>
                                        <th>利息计息天数</th>
                                        <th>利息 <br>(剩余本金 X 天 X 每日利息)</th>
                                        <th>付款方</th>
                                        <th>收款方</th>
                                    </tr>

                                    <tr ms-for="(index, paymentOrder) in @repaymentList">
                                        <td>{{ paymentOrder.createdAt | date("yyyy-MM-dd:HH:mm:ss ") }}</td>
                                        <td>{{ paymentOrder.paymentNo || '--'}}</td>
                                        <td>{{ paymentOrder.redemptionValue}}</td>
                                        <td>{{ paymentOrder.leftPrincipalValue}}  </td>
                                        <td>{{ paymentOrder.leftPrincipalValue  + paymentOrder.redemptionValue}}  </td>
                                        <td> 第 3 天 </td>
                                        <td> {{ paymentOrder.leftPrincipalValue  + paymentOrder.redemptionValue}} x 3 x 0.1 / 360 </td>
                                        <td> - </td>
                                        <td> - </td>
                                    </tr>
                                </table>

                            </div>
                        </div>
                    </div>


                    <!-- 港口返还货物记录-->
                    <div class="panel panel-default " >
                        <div class="panel-heading">港口返还货物记录</div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-hover table-striped table-bordered">
                                    <tr>
                                        <th>交易日期</th>
                                        <th>放货数量（吨）</th>
                                        <th>放货方</th>
                                        <th>收货方</th>
                                        <th>港口确认放货时间</th>
                                        <th>查看放货文件</th>
                                    </tr>

                                    <tr ms-for="(index, delivery) in @deliveryList">
                                        <td>{{ delivery.createdAt | date("yyyy-MM-dd:HH:mm:ss ") }}</td>
                                        <td>{{ delivery.redemptionAmount}}</td>
                                        <td> - </td>
                                        <td> - </td>
                                        <td> <span ms-visible="delivery.confirmDate"> {{ delivery.confirmDate | date("yyyy-MM-dd:HH:mm:ss ")}}  </span> </td>
                                        <td>
                                            <a class="" ms-for="(index, file) in delivery.fileList" ms-click="@getFile($event, file)">{{file.originalFileName}}<br> </a>
                                        </td>
                                    </tr>
                                </table>

                            </div>
                        </div>
                    </div>





                    <!-- 各方合同查看 -->
                    <div class="panel panel-default " >
                        <div class="panel-heading">合同信息</div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-hover contract-table" ms-visible="@currentUser.role === @role.financer || @currentUser.role === @role.trader || @currentUser.role === @role.fundProvider ">
                                    <tr>
                                        <th class="text-right ">融资用户合同及单据:</th>
                                        <td>
                                            <a class="" ms-for="(index, contract) in @contractList | filterBy(@contractFilter, @role.financer)" ms-click="@getFile($event, contract)">{{contract.originalFileName}}</a>
                                        </td>
                                    </tr>
                                </table>

                                <table class="table table-hover contract-table" ms-visible="@currentUser.role === @role.harbor || @currentUser.role === @role.trader || @currentUser.role === @role.fundProvider " >
                                    <tr>
                                        <th class="text-right contract-table">港口方合同及单据:</th>
                                        <td>
                                            <a class="" ms-for="(index, contract) in @contractList | filterBy(@contractFilter, @role.harbor)" ms-click="@getFile($event, contract)">{{contract.originalFileName}}</a>
                                        </td>
                                    </tr>
                                </table>
                                <table class="table table-hover contract-table" ms-visible="@currentUser.role === @role.supervisor || @currentUser.role === @role.trader || @currentUser.role === @role.fundProvider ">
                                    <tr>
                                        <th class="text-right contract-table">监管方合同及单据:</th>
                                        <td>
                                            <a class="" ms-for="(index, contract) in @contractList | filterBy(@contractFilter, @role.supervisor)" ms-click="@getFile($event, contract)">{{contract.originalFileName}}</a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>


                    <!-- 融资方, 港口 与 监管 上传合同-->
                    <div class="panel panel-info" ms-visible="@currentUser.role === @role.financer || @currentUser.role === @role.harbor || @currentUser.role === @role.supervisor ">
                        <div class="panel-heading">上传合同及单据</div>
                        <div class="panel-body upload-box">
                            <table class="table table-hover">
                                <tr ms-for="(index, file) in @uploadFileList">
                                    <td class="border0 text-center">{{file.name}} <a href=""></a></td>
                                    <td class="border0 text-center"><span class="btn btn-primary" ms-click="@deleteFile($event, file)">删除</span></td>
                                </tr>
                            </table>
                        </div>
                        <div class="panel-footer ">
                            <div class="row">
                                <div class="col-sm-5">
                                    <select class="form-control contract-type-select" ms-duplex="@selectedContractType">
                                        <option value="" > -- 请选择类型 --  </option>
                                        <option ms-for="(key, value) in @contractType" ms-attr="{value: key}" >{{value}} </option>
                                    </select>
                                </div>
                                <div class="col-sm-6">
                                    <div id="uploadPicker" class="btn">选择文件并上传</div>
                                </div>
                            </div>
                        </div>
                    </div>



                    <!-- 港口确认货物 -->
                    <div class="panel panel-info" ms-if="@currentUser.role === @role.harbor && !@currentOrder.harborConfirmAmount">
                        <div class="panel-heading">港口确认货物</div>
                        <div class="panel-body">
                            <h4 class="lineH40">
                                当前有 <input type="text" class="goods" ms-duplex-number="@inputHarborConfirmAmount">吨货物 <br>
                                货物属于{{@currentOrder.financerCompanyName || ''}}所有, 并承诺与实际情况相符。
                            </h4>
                            <span class="text-danger" ms-visible="@errorHarborConfirmAmount"> 数量错误!</span>
                        </div>
                    </div>


                    <!-- 港口 与 监管 显示确认货物信息 -->
                    <div class="panel panel-info" ms-if="@currentUser.role === @role.harbor && @currentOrder.harborConfirmAmount || @currentUser.role === @role.supervisor && @currentOrder.harborConfirmAmount">
                        <div class="panel-heading">港口货物确认信息</div>
                        <div class="panel-body">
                            <h4 class="lineH40" ms-visible="@currentOrder.harborConfirmAmount">
                                已确认有 {{@currentOrder.harborConfirmAmount}} 吨货物属于{{@currentOrder.financerCompanyName || ''}}所有, 并承诺与实际情况相符。
                            </h4>
                        </div>
                        <div class="panel-footer text-center">

                        </div>
                    </div>




                    <!-- 贸易商选择 资金方 港口 监管方-->
                    <div class="panel panel-info" ms-if="@currentUser.role === @role.trader && !@currentOrder.harborUserId && !@currentOrder.fundProviderUserId" >
                        <div class="panel-heading">选择资金方,港口和监管方 </div>
                        <div class="panel-body">
                            <form class="form-horizontal" novalidate>

                                <div class="form-group" ms-class="[@traderFormError.fundProvider && 'has-error']">
                                    <label class="col-sm-2 control-label">选择资金方:</label>
                                    <div class="col-sm-3">
                                        <select class="form-control m-b" ms-duplex="@traderForm.selectedFundProvider">
                                            <option value="" > - </option>
                                            <option ms-for="user in @fundProviderList" ms-attr="{value: user._id}" >{{user.username}} </option>
                                        </select>
                                    </div>
                                    <div class="col-sm-5" ms-visible="@traderFormError.fundProvider">
                                        <span class="help-block">*&nbsp;请选择资金方!</span>
                                    </div>
                                </div>

                                <div class="form-group" ms-class="[@traderFormError.fundProviderAccountant && 'has-error']">
                                    <label class="col-sm-2 control-label">选择资金方财务:</label>
                                    <div class="col-sm-3">
                                        <select class="form-control m-b" ms-duplex="@traderForm.selectedFundProviderAccountant">
                                            <option value="" > - </option>
                                            <option ms-for="user in @fundProviderAccountantList" ms-attr="{value: user._id}" >{{user.username}} </option>
                                        </select>
                                    </div>
                                    <div class="col-sm-5" ms-visible="@traderFormError.fundProviderAccountant">
                                        <span class="help-block">*&nbsp;请选择资金方财务!</span>
                                    </div>
                                </div>


                                <div class="form-group" ms-class="[@traderFormError.harbor && 'has-error']">
                                    <label class="col-sm-2 control-label">选择港口:</label>
                                    <div class="col-sm-3">
                                        <select class="form-control m-b" ms-duplex="@traderForm.selectedHarbor">
                                            <option value="" > - </option>
                                            <option ms-for="user in @harborList" ms-attr="{value: user._id}" >{{user.username}} </option>
                                        </select>
                                    </div>
                                    <div class="col-sm-5" ms-visible="@traderFormError.harbor">
                                        <span class="help-block">*&nbsp;请选择港口!</span>
                                    </div>
                                </div>

                                <div class="form-group" ms-class="[@traderFormError.supervisor && 'has-error']">
                                    <label class="col-sm-2 control-label">选择监管方:</label>
                                    <div class="col-sm-3">
                                        <select class="form-control m-b" ms-duplex="@traderForm.selectedSupervisor">
                                            <option value="" > - </option>
                                            <option ms-for="user in @supervisorList" ms-attr="{value: user._id}" >{{user.username}} </option>
                                        </select>
                                    </div>
                                    <div class="col-sm-5" ms-visible="@traderFormError.supervisor">
                                        <span class="help-block">*&nbsp;请选择监管方!</span>
                                    </div>
                                </div>

                            </form>
                        </div>

                    </div>



                    <!-- 贸易商财务 给出放款金额建议 -->
                    <div class="panel panel-info" ms-if="@currentUser.role === @role.traderAccountant && @currentOrder.status === @action.a17Approved.statusAt " >
                        <div class="panel-heading">贸易商财务放款金额建议: </div>
                        <div class="panel-body">
                            <form class="form-horizontal" novalidate>

                                <div class="form-group" ms-class="[@errorActualLoanValue && 'has-error']">
                                    <label class="col-sm-3 control-label">实际放款金额(万元):</label>
                                    <div class="col-sm-3">
                                        <input type="text" class="form-control" ms-duplex-number="@inputActualLoanValue" >
                                    </div>
                                    <div class="col-sm-5" ms-visible="@errorActualLoanValue">
                                        <span class="help-block">*&nbsp;金额数量不正确, 最少10万元!</span>
                                    </div>
                                </div>

                            </form>
                        </div>
                    </div>






                    <!-- 融资方还款 -->
                    <div class="panel panel-info" ms-if="@currentUser.role === @role.financer && @currentOrder.status === @action.a31FirstReturnMoney.statusAt || @currentUser.role === @role.financer && @currentOrder.status === @action.a32SecondReturnMoney.statusAt" >
                        <div class="panel-heading">融资方还款: </div>
                        <div class="panel-body">
                            <form class="form-horizontal" novalidate>

                                <div class="form-group" ms-class="[@errorRepaymentValue && 'has-error']">
                                    <label class="col-sm-3 control-label">还款金额(万元):</label>
                                    <div class="col-sm-3">
                                        <input type="text" class="form-control" ms-duplex-number="@inputRepaymentValue" >
                                    </div>
                                    <div class="col-sm-5" ms-visible="@errorRepaymentValue">
                                        <span class="help-block">*&nbsp;金额数量不正确, 最少10万元!</span>
                                    </div>
                                </div>

                            </form>
                        </div>
                    </div>



                    <!-- 贸易方 输入放货数量 和 上传港口放货文件合同 -->
                    <div class="panel panel-info" ms-visible="@currentUser.role === @role.trader && @currentOrder.status === 'repaymentStep31' && @isNeedDelivery ">
                        <div class="panel-heading">贸易商返还货物信息</div>

                        <div class="panel-body">

                            <form class="form-horizontal" novalidate>

                                <div class="form-group" ms-class="[@errorRedemptionAmount && 'has-error']">
                                    <label class="col-sm-3 control-label">本次放货数量(吨):</label>
                                    <div class="col-sm-3">
                                        <input type="text" class="form-control" ms-duplex-number="@inputRedemptionAmount" >
                                    </div>
                                    <div class="col-sm-5" ms-visible="@errorRedemptionAmount">
                                        <span class="help-block">*&nbsp;数量不正确, 最少10吨!</span>
                                    </div>
                                </div>

                                <div class="form-group" >
                                    <label class="col-sm-3 control-label">上传放货文件</label>
                                    <div class="col-sm-4">
                                        <table class="table table-hover">
                                            <tr>
                                                <td><div id="uploadPickerRedemptionFile" class="btn ">选择放货文件并上传</div></td>
                                            </tr>
                                            <tr ms-for="(index, file) in @inputRedemptionFileList">
                                                <td class="border0 text-center">{{file.name}} <a href=""></a></td>
                                                <td class="border0 text-center"><span class="btn btn-primary">删除</span></td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>

                            </form>

                        </div>
                    </div>






                    <!-- 动作按钮 -->

                    <div class="row" ms-if="@currentUser.role === @role.financer ">
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a12FinishedUpload.statusAt && !@currentOrder.statusChild1Financer" ms-click="doAction(@action.a12FinishedUpload.name)">{{@action.a12FinishedUpload.displayName}}</button>
                        </div>

                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a31FirstReturnMoney.statusAt" ms-click="doAction(@action.a31FirstReturnMoney.name)">{{@action.a31FirstReturnMoney.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a32SecondReturnMoney.statusAt" ms-click="doAction(@action.a32SecondReturnMoney.name)">{{@action.a32SecondReturnMoney.displayName}}</button>
                        </div>

                        <#--<div class="col-sm-2"><button type="button" class="mb-sm btn btn-danger">Success</button></div>-->
                        <#--<div class="col-sm-2"><button type="button" class="mb-sm btn btn-info">Success</button></div>-->
                    </div>


                    <div class="row" ms-if="@currentUser.role === @role.trader ">
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a11SelectHarborAndSupervisor.statusAt" ms-click="doAction(@action.a11SelectHarborAndSupervisor.name)">{{@action.a11SelectHarborAndSupervisor.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a15Approved.statusAt && @currentOrder.statusChild2Harbor && @currentOrder.statusChild3Supervisor" ms-click="doAction(@action.a15Approved.name)">{{@action.a15Approved.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-danger" ms-if="@currentOrder.status === @action.a16NotApproved.statusAt" ms-click="doAction(@action.a16NotApproved.name)">{{@action.a16NotApproved.displayName}}</button>
                        </div>

                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-info" ms-if="@currentOrder.status === @action.a32ReturnPortionCargo.statusAt" ms-click="doAction(@action.a32ReturnPortionCargo.name)">{{@action.a32ReturnPortionCargo.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a33ReturnAllCargo.statusAt" ms-click="doAction(@action.a33ReturnAllCargo.name)">{{@action.a33ReturnAllCargo.displayName}}</button>
                        </div>

                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a37Punishment.statusAt" ms-click="doAction(@action.a37Punishment.name)">{{@action.a37Punishment.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a38Punishment.statusAt" ms-click="doAction(@action.a38Punishment.name)">{{@action.a38Punishment.displayName}}</button>
                        </div>

                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a36ReturnMoney.statusAt" ms-click="doAction(@action.a36ReturnMoney.name)">{{@action.a36ReturnMoney.displayName}}</button>
                        </div>

                    </div>


                    <div class="row" ms-if="@currentUser.role === @role.traderAccountant ">
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a17Approved.statusAt" ms-click="doAction(@action.a17Approved.name)">{{@action.a17Approved.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a37Approved.statusAt" ms-click="doAction(@action.a37Approved.name)">{{@action.a37Approved.displayName}}</button>
                        </div>
                    </div>


                    <div class="row" ms-if="@currentUser.role === @role.harbor">
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a13FinishedUpload.statusAt && !@currentOrder.statusChild2Harbor" ms-click="doAction(@action.a13FinishedUpload.name)">{{@action.a13FinishedUpload.displayName}}</button>
                        </div>

                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a34ConfirmPortionCargo.statusAt" ms-click="doAction(@action.a34ConfirmPortionCargo.name)">{{@action.a34ConfirmPortionCargo.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a35ConfirmAllCargo.statusAt" ms-click="doAction(@action.a35ConfirmAllCargo.name)">{{@action.a35ConfirmAllCargo.displayName}}</button>
                        </div>
                    </div>


                    <div class="row" ms-if="@currentUser.role === @role.supervisor ">
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a14FinishedUpload.statusAt && !@currentOrder.statusChild3Supervisor" ms-click="doAction(@action.a14FinishedUpload.name)">{{@action.a14FinishedUpload.displayName}}</button>
                        </div>
                    </div>



                    <div class="row" ms-if="@currentUser.role === @role.fundProvider ">
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a18Approved.statusAt" ms-click="doAction(@action.a18Approved.name)">{{@action.a18Approved.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-danger" ms-if="@currentOrder.status === @action.a19NotApproved.statusAt" ms-click="doAction(@action.a19NotApproved.name)">{{@action.a19NotApproved.displayName}}</button>
                        </div>
                    </div>

                    <div class="row" ms-if="@currentUser.role === @role.fundProviderAccountant ">
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a20Approved.statusAt" ms-click="doAction(@action.a20Approved.name)">{{@action.a20Approved.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a21auto.statusAt" ms-click="doAction(@action.a21auto.name)">{{@action.a21auto.displayName}}</button>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="mb-sm btn btn-success" ms-if="@currentOrder.status === @action.a22auto.statusAt" ms-click="doAction(@action.a22auto.name)">{{@action.a22auto.displayName}}</button>
                        </div>
                    </div>
                </div>
            </section>



            <!-- Page footer-->
            <#include "../common/footer.ftl" >

</div>



<#if env == 'dev' || env == 'staging' || env == 'prod' >
    <!-- 生产环境使用 bundle.js 文件 -->
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/financeOrderInfo.bundle.js"></script>
<#else>
    <script src="${staticPathAdmin}/js/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/financeOrderInfo.bundle.js"></script>

    <!-- 开发环境下 IE8 环境使用 /page-temp-bundle/ 文件 -->

    <!--[if lt IE 9]>
    <script src="${staticPathAdmin}/js/page-temp-bundle/common.bundle.js"></script>
    <script src="${staticPathAdmin}/js/page-temp-bundle/financeOrderInfo.bundle.js"></script>

    <![endif]-->

</#if>



</body>
</html>
