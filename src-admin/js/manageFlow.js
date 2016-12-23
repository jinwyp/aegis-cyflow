/**
 * Created by JinWYP on 23/12/2016.
 */


(function(window, $, cytoscape, angular){

    $.ajaxSettings.async = false;

    var originalData;

    var chartEventCallback= function(cy){

        console.log(cy.width())
        cy.nodes('.task').qtip({
            content: function(){
                var data = this.data();
                return data.original.points[data.id];
            },
            // show: {
            //     event: 'mouseover'
            // },
            // hide: {
            //     event: 'mouseout'
            // },
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

            var data = this.data();
            var points = [];
            data.original[(data.taskType=='autoTasks')?'autoTasks':'userTasks'][data.id].points.forEach(function(p, pi){
                var val;
                if(data.original.state.points.hasOwnProperty(p)){
                    if(data.original.state.points[p].memo){
                        var memo = data.original.state.points[p].memo;
                        (memo.indexOf('img:')==0) && (val={'url': memo.substr(4), 'text': '查看图片'});
                        (memo.indexOf('pdf:')==0) && (val={'url': memo.substr(4), 'text': '查看PDF文件'});
                    }

                    !val && (val = data.original.state.points[p].value);
                }else{
                    val = '未采集';
                }
                points.push({'key':p, 'value':val});
            })

            var data_ptDetail = {'points': points, 'task': {'type': data.taskType}};
            var ptDetail = ejs.compile($('#tmpl_ptDetail').html())(data_ptDetail);
            $('#ptDetail>div').html(ptDetail);

            $('.hastip').qtip({
                content: function(){
                    return '<span class="pointer"></span><div class="tip-pointtext-contentbg"></div><div class="tip-pointtextcontent"><div class="content">' + $(this).attr('data') + '</div></div>';
                },
                position: {
                    my: 'bottom right',
                    at: 'top right'
                },
                show: {
                    event: 'click'
                },
                hide: {
                    event: 'unfocus'
                },
                style: {
                    classes: 'qtip-bootstrap tip-pointtext',
                    tip: {
                        width: 16,
                        height: 8
                    }
                }
            })
        })

        cy.nodes('.node').qtip({
            content: function(){
                var data = this.data();
                var id = data.id;
                var vertices = data.original.vertices;
                return vertices[id];
            },
            // show: {
            //     event: 'mouseover'
            // },
            // hide: {
            //     event: 'mouseout'
            // },
            position: {
                my: 'top center',
                at: 'bottom center'
            },
            style: {
                classes: 'qtip-bootstrap',
                tip: {
                    width: 16,
                    height: 8
                }
            }
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

            var chart = new flowChart('chart', originalData, chartEventCallback);
            console.log(chart.cy.width())
        }
    };

    app.init();

})(window, jQuery, cytoscape, angular);




