/**
 * Created by JinWYP on 23/12/2016.
 */


(function(window, $, cytoscape){

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
            selector: 'node.task',
            style: {
                // 'shape': 'roundrectangle',
                'shape': 'rhomboid',
                'width': function(ele){
                    return 150;
                    // return Math.max(150, ele.data().id.length*16);
                },
                'height': 80
            }
        },

        {
            selector: 'node.task.autoTasks',
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
            selector: 'node.task.userTasks',
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
            selector: 'node.task.partUTasks',
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
            selector: 'node.task:selected',
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
            selector: 'edge.singleChildEdge',
            style: {
                'visibility': 'hidden'
            }
        },

        {
            // 只有一个子任务
            selector: 'edge.taskedge.singleTaskEdge',
            style: {
                'visibility': 'visible'
            }
        },
    ];
    
    var flowChart = function (domId, data, actionCB, config){
        this.config = config || {};
        this.domId = domId || '';
        this.data = data || [];
        this.cy = this.generateFc(domId, data, actionCB);
        return this;
    };

    flowChart.prototype.getStyle =  function(){
        return styleArr;
    };


    flowChart.prototype.getModel = function(originalData){
        var modelData;

        var taskEdge = function(edge, isFinished, isProcessing, name){
            // edgeItem: { 
            //             data: {'source': curEdge.begin, 'target': t, name:name, sourceType:'node', endType:'task', taskType:type, original: originalData}, 
            //             classes: classes 
            //         }
            var curEdge = edge;
            var resultEdges = [];
            var children = [];
            var cls = [];
            

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
                        children.push({'data': { id : t,
                                                taskType : type,
                                                description : t.description,
                                                program : '',
                                                points : '',
                                                parent: name,
                                                original : originalData},
                                        'classes': classes+' task '+type})
                        classes && cls.push(classes);
                        resultEdges.push({ data: {'source': curEdge.begin, 'target': t, name:name, sourceType:'node', endType:'task', taskType:type, original: originalData}, classes: 'taskedge'  },
                            { data: {'source': t, 'target': curEdge.end, name:name, sourceType:'task', endType:'node', taskType:type, original: originalData}, classes: 'taskedge'  });

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
                            children.push({'data': { id : subt,
                                                taskType : type,
                                                description : subt.description,
                                                program : '',
                                                points : '',
                                                parent: name,
                                                original : originalData},
                                        'classes': classes +' task '+ type })
                            classes && cls.push(classes);
                            resultEdges.push({ data: {'source': curEdge.begin, 'target': subt, name:name, gidKey:id, sourceType:'node', endType:'task', taskType:type, original: originalData}, classes: 'taskedge' },
                                { data: {'source': subt, 'target': curEdge.end, name:name, gidKey:id, sourceType:'task', endType:'node', taskType:type, original: originalData}, classes: 'taskedge' });
                        })
                    })
                }
            });

            (children.length==1) && resultEdges.forEach(function(e, ei){
                e.classes += ' singleTaskEdge';
            })

            var ecls = (cls.length>0) ? ((cls.indexOf('isProcessing')>=0) ? 'isProcessing' : ((cls.length==children.length) ? 'isFinished' : '')) : '';
            (children.length==1) && (ecls+=' singleChildEdge');
            resultEdges.push({ data: {'source': curEdge.begin, 'target': name, 'name':name + '-source', 'sourceType':'node', 'endType':'edgeGroup', 'children': children, 'original': originalData}, 'classes': ecls},
                { data: {'source': name, 'target': curEdge.end, 'name':name+'-target', 'sourceType':'edgeGroup', 'endType':'node', 'children': children, 'original': originalData}, 'classes': ecls})
        
            return resultEdges;
        };


        var formatModel = function(){
            var node_keys = [];
            var nodes = [];
            var edges = [];
            var historyEdges = [];
            var nodeGParents = [];
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

            edges.forEach(function(edgeItem, ei){
                if(edgeItem.classes.indexOf('taskedge')<0){
                    var nArr = [edgeItem.data.source, edgeItem.data.target];

                    nArr.forEach(function(n, ni){
                        var className = '';
                        if(edgeItem.classes.indexOf('isFinished')>=0){
                            className = 'isFinished';
                        }else if(edgeItem.classes.indexOf('isProcessing')>=0){
                            className = 'isProcessing';
                            (ni==0)&&(edgeItem.data.sourceType=='node')&&(className='isFinished');
                        }

                        (ni==0)&&(className+=' '+edgeItem.data.sourceType);
                        (ni==1)&&(className+=' '+edgeItem.data.endType);
                        (ni==1)&&(edgeItem.data.children)&&(edgeItem.data.children.length==1)&&(className+=' singleChild');

                        className += ' ' + edgeItem.data.taskType;

                        if(node_keys.indexOf(n)<0){
                            node_keys.push(n);
                            var tempNode = {
                                data: {
                                    id : n,
                                    taskType : edgeItem.data.taskType,
                                    description : '',
                                    program : '',
                                    points : '',
                                    original : originalData
                                },
                                classes: className
                            };

                            if (((ni==0) && (edgeItem.data.sourceType == 'node')) || ((ni==1) && (edgeItem.data.endType == 'node'))){
                                tempNode.data.description = originalData.vertices[n]
                            }else {
                                var thisTask = originalData.userTasks[n] || originalData.autoTasks[n] || {};
                                tempNode.data.description = thisTask.description;
                                tempNode.data.points = thisTask.points;
                            }
                            nodes.push(tempNode)
                        }else{
                            var classes = nodes[node_keys.indexOf(n)].classes;
                            if(classes.indexOf('isProcessing')<0){
                                (className.indexOf('isFinished')>=0) && (!$.trim(classes) || classes.indexOf('isFinished')<0) && (classes+=' isFinished');
                                (className.indexOf('isProcessing')>=0) && (!$.trim(classes) || classes.indexOf('isProcessing')<0) && ((classes = classes.replace('isFinished', '')) && (classes+=' isProcessing'));
                                nodes[node_keys.indexOf(n)].classes = classes;
                            }
                        }
                    })

                    edgeItem.data.children && edgeItem.data.children.forEach(function(e, ei){
                        nodes.push(e);
                    });
                }
            })

            console.log({nodes: nodes, edges: edges})
            return {nodes: nodes, edges: edges};
        }

        modelData = formatModel();

        return modelData;
    };


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