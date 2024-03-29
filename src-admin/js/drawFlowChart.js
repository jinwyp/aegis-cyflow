/**
 * Created by JinWYP on 23/12/2016.
 */


(function(window, $, cytoscape){

    var flowChart = function (domId, data, actionCB, config){
        this.config = config || {};
        this.domId = domId || '';
        this.data = data || [];
        this.cy = this.generateFc(domId, data, actionCB);
        return this;
    };

    flowChart.prototype.getStyle =  function(){
        var styleArr = [
            {
                selector: 'node',
                style: {
                    'shape': 'ellipse',
                    // 'shape': 'diamond',
                    'width': function(ele){
                        return 100;
                        // return Math.max(100, ele.data().id.length*16);
                    },
                    'height': 100,
                    'content': 'data(id)',
                    'text-valign': 'center',
                    'text-halign': 'center',
                    'background-color': 'gray',
                    'color': '#fff',
                    'font-size': '24px',
                    'text-outline-width': 8,
                    'text-outline-color': 'gray'
                }
            },

            {
                selector: 'node.isProcessing',
                style: {
                    'background-color': 'orange',
                    'text-outline-color': 'orange'
                }
            },

            {
                selector: 'node.isFinished',
                style: {
                    'background-color': 'green',
                    'text-outline-color': 'green'
                }
            },

            {
                selector: 'node.task-node',
                style: {
                    // 'shape': 'roundrectangle',
                    'shape': 'triangle',
                    'width': function(ele){
                        return 150;
                        // return Math.max(150, ele.data().id.length*16);
                    },
                    'height': 80
                }
            },

            {
                selector: 'node.task-node.autoTasks',
                style: {
                    'shape': 'star',
                    'width': function(ele){
                        return 110;
                        // return Math.max(110, ele.data().id.length*20);
                    },
                    'height': function(ele){
                        return 94;
                        // var h = (110 < ele.data().id.length*20) ? (ele.data().id.length*16) : 94;
                        // return h
                    }

                }
            },

            {
                selector: 'node.task-node.userTasks',
                style: {
                    'shape': 'hexagon',
                    'width': function(ele){
                        return 110;
                        // return Math.max(110, ele.data().id.length*20);
                    },
                    'height': function(ele){
                        return 94;
                        // var h = (110 < ele.data().id.length*20) ? (ele.data().id.length*16) : 94;
                        // return h
                    }

                }
            },

            {
                selector: 'node.task-node.partUTasks',
                style: {
                    // 'shape': 'pentagon',
                    'shape': 'polygon',
                    'width': function(ele){
                        return 110;
                        // return Math.max(110, ele.data().id.length*20);
                    },
                    'height': function(ele){
                        return 94;
                        // var h = (110 < ele.data().id.length*20) ? (ele.data().id.length*16) : 94;
                        // return h
                    }

                }
            },

            {
                selector: 'node.task-node:selected',
                style: {
                    'border-width': 3,
                    'border-color': '#e86e81'
                }
            },
            {
                selector: '$node > node',
                style: {
                    'padding-top': '10px',
                    'padding-left': '10px',
                    'padding-bottom': '10px',
                    'padding-right': '10px',
                    'text-valign': 'top',
                    'text-halign': 'center',
                    'color': '#333',
                    'background-color': '#bbb',
                    'text-outline-width': 0
                }
            }, 

            {
                selector: 'node.singleChild',
                style: {
                    'background-color': '#d8dee4',
                    'border-width': 0 
                }
            },

            // {
            //     selector: 'node.edgeGroup',
            //     style: {
            //         'shape': 'rectangle',
            //         'background-color': '#bbb',
            //     }
            // },

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
            },

            {
                // 任务edge不显示，显示为parent edge
                selector: 'edge.taskedge',
                style: {
                    'visibility': 'hidden'
                }
            },
            
            {
                // 只有一个子任务的edge parent
                selector: 'edge.singleChild',
                style: {
                    'visibility': 'hidden'
                }
            },

            {
                // 只有一个子任务
                selector: 'edge.taskedge.singleChild',
                style: {
                    'visibility': 'visible'
                }
            },
        ];
        return styleArr;
    };

    flowChart.prototype.getModel = function(originalData){
        var nodes = [],
            edges = [],
            judgeNodes = [],
            judgeNodesIndex = [];

        var originalEdges = originalData.edges,
            historyEdges = originalData.state.histories,
            ingEdges = originalData.state.edges;

        for(var e in originalEdges){
            if(['success', 'fail', 'start'].indexOf(e)>=0){
                continue;
            }
            var classes = '';
            var curEdge = originalEdges[e];

            var taskEdges = [],
                judgeToParentEdges = [],
                taskNodes = [],
                parentNodes = [];
            
            var taskNodesIndex = [],
                parentNodesIndex = [];
            
            (historyEdges.indexOf(e)>=0) && (classes = 'isFinished');
            ingEdges.hasOwnProperty(e) && (classes = 'isProcessing');

            // taskNodes+taskedges
            ['autoTasks', 'userTasks', 'partUTasks', 'partGTasks'].forEach(function(type, ti){
                curEdge[type].length && curEdge[type].forEach(function(item, itemi){
                    var tasks = [item];
                    if((type == 'partUTasks') || (type == 'partGTasks')){
                        tasks = item.tasks;
                    }

                    tasks.forEach(function(task, taski){
                        var tcls = classes;
                        var taskItem = originalData[(type=='autoTasks')?'autoTasks':'userTasks'][task];
                        (classes == 'isProcessing') && taskItem.points.forEach(function(t, ti){
                            if(originalData.state.points.hasOwnProperty(t)){
                                tcls = 'isFinished';
                            }
                        })
                        taskNodes.push({'data': { id : task,
                                                taskType : type,
                                                description : taskItem.description || '暂无描述',
                                                program : '',
                                                points : '',
                                                parent: e,
                                                original : originalData},
                                        'classes': 'task-node '+ type + ' ' + tcls})
                        // taskedge
                        taskEdges.push({ 'data': { 'source': curEdge.begin, 
                                                    'target': task, 
                                                    'name':task+'-begin', 
                                                    'sourceType':'judge-node', 
                                                    'endType':'task-node', 
                                                    'taskType':type, 
                                                    'original': originalData}, 
                                            'classes': tcls + ' taskedge'  },
                                        { 'data': { 'source': task, 
                                                    'target': curEdge.end, 
                                                    'name':task+'-end', 
                                                    'sourceType':'task-node', 
                                                    'endType':'judge-node', 
                                                    'taskType':type, 
                                                    'original': originalData}, 
                                            'classes': tcls + ' taskedge'  })
                    });
                })
            });

            (taskNodes.length==1) && taskEdges.forEach(function(taske, taskei){
                taske.classes += ' singleChild';
            });

            // parentNodes
            parentNodes.push({'data': { id : e,
                                        taskType : 'parent-node',
                                        description : e || '暂无描述',
                                        program : '',
                                        points : '',
                                        parent: '',
                                        original : originalData},
                                'classes': 'parent-node ' + classes + ((taskNodes.length==1)?' singleChild':'')});
            

            // judgeToParentEdges
            judgeToParentEdges.push({ 'data': { 'source': curEdge.begin, 
                                                    'target': e, 
                                                    'name':e+'-begin', 
                                                    'sourceType':'judge-node', 
                                                    'endType':'parent-node', 
                                                    'taskType':'', 
                                                    'original': originalData}, 
                                            'classes': classes + ' judge-to-parent-edge' + ((taskNodes.length==1)?' singleChild':'')  },
                                        { 'data': { 'source': e, 
                                                    'target': curEdge.end, 
                                                    'name':e+'-end', 
                                                    'sourceType':'parent-node', 
                                                    'endType':'judge-node', 
                                                    'taskType':'', 
                                                    'original': originalData}, 
                                            'classes': classes + ' judge-to-parent-edge' + ((taskNodes.length==1)?' singleChild':'')  });

            // judgeNodes
            [curEdge.begin, curEdge.end].forEach(function(n, ni){
                var out = (ni==0)?{name: e, classes: classes}:'';
                var ins = (ni==1)?{name: e, classes: classes}:'';
                if(judgeNodesIndex.indexOf(n)<0){
                    judgeNodesIndex.push(n);
                    judgeNodes.push({'data': { id : n,
                                                taskType : 'judge-node',
                                                description : originalData.vertices[n] || '暂无描述',
                                                program : '',
                                                points : '',
                                                parent: '',
                                                ins: [],
                                                out: [],
                                                original : originalData},
                                        'classes': 'judge-node'})
                }
                ins && judgeNodes[judgeNodesIndex.indexOf(n)].data.ins.push(ins);
                out && judgeNodes[judgeNodesIndex.indexOf(n)].data.out.push(out)
            })
            
            nodes = nodes.concat(taskNodes, parentNodes);
            edges = edges.concat(taskEdges, judgeToParentEdges);
        }

        // judgeNodes status
        judgeNodes.forEach(function(jn, jni){
            if(jn.data.id=='V3'){
                console.log(1)
            }
            var c = 'isFinished';
            for(var i=0; i<jn.data.ins.length; i++){
                if(jn.data.ins[i].classes.indexOf('isProcessing')>=0){
                    c = 'isProcessing';
                    break;
                }
                if(!(/(isProcessing|isFinished)/g.test(jn.data.ins[i].classes))){
                    c = '';
                }
            }

            for(var j=0; j<jn.data.out.length; j++){
                if(/(isProcessing|isFinished)/g.test(jn.data.out[j].classes)){
                    c = 'isFinished';
                    break;
                }
            }

            jn.classes += ' ' + c;
        });

        nodes = nodes.concat(judgeNodes);

        console.log({nodes: nodes, edges: edges});
        return {nodes: nodes, edges: edges};

    }

    flowChart.prototype.generateFc = function(domId, data, eventCB){

        var self = this;
        var cfg = Object.assign({
            container: document.getElementById(domId),

            layout: {
                name: 'dagre'
            },
            style: self.getStyle(),
            elements: self.getModel(data),


            boxSelectionEnabled: false,
            autounselectify: false,
            userZoomingEnabled: true,
            userPanningEnabled: true,
            autoungrabify: false,

            minZoom: 0.3, //http://js.cytoscape.org/#core
            maxZoom: 1,

            textureOnViewport : false
            // pixelRatio : 1.0


        }, self.config);


        var cy = cytoscape(cfg);

        eventCB(cy);

        return cy;

    };


    window.flowChart = flowChart;


})(window, jQuery, cytoscape)