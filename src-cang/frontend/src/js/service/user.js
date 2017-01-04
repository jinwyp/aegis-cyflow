/**
 * Created by JinWYP on 30/11/2016.
 */


var jQuery = require('jquery');
var headers = require('./token').headers;
var tokenService = require('./token');
var sessionUserId = tokenService.sessionUserId;



var url = require('./token').url;


var userRoleAll = [
    { name:'systemAdmin', displayName : '系统管理员'},
    { name:'financer', displayName : '融资方'},
    { name:'harbor', displayName : '港口'},
    { name:'supervisor', displayName : '监管'},
    { name:'trader', displayName : ' 贸易商'},
    { name:'traderAccountant', displayName : '贸易商财务'},
    { name:'fundProvider', displayName : ' 资金方'},
    { name:'fundProviderAccountant', displayName : '资金方财务'}
];
var userRolePartial = userRoleAll.slice(2);

var userRoleAllObject = {};
var userRoleNameObject = {};

userRoleAll.forEach(function (role, index){
    userRoleAllObject[role.name] = role.displayName;
    userRoleNameObject[role.name] = role.name;
});

exports.userRoleList = userRolePartial;
exports.userRoleAllList = userRoleAll;
exports.userRoleAllObject = userRoleAllObject;
exports.userRoleKeyObject = userRoleNameObject;


exports.login = function (user){

    var params = jQuery.extend({
        email : '',
        password : ''
    }, user);

    return jQuery.ajax({
        contentType : 'application/json',
        dataType : 'json',
        url      : url.login,
        method   : 'POST',
        data     : JSON.stringify(params)});

};

exports.logout = function (user){

    tokenService.removeSessionInLocalStorage();
    window.location.href = '/warehouse/admin/login'
};

exports.getSessionUser = function (query){

    if (sessionUserId){
        var params = jQuery.extend({}, query);

        return jQuery.ajax({
            headers : headers,
            contentType : 'application/json',
            dataType : 'json',
            url      : url.userList + '/' + sessionUserId,
            method   : 'GET',
            data     : params

        });
    }else{
        return {
            done : function () {
                
            }
        }
    }


};

exports.getUserList = function (query){

    var params = jQuery.extend({}, query);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.userList,
        method   : 'GET',
        data     : params

    });

};


exports.getUserInfoById = function (userId, query){

    var params = jQuery.extend({}, query);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.userList + '/' + userId,
        method   : 'GET',
        data     : params

    });

};

exports.addNewUser = function (user){

    var params = jQuery.extend({
        username : '',
        // password : '',
        email : '',
        mobilePhone : '',
        companyName : ''
        // belongToUser : '', // 资金方财务关联资金方用户ID, 贸易商财务关联贸易商用户ID
        // role : userRoleAllObject.systemAdmin
    }, user);


    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.userList,
        method   : 'POST',
        data     : JSON.stringify(params)

    });

};

exports.updateUserInfoById = function (userId, user){

    var params = jQuery.extend({}, user);

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.userList + '/' + userId,
        method   : 'PATCH',
        data     : JSON.stringify(params)

    });

};

exports.modifyPassword = function (userId, oldPassword, newPassword){

    var params = jQuery.extend({}, {
        userId : userId,
        oldPassword : oldPassword,
        newPassword : newPassword
    });

    return jQuery.ajax({
        headers : headers,
        contentType : 'application/json',
        dataType : 'json',
        url      : url.password,
        method   : 'POST',
        data     : JSON.stringify(params)

    });

};

