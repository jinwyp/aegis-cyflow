(function(window, $, cytoscape){

    $.ajaxSettings.async = false; 

    var originalData = {};

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
                            originalData[type][t].forEach(function(p, pi){
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
                                originalData['userTasks'][subt].forEach(function(p, pi){
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

        $.getJSON('./json/data4.json', function(res){
            originalData = res;
            var node_keys = [];
            var nodes = [];
            var edges = [];
            var historyEdges = [];
            historyEdges = res.state.histories;

            for(var i in res.edges){
                if(['success', 'fail', 'start'].indexOf(i)>=0){
                    continue;
                }
                var curEdge = res.edges[i];
                var isFinished = (historyEdges.indexOf(i)>=0) ? true : false;
                var isProcessing = (res.state.edges) ? (res.state.edges.hasOwnProperty(i)) : false;
    
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
                        (c.indexOf('isFinished')>=0) && (!$.trim(classes) || classes.indexOf('isFinished')<0) && (classes+=' isFinished');
                        (c.indexOf('isProcessing')>=0) && (!$.trim(classes) || classes.indexOf('isProcessing')<0) && (classes+=' isProcessing');
                        nodes[node_keys.indexOf(n)].classes = classes;
                    }
                })
            })
            modelData = {nodes: nodes, edges: edges};
        })
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
        cy.nodes('.task').qtip({
            content: function(){
                return taskTip.apply(this);
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

        cy.nodes('.node').qtip({
            content: function(){
                var data = this._private.data;
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
        var taskId = this._private.data.id;
        var taskType = (this._private.data.taskType == 'autoTasks') ? 'autoTasks' : 'userTasks';
        var originalPoints = this._private.data.original.points;
        var points = this._private.data.original[taskType][taskId];
        var phtml = ''; 
        (points.length>0) && points.forEach(function(p, pi){
            var status;
            if(!!self._private.classes.isFinished){
                status = '<i class="success">已采集；</i> 采集结果：<i class="result">' + self._private.data.original.state.points[p].value + '</i>';
            }
            if(!!self._private.classes.isProcessing){
                status = self._private.data.original.state.points.hasOwnProperty(p) ? ('<i class="success">已采集；</i>采集结果：<i class="result">' + self._private.data.original.state.points[p].value) + '</i>' : '进行中';
            }
            if(!self._private.classes.isProcessing && !self._private.classes.isFinished){
                status = '未开始';
            }
            
            phtml += '<li><span class="point">'+ originalPoints[p] +'：</span><span class="pointState">'+ status +'</span></li>'
        })
        return '<ul class="edgeTip">'+ phtml +'</ul>';
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
        var data = this._private.data.original;
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

})(window, jQuery, cytoscape)




		