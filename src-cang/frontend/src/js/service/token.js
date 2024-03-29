/**
 * Created by JinWYP on 05/12/2016.
 */


var rawToken = localStorage.getItem('feathers-jwt') || '';
var token = 'Bearer ' + rawToken;
var sessionUserId = localStorage.getItem('sessionUserId') || '';
var sessionUserRole = localStorage.getItem('sessionUserRole') || '';

var headers = {
    "X-Authorization" : token,
    "x-authorization-userid" : sessionUserId
};

var prefix = '/api/cang';

var url = {
    files : '/api/internal/file',
    financeOrderList : prefix + '/financeorders',
    depositList : prefix + '/deposits',
    deliveryList : prefix + '/deliveries',
    contractList : prefix + '/files',
    userList : prefix + '/users',
    companyList : prefix + '/companies',
    login : prefix + '/auth/login',
    session : prefix + '/sessionuser'
};


var saveSessionInLocalStorage = function (jwt, user) {
    localStorage.setItem('feathers-jwt', jwt);
    localStorage.setItem('sessionUserId', user.userId);
    localStorage.setItem('sessionUserRole', user.role);
}

var removeSessionInLocalStorage = function () {
    localStorage.removeItem('feathers-jwt');
    localStorage.removeItem('sessionUserId');
    localStorage.removeItem('sessionUserRole');
}

module.exports = {
    url : url,
    rawToken : rawToken,
    token : token,
    headers : headers,
    sessionUserId : sessionUserId,
    sessionUserRole : sessionUserRole,

    saveSessionInLocalStorage : saveSessionInLocalStorage,
    removeSessionInLocalStorage : removeSessionInLocalStorage
};