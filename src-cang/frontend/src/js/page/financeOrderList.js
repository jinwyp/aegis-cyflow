

var avalon = require('avalon2') ;
require('../component/header.js');
require('../component/pagination.js');


var orderService = require('../service/financeOrder.js') ;
var sessionUserId = require('../service/token.js').sessionUserId ;
var sessionUserRole = require('../service/token.js').sessionUserRole ;



var orderList = function() {

    var vm = avalon.define({
        $id : 'orderListController',
        orderList : [],
        currentUser : {
            id : sessionUserId,
            role : sessionUserRole
        },
        status : orderService.statusList,
        searchQuery : {
            orderNo : '',
            status : '',
            skip : 0,
            countPerPage : 10
        },
        configPagination : {
            id : 'pagination',
            totalCount : 0,
            currentPage : 1,
            countPerPage : 10,
            changePageNo : function(currentPageNo, skip, countPerPage){

                vm.searchQuery.countPerPage = countPerPage
                vm.configPagination.countPerPage = countPerPage
                vm.searchQuery.skip = skip
                getOrders()
            }
        },

        clickSearchButton : function(event){
            event.preventDefault()
            vm.searchQuery.skip = 0
            getOrders()
        }

    });


    function getOrders(query){
        query = query || {
            userRole : sessionUserRole,
            userId : sessionUserId
        };

        if (vm.searchQuery.orderNo) query.orderNo = vm.searchQuery.orderNo
        if (vm.searchQuery.status) query.status = vm.searchQuery.status
        if (vm.searchQuery.countPerPage) query.$limit = vm.searchQuery.countPerPage
        if (vm.searchQuery.skip) query.$skip = vm.searchQuery.skip

        orderService.getFinanceOrderList(query).done(function(data, textStatus, jqXHR) {
            if (data.success){
                vm.orderList = data.data;

                vm.configPagination.currentPage = data.meta.page;
                vm.configPagination.totalCount = data.meta.total;
            }else{
                $.notify(data.error.message, 'error');
            }
        })
    }

    getOrders();

};

orderList();


module.exports = orderList;