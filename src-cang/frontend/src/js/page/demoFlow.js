
var avalon = require('avalon2') ;
var $ = require('jquery') ;
require('../component/header.js');



var flowChart = function() {

    var data = {
        vbox : [
            { id : 'V1' , edge : [
                {id: 'E1', to : 'V2', left : 0},
                {id: 'E3', to : 'V4', left : 0}
            ]},
            { id : 'V2' , edge : [
                {id: 'E2', to : 'V3'}
            ]},
            { id : 'V3' , edge : [

            ]}
        ],
            edge : [
            { id : 'E1', begin : 'V1', end : 'V2'}
        ]
    };

    var vboxList = [];
    var lineList = [];
    function checkVbox(box){
        if (vboxList.indexOf(box) > -1){
            return true;
        }else{
            return false;
        }
    }

    function getAllVbox(list) {
        vboxList = [];
        list.forEach(function(item, index){
            vboxList.push(item.id)
        })
    }

    function getAllline(list) {
        lineList = [];
        list.forEach(function(vbox, index){
            vboxList.push(vbox.id)

            vbox.edge.forEach(function(line, index){
                lineList.push(line.id)
            })
        })
    }
    function checkLine(line){
        if (lineList.indexOf(line) > -1){
            return true;
        }else{
            return false;
        }
    }


    var vm = avalon.define({
        $id : 'flowController',

        currentVbox : {
            id : ''
        },
        addLine : {
            id : '',
            to : ''
        },

        flowData : {
            vbox : [
                { id : 'V1' , edge : [
                    {id: 'E1', to : 'V2', left : 0}
                ]},
                { id : 'V2' , edge : [
                    {id: 'E2', to : 'V3'}
                ]},
                { id : 'V3' , edge : [

                ]}
            ]
        },

        clickVbox : function(currentVbox){
            vm.currentVbox = currentVbox;
        },

        addVbox : function(event){
            event.preventDefault();

            if (vm.currentVbox && vm.currentVbox.id){

                if (vm.addLine.id){
                    getAllline(vm.flowData.vbox)
                    getAllVbox(vm.flowData.vbox);

                    if (checkLine(vm.addLine.id)){
                        console.log("线名称已存在!")
                        return ;
                    }



                    if (checkVbox(vm.addLine.to)){

                    }else{
                        //创建新节点
                        var newVbox = {
                            id : vm.addLine.to ,
                            edge : []
                        }
                        var newLine = {
                            id : vm.addLine.id ,
                            to : vm.addLine.to,
                            left : 0
                        }

                        vm.flowData.vbox.push(newVbox)

                        vm.flowData.vbox.forEach(function(vbox, index){
                            if (vbox.id === vm.currentVbox.id){
                                vbox.edge.push(newLine)
                            }
                        })

                        console.log("添加成功!")
                    }


                }else{
                    console.log("请填写线名称!")
                }


            }else {
                console.log("请选择节点!")
            }

            console.log(vm.flowData.vbox)
        }



    });

};





flowChart();


module.exports = flowChart;