/**
 * Created by liushengbin on 2016/12/22.
 */
(function(window, $, ejs){

    $.ajaxSettings.async = false;

    var currentPage=1;
    var pageSize=10;
    var container = $("#panel-pagination");

    var newDataList = {'flows' : []};

    // var dataTest;
    // $.getJSON('./json/dataList.json', function(res){
    //     dataTest = res.dataList;
    // });
    //
    // var sources = function () {
    //     var result = [];
    //     for (var i = 1; i < 110; i++) {
    //         result.push(i);
    //     }
    //     return result;
    // }();

    var PAGE = function() {
        return {
            init :       function () {
                console.log('------init------');
                getData();

                container.pagination({
                    dataSource : function () {
                        var result = [];
                        for (var i = 1; i < newDataList.length; i++) {
                            result.push(i);
                        }
                        return result;
                    },
                    pageNumber : currentPage,
                    pageSize :   pageSize,
                    callback :   function (data, pagination) {
                        currentPage = pagination.pageNumber;
                        pageSize = pagination.pageSize;
                        console.log('------callback------' + currentPage);

                        // formatData(dataTest[0]);
                        // var history = ejs.compile($('#tmpl_table').html())(dataTest[0]);

                        var history = ejs.compile($('#tmpl_table').html())(newDataList);
                        $('#table-list').html(history);
                    }
                });
            }
        }
    };

    function formatData (data) {
        newDataList.flows = newDataList.flows.splice(0, newDataList.length);
        data.flows.forEach(function (item) {
            item.company_type = item.user_type.split('-')[0];
            item.company_id = item.user_type.split('-')[1];
            newDataList.flows.push(item);
        });
    }

    window.PAGE = PAGE;

    new PAGE().init();

    function getData (){
        var company_type = $("#input-company-type").val();
        var company_id = $("#input-company-id").val();
        var userId = $("#input-user-id").val();
        var flowId = $("#input-flow-id").val();
        var flowType = $("#input-flow-type").val();
        var flowState = $("#input-flow-state").val();

        var temp = "";
        if(!(flowId==null || flowId=="")){
            temp = temp +"flowId="+flowId+"&"
        }
        if(!(flowType==null || flowType=="")){
            temp = temp +"flowType="+flowType+"&"
        }
        if(!(company_type==null || company_type==""||company_id==null || company_id=="")){
            temp = temp +"userType="+company_type+"-"+company_id+"&"
        }
        if(!(userId==null || userId=="")){
            temp = temp +"userId="+userId+"&"
        }
        if(!(flowState==null || flowState=="")) {
            temp = temp + "status=" + flowState + "&"
        }
        var url = "";

        if(temp != ""){
            url = "/api/flow?"+temp.substring(0,temp.length-1)+"&page="+currentPage+"&pageSize="+pageSize;
        } else {
            url = "/api/flow?page="+currentPage+"&pageSize="+pageSize;
        }

        $.ajax({
            method: "get",
            url: url
        }).done(function (data) {
            formatData(data);
            // console.dir(data);
        });
    }

    $(".btn-submit").click(function(){
        currentPage = 1;
        getData();
        var history = ejs.compile($('#tmpl_table').html())(newDataList);
        $('#table-list').html(history);
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