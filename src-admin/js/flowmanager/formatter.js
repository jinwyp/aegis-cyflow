/**
 * Created by JinWYP on 09/01/2017.
 */


var testData1 = {
    "initial"    : "START",
    "graphJar"   : "com.yimei.cflow.graph.money.MoneyGraphJar",
    "timeout"    : 100,
    "persistent" : true,

    "vertices" : {
        "START" : {
            "description" : "发起申请",
            "program"     : "import com.yimei.cflow.api.models.flow._; (state: State) => Seq(Arrow(\"V1\", Some(\"E1\")))"
        }
    },
    "edges"    : {
        "E1" : {
            "name"       : "E1",
            "begin"      : "V0",
            "end"        : "V1",
            "userTasks"  : [],
            "partGTasks" : [],
            "partUTasks" : [],
            "autoTasks"  : []
        }
    }
};


var globalConfig = {
    initial : 'START',  // 代表初始节点
    graphJar   : "com.yimei.cflow.graph.money.MoneyGraphJar",
    timeout: 100, // 超时时间配置(图的属性)
    flowType: '',  //流程类型
    persistent: false // 代表初始节点
};


var singleVertex = {
    "id" : "START",
    "description" : "发起申请",
    "program"     : "import com.yimei.cflow.api.models.flow._; (state: State) => Seq(Arrow(\"V1\", Some(\"E1\")))"
};
var verticesArray = [];

var edge = {
    "id"         : "E1",
    "name"       : "E1",
    "begin"      : "V0",
    "end"        : "V1",
    "userTasks"  : [],
    "partGTasks" : [],
    "partUTasks" : [],
    "autoTasks"  : []
}
var edgeArray = []

var point = {
    "id" : "P1",
    "description" : "申请成功率"
}


(function() {
    'use strict';

   var formatter = {

       cyArrayToRawArray : function(){

       },

       rawArrayToObj : function () {

       }
   }
})();