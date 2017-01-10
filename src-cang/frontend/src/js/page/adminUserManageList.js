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
            companyName : '',
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

                getUsers(query)
            }
        },


        clickSearchButton : function (event) {
            event.preventDefault();
            getUsers();
        },

        clickResetPassword:function (e) {
            $(".modal_resetP").modal();

            var userId = e.target.id;

            $("#resetPassword").unbind('click').click(function () {
                userService.resetPasswordByUserId(userId).done(function(data) {
                    if (data.success){
                        $(".modal_resetP").modal('hide');
                        setTimeout(function () {
                            $(".modal_resetP_ok").modal();
                        },500);

                    }else{
                        console.log(data.error);
                    }
                })
            })
        },

        clickDelete:function (e) {
            $(".modal_deleteUser").modal();
            var userId = e.target.id;
            $("#deleteUser").unbind('click').click(function () {
                userService.deleteUser(userId).done(function(data) {
                    if (data.success){
                        $(".modal_deleteUser").modal('hide');
                        setTimeout(function () {
                            $(".modal_deleteUser_ok").modal();
                        },500);

                    }else{
                        console.log(data.error);
                    }
                })
            })
        }

    });


    function getUsers(query){
        query = query || {};

        if (vm.searchQuery.username) query.username = vm.searchQuery.username;
        if (vm.searchQuery.companyName) query.companyName = vm.searchQuery.companyName;


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