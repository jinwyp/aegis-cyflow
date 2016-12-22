/**
 * Created by liushengbin on 2016/12/22.
 */
(function(window, $, ejs){

    $.ajaxSettings.async = false;

    var dataList;
    var currentPage=1;

    var container = $("#panel-pagination");

    $.getJSON('./json/dataList.json', function(res){
        dataList = res.dataList;
    });

    var sources = function () {
        var result = [];
        for (var i = 1; i < 110; i++) {
            result.push(i);
        }
        return result;
    }();

    var PAGE = function(){
        return {
            init :       function () {
                this.tmplRender(1);
                console.log("init");
            },
            tmplRender : function (page) {
                console.log(page);
                console.log(dataList[page - 1]);
                var history = ejs.compile($('#tmpl_table').html())(dataList[page - 1]);
                $('#table-list').html(history);
            }
        }

    };

    container.pagination({
        dataSource : sources,
        pageNumber: currentPage,
        pageSize : 10,
        callback :   function (data, pagination) {
            console.log(pagination);
            console.log(pagination.pageRange);
            console.log(pagination.pageRange-1);
            // console.log(pagination.pageRange-1);
            // if()
            PAGE().tmplRender(pagination.pageRange-1);
        }
    })

    window.PAGE = PAGE;

    new PAGE().init();

    $(".btn-submit").click(function(){
        alert(111);
        // var userId = $("#input-user-id").val();
        // var userType = $("#input-user-type").val();
        // var flowId = $("#input-flow-id").val();
        // var flowType = $("#input-flow-type").val();
        // console.log(userId);
        // console.log(userType);
        // console.log(flowId);
        // console.log(flowType);



        // $.ajax({
        //     url: '/test',
        //     data: {
        //         userId: userId,
        //         userType: userType,
        //         flowId: flowId,
        //         flowType: flowType,
        //     }
        // });
    });

})(window, jQuery, ejs)