/**
 * Created by JinWYP on 09/12/2016.
 */


var avalon = require('avalon2') ;
var userService = require('../service/user.js') ;
var orderService = require('../service/financeOrder.js') ;

avalon.filters.rolename= function (str) {
    if (!str){
        return str
    }
    return userService.userRoleAllObject[str]
};

avalon.filters.statusname= function (str) {
    if (!str){
        return str
    }
    return orderService.statusObject[str]
};

avalon.filters.typename= function (str) {
    if (!str){
        return str
    }else {
        if (str === 'MYR') return '煤易融';
        if (str === 'MYD') return '煤易贷';
        if (str === 'MYG') return '煤易购';
    }
};


avalon.filters.deposittype= function (str) {
    if (!str){
        return str
    }

    return orderService.depositType[str]
};