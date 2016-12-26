/**
 * Created by liushengbin on 2016/12/22.
 */
(function(window, $, ejs){

    $.ajaxSettings.async = false;

    var dataList;
    var currentPage=1;
    var container = $("#panel-pagination");

    var dataTest;
    $.getJSON('./json/dataList.json', function(res){
        dataTest = res.dataList;
    });

    var sources = function () {
        var result = [];
        for (var i = 1; i < 110; i++) {
            result.push(i);
        }
        return result;
    }();

    var PAGE = function() {
        return {
            init :       function () {
                console.log('------init------');
                this.tmplRender(1);

                container.pagination({
                    dataSource : sources,
                    pageNumber : currentPage,
                    pageSize :   10,
                    callback :   function (data, pagination) {
                        currentPage = pagination.pageNumber;
                        console.log('------callback------' + currentPage);
                        getData();
                        var history = ejs.compile($('#tmpl_table').html())(dataList);
                        // console.log(history);
                        $('#table-list').html(history);
                    }
                });
            },
            tmplRender : function (page) {
                console.log('------tmplRender------');
            }
        }
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
            url = "/api/flow?"+temp.substring(0,temp.length-1)
        } else {
            url = "/api/flow"
        }

        $.ajax({
            method: "get",
            url: url
        }).done(function (data) {
            console.dir(data);
            dataList = data;
            // var table = document.getElementById("mytab");
            // var temp = "<tr>";
            // for (var key in data.flows) {
            //     temp += "<td>" +data.flows[key].user_type.split("-")[0] + "</td>";
            //     temp += "<td>" +data.flows[key].user_type.split("-")[1] + "</td>";
            //     temp += "<td>" +data.flows[key].user_id + "</td>";
            //     temp += "<td>" +data.flows[key].flow_id + "</td>";
            //     temp += "<td>" +data.flows[key].flow_type + "</td>";
            //     temp += "<td>" +data.flows[key].finished + "</td>";
            //     temp += "<td><a  target=\"_blank\" href='/mng/graph.html?id=" +data.flows[key].flow_id + "'>查看</a></td></tr>";
            // }
            //
            // table.innerHTML += temp
        });
    }

    $(".btn-submit").click(function(){

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

    ejs.locals.split = function (name, tag) {
        return name.split(tag)
    }

})(window, jQuery, ejs)