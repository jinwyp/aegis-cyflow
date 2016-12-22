(function(window, $, cytoscape, ejs){

    $.ajaxSettings.async = false; 

    var originalData;

    var FC = function(id){
        this.id = id;
        this.cy = this.generateFc();
        return this;
    }
    
    FC.prototype.getStyle =  function(){
        var styleArr = [
            {
                selector: 'node',
                style: {
                    'shape': 'ellipse',
                    'width': 100,
                    'height': 100,
                    'content': 'data(id)',
                    'text-valign': 'center',
                    'text-halign': 'center',
                    'background-color': 'gray',
                    'color': '#fff',
                    'font-size': '24px'
                }
            },

            {
                selector: 'node.isProcessing',
                style: {
                    'background-color': 'orange'
                }
            },

            {
                selector: 'node.isFinished',
                style: {
                    'background-color': 'green'
                }
            },

            {
                selector: 'node.task',
                style: {
                    'shape': 'roundrectangle',
                    'width': 150,
                    'height': 80
                }
            },

            {
                selector: 'node.task.autoTasks',
                style: {
                    'shape': 'star',
                    'width': 110
                }
            },

            {
                selector: 'node.task:selected',
                style: {
                    'border-width': 3,
                    'border-color': '#e86e81'
                }
            },

            {
                selector: 'edge',
                style: {
                    'width': 4,
                    'target-arrow-shape': 'triangle',
                    'line-color': 'gray',
                    'target-arrow-color': 'gray',
                    'curve-style': 'bezier',
                    // 'control-point-distances': '-30% 30%',
                    // 'control-point-weights': '0 1'
                }
            },

            {
                selector: 'edge.toRight',
                style: {
                    'curve-style': 'unbundled-bezier',
                    'control-point-distances': '30% -30%',
                    'control-point-weights': '0 1'
                }
            },

            {
                selector: 'edge.toLeft',
                style: {
                    'curve-style': 'unbundled-bezier',
                    'control-point-distances': '-30% 30%',
                    'control-point-weights': '0 1'
                }
            },

            {
                selector: 'edge.isProcessing',
                style: {
                    'line-color': 'orange',
                    'target-arrow-color': 'orange'
                }
            },

            {
                selector: 'edge.isFinished',
                style: {
                    'line-color': 'green',
                    'target-arrow-color': 'green'
                }
            }
        ];
        return styleArr;
    };
    FC.prototype.getModel = function(){
        var modelData;
        var self = this;

        if(!originalData){
            $.getJSON('./json/data4.json', function(res){
                originalData = res;
            })
        }

        var taskEdge = function(edge, isFinished, isProcessing, name){
            var curEdge = edge;
            var edges = [];
            ['autoTasks', 'userTasks', 'partUTasks', 'partGTasks'].forEach(function(type, ti){
                var tasks = edge[type];
                if((tasks.length>0) && (type=='autoTasks' || type == 'userTasks')){
                    tasks.forEach(function(t, ti){
                        var classes = '';  
                        if(isFinished){
                            classes = 'isFinished';
                        }else if(isProcessing){
                            var complete = false;
                            originalData[type][t].points.forEach(function(p, pi){
                                originalData.state.points.hasOwnProperty(p) && (complete=true);
                            })
                            !complete ? (classes = 'isProcessing') : (classes = 'isFinished');
                        }
                        edges.push({ data: {'source': curEdge.begin, 'target': t, name:name, sourceType:'node', endType:'task', taskType:type, original: originalData}, classes: classes },
                                    { data: {'source': t, 'target': curEdge.end, name:name, sourceType:'task', endType:'node', taskType:type, original: originalData}, classes: classes });
                    })
                }

                if((tasks.length>0) && (type=='partUTasks' || type == 'partGTasks')){
                    tasks.forEach(function(t, ti){
                        var id = t.guidKey || t.ggidKey;
                        (t.tasks.length>0) && t.tasks.forEach(function(subt, si){
                            var classes = '';  
                            if(isFinished){
                                classes = 'isFinished';
                            }else if(isProcessing){
                                var complete = false;
                                originalData['userTasks'][subt].points.forEach(function(p, pi){
                                    originalData.state.points.hasOwnProperty(p) && (complete=true);
                                })
                                !complete ? (classes = 'isProcessing') : (classes = 'isFinished');
                            }
                            edges.push({ data: {'source': curEdge.begin, 'target': subt, name:name, gidKey:id, sourceType:'node', endType:'task', taskType:type, original: originalData}, classes: classes },
                                        { data: {'source': subt, 'target': curEdge.end, name:name, gidKey:id, sourceType:'task', endType:'node', taskType:type, original: originalData}, classes: classes });
                        })
                    })
                }
            });
            if(edges.length==0){
                edges.push({ data: {'source': curEdge.begin, 'target':curEdge.end, 'name':name, sourceType:'node', endType:'node', taskType:'edge', 'original': curEdge}, classes: (isFinished? 'isFinished' : '') +' '+  (isProcessing? 'isProcessing':'')});
            }
            return edges;
        }
            
        var formatModel = function(){
            var node_keys = [];
            var nodes = [];
            var edges = [];
            var historyEdges = [];
            historyEdges = originalData.state.histories;

            for(var i in originalData.edges){
                if(['success', 'fail', 'start'].indexOf(i)>=0){
                    continue;
                }
                var curEdge = originalData.edges[i];
                var isFinished = (historyEdges.indexOf(i)>=0) ? true : false;
                var isProcessing = (originalData.state.edges) ? (originalData.state.edges.hasOwnProperty(i)) : false;
    
                // task edges
                (function(curEdge, isFinished, isProcessing, i){
                    edges = edges.concat(taskEdge(curEdge, isFinished, isProcessing, i));
                })(curEdge, isFinished, isProcessing, i)
            };

            edges.forEach(function(e, ei){
                var nArr = [e.data.source, e.data.target];

                nArr.forEach(function(n, ni){
                    var c = '';
                    if(e.classes.indexOf('isFinished')>=0){
                        c = 'isFinished';
                    }else if(e.classes.indexOf('isProcessing')>=0){
                        c = 'isProcessing';
                        (ni==0)&&(e.data.sourceType=='node')&&(c='isFinished');
                    }
                    
                    (ni==0)&&(c+=' '+e.data.sourceType);
                    (ni==1)&&(c+=' '+e.data.endType);
                    c += ' ' + e.data.taskType;

                    if(node_keys.indexOf(n)<0){
                        node_keys.push(n);
                        nodes.push({data: {id: n, taskType:e.data.taskType, original: originalData}, classes: c})
                    }else{
                        var classes = nodes[node_keys.indexOf(n)].classes;
                        if(classes.indexOf('isProcessing')<0){
                            (c.indexOf('isFinished')>=0) && (!$.trim(classes) || classes.indexOf('isFinished')<0) && (classes+=' isFinished');
                            (c.indexOf('isProcessing')>=0) && (!$.trim(classes) || classes.indexOf('isProcessing')<0) && ((classes = classes.replace('isFinished', '')) && (classes+=' isProcessing'));
                            nodes[node_keys.indexOf(n)].classes = classes;
                        }
                    }
                })
            })
            
            return {nodes: nodes, edges: edges};
        }

        modelData = formatModel();

        return modelData;
    };
    FC.prototype.generateFc = function(){
        var self = this;
        var styleArr = self.getStyle();
        var modelData = self.getModel();
        var cy = cytoscape({
            container: document.getElementById(self.id),
            boxSelectionEnabled: false,
            autounselectify: false,
            userZoomingEnabled: true,
            userPanningEnabled: true,
            autoungrabify: false,
            minZoom: 0.1,
            layout: {
                name: 'dagre'
            },
            style: styleArr,
            elements: modelData
        });

        geneCallback(cy);

        cy.style(styleArr);

        return cy;
        
    }
    FC.prototype.drawProcessing = function(){
        var self = this;
        var cy = this.cy,
            canvas = $(cy._private.container).find('canvas')[2],
            ctx = canvas.getContext("2d");
        var offset = 0;

        var count = 0;

        function drawfn(){
            count++;
            offset+=5;
            if (offset > 50) {
                offset = 0;
            }
            
            ctx.clearRect(0,0, canvas.width, canvas.height);
            var pro_edges = cy.$('edge.isProcessing');

            pro_edges.forEach(function(v, i){
                var ePath = v.animatePath.path;
                var eType = v.animatePath.type;

                ctx.moveTo(ePath[0], ePath[1]);
                ctx.lineWidth = 2;
                ctx.strokeStyle = '#fff';
                ctx.fillStyle = '#fff';
                ctx.setLineDash([10,5]);
                ctx.lineDashOffset = -offset;

                switch( eType ){
                    case 'bezier':
                    case 'self':
                    case 'compound':
                    case 'multibezier':
                        for( var pi = 2; pi + 3 < ePath.length; pi += 4 ){
                            ctx.quadraticCurveTo( ePath[ pi ], ePath[ pi + 1], ePath[ pi + 2], ePath[ pi + 3] );
                        }
                        break;

                    case 'straight':
                    case 'segments':
                    case 'haystack':
                        for( var pi = 2; pi + 1 < ePath.length; pi += 2 ){
                            ctx.lineTo( ePath[ pi ], ePath[ pi + 1] );
                        }
                        break;
                }

                ctx.stroke();
            })
            
            setTimeout(drawfn, 150);
        }
        
        drawfn();
    
    }    

    var geneCallback= function(cy){
        // cy.nodes('.task').qtip({
        //     content: function(){
        //         return taskTip.apply(this);
        //     },
        //     position: {
        //         my: 'top center',
        //         at: 'bottom center'
        //     },
        //     style: {
        //         classes: 'qtip-bootstrap',
        //         tip: {
        //             width: 16,
        //             height: 8
        //         }
        //     }
        // })

        cy.nodes('.task').on('click', function(e){
            var data = this.data();
            var points = [];
            data.original[(data.taskType=='autoTasks')?'autoTasks':'userTasks'][data.id].points.forEach(function(p, pi){
                var val = data.original.state.points.hasOwnProperty(p) ? data.original.state.points[p].value : '暂无结果';
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
        
    }

    window.FC = FC;



    var taskTip = function(){
        var self = this;
        var taskId = this.data().id;
        var taskType = (this.data().taskType == 'autoTasks') ? 'autoTasks' : 'userTasks';
        var originalPoints = this.data().original.points;
        var points = this.data().original[taskType][taskId].points;
        var phtml = ''; 
        (points.length>0) && points.forEach(function(p, pi){
            var status;
            if(!!self._private.classes.isFinished){
                status = '<i class="success">已采集；</i> 采集结果：<i class="result">' + self.data().original.state.points[p].value + '</i>';
            }
            if(!!self._private.classes.isProcessing){
                status = self.data().original.state.points.hasOwnProperty(p) ? ('<i class="success">已采集；</i>采集结果：<i class="result">' + self.data().original.state.points[p].value) + '</i>' : '<i class="ing">进行中</i>';
            }
            if(!self._private.classes.isProcessing && !self._private.classes.isFinished){
                status = '<i class="wait">未开始</i>';
            }
            
            phtml += '<li><span class="point">'+ originalPoints[p] +'：</span><span class="pointState">'+ status +'</span></li>'
        })
        return '<h5>'+ this.data().original[taskType][taskId].description +'</h5><ul class="edgeTip">'+ phtml +'</ul>';
    }
    var autotaskTmpl = function(model, type){
        var items = '';
        var autotask = '';
        originalTasks = originalData[type] || originalData.userTasks;
        model.forEach(function(v, i){
            var points = '';
            
            originalTasks.hasOwnProperty(v) && originalTasks[v].forEach(function(v, i){
                points += '<li>' +
                                '<span class="point">'+ 
                                    v + '：' + originalData.points[v] +
                                '</span>'+ 
                                '<span class="pointState">' + originalData.state.points[v].value + '</span>' +
                            '</li>'
            });
            points = '<ul class="taskItem">'+ points +'</ul>';
            items += '<li><span class="task">'+ v +'</span>'+points+'</li>'
        })
        autotask = '<ul class="taskList auto">'+ items +'</ul>';
        return autotask;
    }

    var parttaskTmpl = function(model){
        var items = '';
        var parttask = '';
        var autotask = '';

        model.forEach(function(v, i){
            (v.tasks.length>0) && (autotask = autotaskTmpl(v.tasks));
            items += '<li><span class="part">'+ (v.ggidKey ||  v.guidKey) +'</span>'+ autotask +'</li>'
        })

        parttask = '<ul class="taskList part">'+ items +'</ul>';
        return parttask;
    }

    var edgeTip = function(){
        var data = this.data().original;
        var autotask, usertask, partgtask, partutask;
        autotask = usertask = partgtask = partutask = '';

        (data.autoTasks.length>0) && (autotask = autotaskTmpl(data.autoTasks, 'autoTasks'));
        (data.userTasks.length>0) && (usertask = autotaskTmpl(data.userTasks));
        (data.partGTasks.length>0) && (partgtask = parttaskTmpl(data.partGTasks));
        (data.partUTasks.length>0) && (partutask = parttaskTmpl(data.partUTasks));

        $.trim(autotask) && (autotask = '<h5 class="taskTitle">自动任务</h5>' + autotask);
        $.trim(usertask) && (usertask = '<h5 class="taskTitle">用户任务</h5>' + usertask);
        $.trim(partgtask) && (partgtask = '<h5 class="taskTitle">参与方组任务</h5>' + partgtask);
        $.trim(partutask) && (partutask = '<h5 class="taskTitle">参与方任务</h5>' + partutask);

        var html = '<div class="edgeTip">' + 
                        ($.trim(autotask + usertask + partutask + partgtask) || '无需任何任务')
                    '</div>';
        return html;
    }

    var dateFormat = function(date, format){
        if(!/^[1-9]\d*$/.test(date)){
            return  date ;
        }
        var date = parseInt(date),
            currentTime =  new Date().getTime(),
            diffTime = currentTime - date;
        var minute = 60*1000,
            hour = 60*minute,
            day = 24*hour,
            format = format || 'YYYY年MM月DD日',
            alwaysDiff = alwaysDiff || false;
        var formatArr = ['YYYY','MM','DD','H','M','S'];
        var date = new Date(date),
            year = date.getFullYear(),
            month = date.getMonth()+1,
            month = (month>9) ? month : '0'+month,
            day = (date.getDate()>9) ? date.getDate() : '0'+ date.getDate(),
            hour = (date.getHours()>9) ? date.getHours() : '0'+ date.getHours(),
            minute = (date.getMinutes()>9) ? date.getMinutes() : '0'+date.getMinutes(),
            second = (date.getSeconds()>9) ? date.getSeconds() : '0'+date.getSeconds(),
            dateArr = [year,month,day,hour,':'+minute,':'+ second];
        for(var i=0; i < formatArr.length; i++){
            format = format.replace(formatArr[i],dateArr[i]);
        }
        return format;
    };

    var PAGE = function(){
        return {
            init: function(){
                originalData = this.getModel();
                this.fcRender();
                this.tmplRender();
            },
            getModel: function(){
                $.getJSON('./json/data4.json', function(res){
                    originalData = res;
                })
                return originalData;
            },
            tmplRender: function(){
                var data_fcDetail = {'fcDetail': {
                    'id': originalData.state.flowId,
                    'uid': originalData.state.guid,
                    'type': originalData.state.flowType,
                    'utype': '用户类型',
                    'status': '流程状态'
                }};
                var fcDetail = ejs.compile($('#tmpl_fcDetail').html())(data_fcDetail);
                $('#fcDetail').html(fcDetail);

                // var data_ptDetail = {'points': [], 'task': {'type': 'autoTasks'}};
                // var ptDetail = ejs.compile($('#tmpl_ptDetail').html())(data_ptDetail);
                // $('#ptDetail').html(ptDetail);

                var data_history = {'historyPoints': [
                    {'name': '属性', 'value': '文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描述文字描', 'user': '采集人', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '文字描述文字描述文字描述文字描文字描述文字描述文字描述文字描', 'comment': '备注备注'},
                    {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                    {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                    {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                    {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                    {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                    {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                    {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'},
                    {'name': '属性', 'value': 'value', 'user': 'user', 'timestamp': dateFormat('1482624000000', 'YYYY-MM-DD'), 'description': '描述', 'comment': '备注'}
                ]};
                var history = ejs.compile($('#tmpl_historyContainer').html())(data_history);
                $('#historyContainer').html(history);
            },
            fcRender: function(){
                var cy = new FC('cy');
                var count = 0;
                $('#cy canvas').css('visibility','hidden');
                window.cy = cy.cy;
                cy.cy.onRender(function(){
                    count ++;
                    if(count==2){
                        cy.cy.zoom(0).center();
                        setTimeout(function(){
                            $('#cy canvas').css('visibility','visible');
                        }, 100)
                        cy.cy.delay(100).animate({fit: {padding:20}}, {duration: 300});
                    }
                })
            }
        }
    }

    window.PAGE = PAGE;

    new PAGE().init();
})(window, jQuery, cytoscape, ejs)




		