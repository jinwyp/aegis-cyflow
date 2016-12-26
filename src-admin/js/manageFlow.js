/**
 * Created by JinWYP on 23/12/2016.
 */


(function(window, $, cytoscape, angular){

    $.ajaxSettings.async = false;



    function formatVertex(vobj) {
        var result = [];

        for ( var property in vobj){
            result.push({
                id : property,
                description : vobj[property]
            })
        }

        return result;
    }


    var testData1 = {
        "initial": "V0",
        "graphJar": "com.yimei.cflow.graph.money.MoneyGraphJar",
        "timeout": 100,
        "persistent": true,

        "vertices": {
            "V0": { "description":"发起申请","program": "import com.yimei.cflow.api.models.flow._; (state: State) => Seq(Arrow(\"V1\", Some(\"E1\")))"}
        },
        "edges": {
            "E1": {
                "name": "E1",
                "begin": "V0",
                "end": "V1",
                "userTasks": [],
                "partGTasks": [],
                "partUTasks": [],
                "autoTasks": []
            } }
    };








    angular.module('chartApp', []);

    angular.module('chartApp').controller('formController', formController);


    function formController ($scope){
        vm = this;

        vm.currentVertex = {
            id : '未选择',
            description : ''
        };
        vm.currentTask = {
            id : '未选择',
            description : ''
        };

        vm.newVertex = {
            id : '未选择',
            description : ''
        };

        vm.globalConfig = {
            initial : 'start',  // 代表初始节点
            timeout: 100, // 超时时间配置(图的属性)
            flowType: '',  //流程类型
            persistent: false // 代表初始节点
        };

        vm.points = [];

        vm.groups = [];
        vm.v2g = [];
        vm.g2v = [];
        vm.edges = [];
        vm.vertices = [];

        vm.userTasks = [];
        vm.autoTasks = [];
        vm.partUTasks = [];
        vm.partGTasks = [];


        vm.addNewLine = function (){
            console.log(vm.newVertex)
        }





        var formattedData;
        var chartEventCallback= function(cy){

            cy.nodes('.node').qtip({
                content: function(){
                    return this.data().description;
                },
                show: {
                    event: 'click'
                },
                hide: {
                    event: 'unfocus'
                },
                position: {
                    my: 'bottom center',
                    at: 'top center'
                },
                style: {
                    classes: 'qtip-bootstrap',
                    tip: {
                        width: 16,
                        height: 8
                    }
                }
            })

            cy.nodes('.task').qtip({
                content: function(){
                    return this.data().description;
                },
                position: {
                    my: 'bottom center',
                    at: 'top center'
                },
                style: {
                    classes: 'qtip-bootstrap',
                    tip: {
                        width: 16,
                        height: 8
                    }
                }
            })

            cy.nodes('.node').on('click', function(e){
                console.log('node:', this.data())
                vm.currentVertex.id = this.data().id;
                vm.currentVertex.description = this.data().description;
                scope.$apply();

            })

            cy.nodes('.task').on('click', function(e){
                console.log('task:', this.data())
                vm.currentTask.id = this.data().id;
                vm.currentTask.description = this.data().description;
                scope.$apply();
                //data.original[(data.taskType=='autoTasks')?'autoTasks':'userTasks'][data.id].points.forEach(function(p, pi){
                //    var val;
                //    points.push({'key':p, 'value':val});
                //})
            })

        };



        var app = {
            init : function(){
                $.getJSON('./json/data99.json', function(resultData){
                    formattedData = resultData;
                })

                vm.edges = formattedData.edges;
                vm.vertices = formattedData.nodes;

                this.drawChart();
            },
            drawChart : function(){
                var configChart = {
                    domId : 'chart',
                    userZoomingEnabled: false,
                    eventCB : chartEventCallback
                };

                var cytoscapeChart = new flowChart2(formattedData, configChart);
                console.log(cytoscapeChart.width())
            }
        };

        app.init();

    }



})(window, jQuery, cytoscape, angular);




