(function(window, $, cytoscape){
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
        // return new Promise(function(resolve, reject){
            // $.getJSON('./json/data.json', function(res){
                var res = {
                    "edges": {
                        "E3": {
                        "userTasks": [],
                        "partGTasks": {
                            "KPG1": [
                            "PG"
                            ]
                        },
                        "begin": "V2",
                        "partUTasks": {
                            "KPU1": [
                            "PU"
                            ]
                        },
                        "autoTasks": [],
                        "end": "V3"
                        },
                        "Start": {
                        "userTasks": [],
                        "partGTasks": {},
                        "begin": "V5",
                        "partUTasks": {},
                        "autoTasks": [],
                        "end": "V3"
                        },
                        "E2": {
                        "userTasks": [
                            "TKUP1",
                            "TKPG1"
                        ],
                        "partGTasks": {},
                        "begin": "V1",
                        "partUTasks": {},
                        "autoTasks": [],
                        "end": "V2"
                        },
                        "E1": {
                        "userTasks": [],
                        "partGTasks": {},
                        "begin": "V0",
                        "partUTasks": {},
                        "autoTasks": [
                            "A",
                            "B",
                            "C"
                        ],
                        "end": "V1"
                        },
                        "E5": {
                        "userTasks": [],
                        "partGTasks": {},
                        "begin": "V4",
                        "partUTasks": {},
                        "autoTasks": [
                            "DEF"
                        ],
                        "end": "V5"
                        },
                        "E4": {
                        "userTasks": [
                            "UA"
                        ],
                        "partGTasks": {},
                        "begin": "V3",
                        "partUTasks": {},
                        "autoTasks": [],
                        "end": "V4"
                        }
                    },
                    "state": {
                        "guid": "00-hary",
                        "flowType": "ying",
                        "decision": "FlowSuccess",
                        "points": {
                        "E": {
                            "used": true,
                            "timestamp": 1481878267515,
                            "id": "b563e2c4-cddd-46e6-9009-8a13193c406f",
                            "operator": "system",
                            "value": "50",
                            "memo": "memo"
                        },
                        "PU2": {
                            "used": false,
                            "timestamp": 1481878273504,
                            "id": "ae16a1fe-c325-4fa3-985b-16e474bcdc90",
                            "operator": "fund-wangqiId",
                            "value": "50",
                            "memo": "userdata"
                        },
                        "PU1": {
                            "used": false,
                            "timestamp": 1481878273504,
                            "id": "47c76192-b1b3-4099-8979-84df9d273dcf",
                            "operator": "fund-wangqiId",
                            "value": "50",
                            "memo": "userdata"
                        },
                        "F": {
                            "used": true,
                            "timestamp": 1481878267515,
                            "id": "b563e2c4-cddd-46e6-9009-8a13193c406f",
                            "operator": "system",
                            "value": "50",
                            "memo": "memo"
                        },
                        "A": {
                            "used": true,
                            "timestamp": 1481878267515,
                            "id": "b563e2c4-cddd-46e6-9009-8a13193c406f",
                            "operator": "system",
                            "value": "50",
                            "memo": "memo"
                        },
                        "PG1": {
                            "used": false,
                            "timestamp": 1481878278504,
                            "id": "bd7f40c2-cdff-40c9-936b-1ae56953f43f",
                            "operator": "fund-wangqiId",
                            "value": "50",
                            "memo": "userdata"
                        },
                        "UA2": {
                            "used": true,
                            "timestamp": 1481878298495,
                            "id": "73fed178-50e9-4a9a-91e6-0e04815510e3",
                            "operator": "00-hary",
                            "value": "50",
                            "memo": "userdata"
                        },
                        "B": {
                            "used": true,
                            "timestamp": 1481878267515,
                            "id": "b563e2c4-cddd-46e6-9009-8a13193c406f",
                            "operator": "system",
                            "value": "50",
                            "memo": "memo"
                        },
                        "PG2": {
                            "used": false,
                            "timestamp": 1481878278504,
                            "id": "0fcbddce-bf37-44d5-956e-69c6bbb4e602",
                            "operator": "fund-wangqiId",
                            "value": "50",
                            "memo": "userdata"
                        },
                        "C": {
                            "used": true,
                            "timestamp": 1481878267515,
                            "id": "b563e2c4-cddd-46e6-9009-8a13193c406f",
                            "operator": "system",
                            "value": "50",
                            "memo": "memo"
                        },
                        "UA1": {
                            "used": true,
                            "timestamp": 1481878298495,
                            "id": "bc00b306-3dd1-46e5-8d37-690237ab3c5d",
                            "operator": "00-hary",
                            "value": "50",
                            "memo": "userdata"
                        },
                        "KPU1": {
                            "used": true,
                            "timestamp": 1481878268507,
                            "id": "2088389a-65b2-495f-84ef-de2557493b38",
                            "operator": "00-hary",
                            "value": "fund-wangqiId",
                            "memo": "userdata"
                        },
                        "KPG1": {
                            "used": true,
                            "timestamp": 1481878268509,
                            "id": "04e70d57-ad39-4ec4-9a21-08b9011b56d7",
                            "operator": "00-hary",
                            "value": "fund-wqGroup",
                            "memo": "userdata"
                        },
                        "D": {
                            "used": true,
                            "timestamp": 1481878267515,
                            "id": "b563e2c4-cddd-46e6-9009-8a13193c406f",
                            "operator": "system",
                            "value": "50",
                            "memo": "memo"
                        }
                        },
                        "flowId": "ying-00-hary-1",
                        "histories": [
                        {
                            "end": "FlowSuccess"
                        },
                        {
                            "end": "V5",
                            "edge": {
                            "userTasks": [],
                            "partGTasks": {},
                            "name": "E5",
                            "partUTasks": {},
                            "autoTasks": [
                                "DEF"
                            ]
                            }
                        },
                        {
                            "end": "V4",
                            "edge": {
                            "userTasks": [
                                "UA"
                            ],
                            "partGTasks": {},
                            "name": "E4",
                            "partUTasks": {},
                            "autoTasks": []
                            }
                        },
                        {
                            "end": "V3",
                            "edge": {
                            "userTasks": [],
                            "partGTasks": {},
                            "name": "Start",
                            "partUTasks": {},
                            "autoTasks": []
                            }
                        },
                        {
                            "end": "V5",
                            "edge": {
                            "userTasks": [],
                            "partGTasks": {},
                            "name": "E5",
                            "partUTasks": {},
                            "autoTasks": [
                                "DEF"
                            ]
                            }
                        },
                        {
                            "end": "V4",
                            "edge": {
                            "userTasks": [
                                "UA"
                            ],
                            "partGTasks": {},
                            "name": "E4",
                            "partUTasks": {},
                            "autoTasks": []
                            }
                        },
                        {
                            "end": "V3",
                            "edge": {
                            "userTasks": [],
                            "partGTasks": {},
                            "name": "Start",
                            "partUTasks": {},
                            "autoTasks": []
                            }
                        },
                        {
                            "end": "V5",
                            "edge": {
                            "userTasks": [],
                            "partGTasks": {},
                            "name": "E5",
                            "partUTasks": {},
                            "autoTasks": [
                                "DEF"
                            ]
                            }
                        },
                        {
                            "end": "V4",
                            "edge": {
                            "userTasks": [
                                "UA"
                            ],
                            "partGTasks": {},
                            "name": "E4",
                            "partUTasks": {},
                            "autoTasks": []
                            }
                        },
                        {
                            "end": "V3",
                            "edge": {
                            "userTasks": [],
                            "partGTasks": {},
                            "name": "Start",
                            "partUTasks": {},
                            "autoTasks": []
                            }
                        },
                        {
                            "end": "V5",
                            "edge": {
                            "userTasks": [],
                            "partGTasks": {},
                            "name": "E5",
                            "partUTasks": {},
                            "autoTasks": [
                                "DEF"
                            ]
                            }
                        },
                        {
                            "end": "V4",
                            "edge": {
                            "userTasks": [
                                "UA"
                            ],
                            "partGTasks": {},
                            "name": "E4",
                            "partUTasks": {},
                            "autoTasks": []
                            }
                        },
                        {
                            "end": "V3",
                            "edge": {
                            "userTasks": [],
                            "partGTasks": {
                                "KPG1": [
                                "PG"
                                ]
                            },
                            "name": "E3",
                            "partUTasks": {
                                "KPU1": [
                                "PU"
                                ]
                            },
                            "autoTasks": []
                            }
                        },
                        {
                            "end": "V2",
                            "edge": {
                            "userTasks": [
                                "TKUP1",
                                "TKPG1"
                            ],
                            "partGTasks": {},
                            "name": "E2",
                            "partUTasks": {},
                            "autoTasks": []
                            }
                        },
                        {
                            "end": "V1",
                            "edge": {
                            "userTasks": [],
                            "partGTasks": {},
                            "name": "E1",
                            "partUTasks": {},
                            "autoTasks": [
                                "A",
                                "B",
                                "C"
                            ]
                            }
                        }
                        ]
                    },
                    "dataDescription": {
                        "E": "征信平分5",
                        "PU2": "partition user1 采集数据点2",
                        "PU1": "partition user1 采集数据点1",
                        "F": "征信平分6",
                        "A": "征信平分1",
                        "PG1": "partition group1 采集数据点1",
                        "UA2": "用户提交A2",
                        "B": "征信平分2",
                        "PG2": "partition group1 采集数据点2",
                        "C": "征信平分3",
                        "UA1": "用户提交A1",
                        "KPU1": "设置资金方 user1",
                        "KPG1": "设置融资方 group1",
                        "D": "征信平分4"
                    }
                    }


                var node_keys = [];
                var nodes = [];
                var edges = [];
                var historyEdges = [];
                res.state.histories.forEach(function(v,i){
                    v.edge && v.edge.name && historyEdges.push(v.edge.name);
                })
                for(var i in res.edges){
                    var val = [res.edges[i].begin, res.edges[i].end];
                    var isFinished = (historyEdges.indexOf(i)>=0) ? true : false;
                    var isProcessing = res.state.edge ? (i==res.state.edge) : false;
                    edges.push({ data: {'source': val[0], 'target':val[1], 'name':i}, 'isFinished': isFinished, 'isProcessing': isProcessing});
                    
                    val.forEach(function(v, j){
                        if(node_keys.indexOf(v)<0){
                            node_keys.push(v);
                            var ispro = (j==0)? false : isProcessing;
                            nodes.push({data: {'id': v} , 'isFinished': isFinished, 'isProcessing': ispro});
                        }
                        isFinished && (nodes[node_keys.indexOf(v)].isFinished = true);
                        isProcessing && (nodes[node_keys.indexOf(v)].isProcessing = true);
                    });
                };
console.log(nodes)
console.log(edges)
                return {nodes: nodes, edges: edges};
            // })
        // })
    };
    FC.prototype.generateFc = function(){
        var self = this;
        var styleArr = self.getStyle();
        var modelData = self.getModel();
        // return new Promise(function(resolve, reject){
        //     self.getModel().then(function(data){
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

                cy.nodes().on('click', function(e){
                    alert(this._private.data.id)
                })
                cy.edges().on('click', function(e){
                    alert('Edge_' + this._private.data.target)
                })
                cy.style(styleArr);

                return cy;
        //     });
        // })
        
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

    window.FC = FC;
})(window, jQuery, cytoscape)




		