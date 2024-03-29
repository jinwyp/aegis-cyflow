/**
 * Created by JinWYP on 01/12/2016.
 */



var avalon = require('avalon2') ;
require('../component/header.js');
require('../component/pagination.js');

var userService = require('../service/user.js') ;



var companyList = function() {

    var vm = avalon.define({
        $id : 'companyListController',
        companyList : [],
        searchQuery : {
            companyName : ''
        },

        configPagination : {
            id : 'pagination',
            totalCount : 0,
            currentPage : 1,
            countPerPage : 10,
            changePageNo : function(currentPageNo, skip, countPerPage){
                var query = {
                    count: countPerPage,
                    offset : skip,
                    page : currentPageNo
                };

                getCompanies(query)
            }
        },


        clickSearchButton : function (event) {
            event.preventDefault();
            getCompanies();
        }

    });


    function getCompanies(query){
        query = query || {};

        if (vm.searchQuery.companyName){
            query.companyName = vm.searchQuery.companyName;
        }
        userService.getCompanyList(query).done(function(data, textStatus, jqXHR) {
            if (data.success){
                vm.companyList = data.data;

                vm.configPagination.currentPage = data.meta.page;
                vm.configPagination.totalCount = data.meta.total;

            }else{
                console.log(data.error);
            }
        })
    }


    getCompanies();



};

companyList();


module.exports = companyList;