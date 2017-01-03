/**
 * Created by JinWYP on 01/12/2016.
 */



var avalon = require('avalon2') ;
require('../component/header.js');
require('../component/pagination.js');

var userService = require('../service/user.js') ;



var userList = function() {

    var vm = avalon.define({
        $id : 'userListController',
        userList : [],
        searchQuery : {
            username : '',
            companyName : ''
        },

        configPagination : {
            id : 'pagination',
            totalCount : 0,
            currentPage : 1,
            countPerPage : 10,
            changePageNo : function(currentPageNo, skip, countPerPage){
                var query = {
                    $limit: countPerPage,
                    $skip : skip
                };

                getUsers(query)
            }
        },


        clickSearchButton : function (event) {
            event.preventDefault();
            getUsers();
        },

        clickResetPassword:function () {
            $(".modal_1").modal();
        },

        clickDelete:function () {
            $(".modal_2").modal();
        }

    });


    function getUsers(query){
        query = query || {};

        userService.getUserList(query).done(function(data, textStatus, jqXHR) {
            if (data.success){
                vm.userList = data.data;

                vm.configPagination.currentPage = data.meta.page;
                vm.configPagination.totalCount = data.meta.total;

            }else{
                console.log(data.error);
            }
        })
    }


    getUsers();



};

userList();


module.exports = userList;