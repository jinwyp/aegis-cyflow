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
                    'shape': 'roundrectangle',
                    'width': 150,
                    'height': 40,
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
        $.getJSON('./json/data3.json', function(res){
            originalData = res;
            var node_keys = [];
            var nodes = [];
            var edges = [];
            var historyEdges = [];
            res.state.histories.forEach(function(v,i){
                v.edge && historyEdges.push(v.edge);
            })
            for(var i in res.edges){
                var val = [res.edges[i].begin, res.edges[i].end];
                var isFinished = (historyEdges.indexOf(i)>=0) ? true : false;
                var isProcessing = res.state.edge ? (i==res.state.edge) : false;
                edges.push({ data: {'source': val[0], 'target':val[1], 'name':i, 'original': res.edges[i]}, classes: (isFinished? 'isFinished' : '') +' '+  (isProcessing? 'isProcessing':'')});
                console.log(res.edges[i])
                val.forEach(function(v, j){
                    var ispro = (j==0)? false : isProcessing;
                    if(node_keys.indexOf(v)<0){
                        node_keys.push(v);
                        nodes.push({data: {'id': v}, classes: (isFinished? 'isFinished' : '') +' '+  (ispro? 'isProcessing':'')});
                    }
                    isFinished && (nodes[node_keys.indexOf(v)].classes.indexOf('isFinished')<0) && (nodes[node_keys.indexOf(v)].classes += ' isFinished');
                    ispro && (nodes[node_keys.indexOf(v)].classes.indexOf('isProcessing')<0) && (nodes[node_keys.indexOf(v)].classes += ' isProcessing');
                });
            };
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
            autounselectify: true,
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
        
        function drawfn(){
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
        cy.edges().qtip({
            content: function(){
                return edgeTip.apply(this);
            },
            position: {
                my: 'bottom left',
                at: 'bottom left'
            },
            style: {
                classes: 'qtip-bootstrap',
                tip: {
                    width: 8,
                    height: 8
                }
            }
        })
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
    window.FC = FC;
})(window, jQuery, cytoscape)




		