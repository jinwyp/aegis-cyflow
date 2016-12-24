/**
 * Created by liushengbin on 2016/12/22.
 */
(function(window, $, ejs){

    $.ajaxSettings.async = false;
    //
    // var dataList;
    // var currentPage=1;
    //
    // var container = $("#panel-pagination");
    //
    // $.getJSON('./json/dataList.json', function(res){
    //     dataList = res.dataList;
    // });
    //
    // var sources = function () {
    //     var result = [];
    //     for (var i = 1; i < 110; i++) {
    //         result.push(i);
    //     }
    //     return result;
    // }();
    //
    // var PAGE = function(){
    //     return {
    //         init :       function () {
    //             console.log('------init------');
    //             this.tmplRender(1);
    //
    //             container.pagination({
    //                 dataSource : sources,
    //                 pageNumber: currentPage,
    //                 pageSize : 10,
    //                 callback :   function (data, pagination) {
    //                     currentPage = pagination.pageNumber;
    //                     console.log('------callback------'+currentPage);
    //                     var history = ejs.compile($('#tmpl_table').html())(dataList[currentPage - 1]);
    //                     console.log(history);
    //                     $('#table-list').html(history);
    //
    //                     // console.log(pagination);
    //                     // console.log(pagination.pageRange);
    //                     // console.log(pagination.pageRange-1);
    //                     // console.log(pagination.pageRange-1);
    //                     // if()
    //                     // PAGE().tmplRender(pagination.pageRange-1);
    //                 }
    //             });
    //         },
    //         tmplRender : function (page) {
    //             console.log('------tmplRender------');
    //             // console.log(page);
    //             // console.log(dataList[page - 1]);
    //             // var history = ejs.compile($('#tmpl_table').html())(dataList[page - 1]);
    //             // $('#table-list').html(history);
    //         }
    //     }
    //
    // };
    //
    // window.PAGE = PAGE;
    //
    // new PAGE().init();

    $(".btn-submit").click(function(){
        alert(111);
        var company_type = $("#input-company-type").val();
        var company_id = $("#input-company-id").val();
        var userId = $("#input-user-id").val();
        var flowId = $("#input-flow-id").val();
        var flowType = $("#input-flow-type").val();
        var flowState = $("#input-flow-state").val();
        console.log(company_type);
        console.log(company_id);
        console.log(userId);
        console.log(flowId);
        console.log(flowType);
        console.log(flowState);

       // parameters(("flowId".?, "flowType".?, "userType".?,
       //     "userId".?, "status".as[Int].?, "limit".as[Int].?, "offset".as[Int].?)).as(FlowQuery) { fq =>

        var temp = "";
        if(!company_type.isEmpty()){
            temp = temp +""
        }


            $.ajax({
            url: '/test',
            data: {
                userId: userId,
                userType: userType,
                flowId: flowId,
                flowType: flowType,
            }
        });
    });

    $("#input-user-type").focus(function () {
        if($(".user-type-ul").hasClass('hidden')){
            $(".user-type-ul").removeClass('hidden');
        }else{
            $(".user-type-ul").addClass('hidden');
        }
    });

    $("#input-user-type").blur(function () {
        if($(".user-type-ul").hasClass('hidden')){
            $(".user-type-ul").removeClass('hidden');
        }else{
            $(".user-type-ul").addClass('hidden');
        }
    });

    $(".user-type-ul li").mousedown(function () {
        $("#input-user-type").val($(this).text());
    });

    $("#input-flow-type").focus(function () {
        if($(".flow-type-ul").hasClass('hidden')){
            $(".flow-type-ul").removeClass('hidden');
        }else{
            $(".flow-type-ul").addClass('hidden');
        }
    });

    $("#input-flow-type").blur(function () {
        if($(".flow-type-ul").hasClass('hidden')){
            $(".flow-type-ul").removeClass('hidden');
        }else{
            $(".flow-type-ul").addClass('hidden');
        }
    });

    $(".flow-type-ul li").mousedown(function () {
        $("#input-flow-type").val($(this).text());
    });

    $("#input-status").focus(function () {
        if($(".status-ul").hasClass('hidden')){
            $(".status-ul").removeClass('hidden');
        }else{
            $(".status-ul").addClass('hidden');
        }
    });

    $("#input-status").blur(function () {
        if($(".status-ul").hasClass('hidden')){
            $(".status-ul").removeClass('hidden');
        }else{
            $(".status-ul").addClass('hidden');
        }
    });

    $(".status-ul li").mousedown(function () {
        $("#input-status").val($(this).text());
    });

})(window, jQuery, ejs)