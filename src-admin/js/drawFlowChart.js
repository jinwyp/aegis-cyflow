/**
 * Created by JinWYP on 23/12/2016.
 */



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





var flowChart = function (domId, data, actionCB){
    this.domId = domId;
    this.data = data;
    this.cy = this.generateFc(domId, data, actionCB);
    return this;
};

flowChart.prototype.getStyle =  function(){
    return styleArr;
};


flowChart.prototype.getModel = function(originalData){
    var modelData;

    var taskEdge = function(edge, isFinished, isProcessing, name){
        var curEdge = edge;
        var resultEdges = [];
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
                    resultEdges.push({ data: {'source': curEdge.begin, 'target': t, name:name, sourceType:'node', endType:'task', taskType:type, original: originalData}, classes: classes },
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
                        resultEdges.push({ data: {'source': curEdge.begin, 'target': subt, name:name, gidKey:id, sourceType:'node', endType:'task', taskType:type, original: originalData}, classes: classes },
                            { data: {'source': subt, 'target': curEdge.end, name:name, gidKey:id, sourceType:'task', endType:'node', taskType:type, original: originalData}, classes: classes });
                    })
                })
            }
        });
        if(resultEdges.length==0){
            resultEdges.push({ data: {'source': curEdge.begin, 'target':curEdge.end, 'name':name, sourceType:'node', endType:'node', taskType:'edge', 'original': curEdge}, classes: (isFinished? 'isFinished' : '') +' '+  (isProcessing? 'isProcessing':'')});
        }
        return resultEdges;
    };


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


flowChart.prototype.generateFc = function(domId, data, eventCB){
    var self = this;

    var cy = cytoscape({
        container: document.getElementById(domId),
        boxSelectionEnabled: false,
        autounselectify: false,
        userZoomingEnabled: true,
        userPanningEnabled: true,
        autoungrabify: false,
        minZoom: 0.1,
        layout: {
            name: 'dagre'
        },
        style: self.getStyle(),
        elements: self.getModel(data)
    });

    eventCB(cy);

    return cy;

};


window.flowChart = flowChart;