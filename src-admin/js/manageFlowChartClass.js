/**
 * Created by JinWYP on 23/12/2016.
 */


(function(window, $, cytoscape){

    var styleList = [
        {
            selector: 'node',
            style: {
                'shape': 'ellipse',
                'width': function(ele){
                    return Math.max(100, ele.data().id.length*16);
                },
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
                'width': function(ele){
                    return Math.max(150, ele.data().id.length*16);
                },
                'height': 80
            }
        },

        {
            selector: 'node.task.autoTasks',
            style: {
                'shape': 'star',
                'width': function(ele){
                    return Math.max(110, ele.data().id.length*20);
                },
                'height': function(ele){
                    var h = (110 < ele.data().id.length*20) ? (ele.data().id.length*16) : 94;
                    return h
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


    var formatterObjectToArray = function(source){

        var tempNodesId = [];
        var result = {
            nodes : [],
            edges : [],
            formattedSource : {
                edges : [],
                vertices : []
            }
        };

        for (var property in source.edges){

            var currentEdge = source.edges[property];
            var tempEdge = {};
            var tempNode = {};

            console.log(currentEdge)
            tempEdge = {
                classes : '',

                data : {
                    id : property,
                    source : currentEdge.begin,
                    target : currentEdge.end
                },

                sourceData : {
                    id : currentEdge.name,
                    source : currentEdge.begin,
                    target : currentEdge.end,
                    userTasks : currentEdge.userTasks,
                    autoTasks : currentEdge.autoTasks,
                    partGTasks : currentEdge.partGTasks,
                    partUTasks : currentEdge.partUTasks
                }
            };


            if (tempNodesId.indexOf(currentEdge.begin) === -1) {

                var tempNodeBegin = {
                    description : '',
                    program : ''
                };
                if (typeof source.vertices[currentEdge.begin] !== 'undefined' ){
                    var tempNodeBegin = source.vertices[currentEdge.begin];
                }

                tempNode = {
                    classes : '',

                    data : {
                        id : currentEdge.begin
                    },

                    sourceData : {
                        id : currentEdge.begin,
                        description : tempNodeBegin.description,
                        program : tempNodeBegin.program
                    }
                };

                tempNodesId.push(currentEdge.begin)

                result.nodes.push(tempNode)
                result.formattedSource.vertices.push(tempNode)

            }



            if (tempNodesId.indexOf(currentEdge.end) === -1) {

                var tempNodeTarget = {
                    description : '',
                    program : ''
                };

                if (typeof source.vertices[currentEdge.end] !== 'undefined' ){
                    tempNodeTarget = source.vertices[currentEdge.end];
                }

                tempNode = {
                    classes : '',

                    data : {
                        id : currentEdge.end
                    },

                    sourceData : {
                        id : currentEdge.end,
                        description : tempNodeTarget.description,
                        program : tempNodeTarget.program
                    }
                };
                tempNodesId.push(currentEdge.end)
                result.nodes.push(tempNode)
                result.formattedSource.vertices.push(tempNode)
            }


            result.edges.push(tempEdge)
            result.formattedSource.edges.push(tempEdge)


        }

        console.log(result)
        return result;

    };


    var flowChart2 = function (data, config){
        return this.init(data, config);
    };


    flowChart2.prototype.init =  function(sourceData, config){
        var self = this;

        var cfg = Object.assign({
            container: document.getElementById(config.domId),

            layout: {
                name: 'dagre'
            },
            style: styleList,
            elements: formatterObjectToArray(sourceData),

            boxSelectionEnabled: false,
            autounselectify: false,
            userZoomingEnabled: true,
            userPanningEnabled: true,
            autoungrabify: false,

            minZoom: 0.3, //http://js.cytoscape.org/#core
            maxZoom: 1,

            textureOnViewport : false
            // pixelRatio : 1.0


        }, config);


        var cy = cytoscape(cfg);

        cfg.eventCB(cy);

        return cy;
    };


    flowChart2.prototype.formatterObjectToArray = formatterObjectToArray;


    window.flowChart2 = flowChart2;


})(window, jQuery, cytoscape)