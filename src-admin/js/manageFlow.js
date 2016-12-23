/**
 * Created by JinWYP on 23/12/2016.
 */


(function(window, $, cytoscape, angular){

    $.ajaxSettings.async = false;

    var originalData;
    var allVertex = [];
    var vm = null;
    var scope = null;

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
            originalData = this.getModel();
        },
        getModel: function(){
            $.getJSON('./json/data4.json', function(res){
                originalData = res;
            })
            this.drawChart();
            allVertex = formatVertex(originalData.vertices);
            runAngular()
            return originalData;
        },
        drawChart : function(){

            var chart = new flowChart('chart', originalData, chartEventCallback, {
                userZoomingEnabled: false
            });
            console.log(chart.cy.width())
        }
    };

    app.init();


    
    
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



    function runAngular(){

        angular.module('chartApp', []);

        angular.module('chartApp').controller('formController', formController);


        function formController ($scope){
            scope = $scope;
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
            vm.userTasks = [];
            vm.autoTasks = [];
            vm.partGTasks = [];
            vm.groups = [];
            vm.v2g = [];
            vm.g2v = [];
            vm.vertices = allVertex;


            vm.addNewLine = function (){
                console.log(vm.newVertex)
            }


        }

    }


})(window, jQuery, cytoscape, angular);




