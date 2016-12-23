/**
 * Created by JinWYP on 23/12/2016.
 */


(function(window, $, cytoscape, angular){

    $.ajaxSettings.async = false;

    var originalData;

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

        cy.nodes('.task').on('click', function(e){
            console.log(1111)
            var data = this.data();
            var points = [];
            data.original[(data.taskType=='autoTasks')?'autoTasks':'userTasks'][data.id].points.forEach(function(p, pi){
                var val;
                points.push({'key':p, 'value':val});
            })
        })

        cy.nodes('.task').on('click', function(e){
            console.log(1111)
            var data = this.data();
            var points = [];
            data.original[(data.taskType=='autoTasks')?'autoTasks':'userTasks'][data.id].points.forEach(function(p, pi){
                var val;
                points.push({'key':p, 'value':val});
            })
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






    angular.module('chartApp', []);

    angular.module('chartApp').controller('formController', formController);


    function formController (){
        var vm = this;

        vm.currentVertex = {
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
        vm.vertices = [];

        vm.phones = [
            {
                name: 'Nexus S',
                snippet: 'Fast just got faster with Nexus S.'
            }, {
                name: 'Motorola XOOM™ with Wi-Fi',
                snippet: 'The Next, Next Generation tablet.'
            }, {
                name: 'MOTOROLA XOOM™',
                snippet: 'The Next, Next Generation tablet.'
            }
        ];
    }

})(window, jQuery, cytoscape, angular);




