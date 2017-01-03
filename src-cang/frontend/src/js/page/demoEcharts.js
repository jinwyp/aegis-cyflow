
require('../component/header.js');
var echarts = require('echarts');



// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('main'));
// var myChart2 = echarts.init(document.getElementById('main2'));
    // myChart.showLoading();
    // myChart.hideLoading();
// 绘制图表
myChart.setOption({
    // title: { text: '折线图' },
    // tooltip: {
    //     trigger: 'axis'
    // },
    // legend: {
    //     data:['煤炭价格']
    // },
    // toolbox: {
    //     feature: {
    //         saveAsImage: {}
    //     },
    //     right:'4%'
    // },
    // grid: {
    //     left: '3%',
    //     right: '4%',
    //     bottom: '3%',
    //     containLabel: true
    // },
    // xAxis : [
    //     {
    //         type : 'category',
    //         boundaryGap : false,
    //         data : ['周一','周二','周三','周四','周五','周六','周日']
    //     }
    // ],
    // yAxis : [
    //     {
    //         type : 'value'
    //     }
    // ],
    // series : [
    //     {
    //         name:'煤炭价格',
    //         type:'line',
    //         stack: '总量',
    //         lineStyle:{
    //             normal:{
    //                 color:'#98cab3'
    //             }
    //         },
    //         areaStyle: {
    //             normal: {
    //                 color:'#aed4c2'
    //             }
    //         },
    //         itemStyle:{
    //             normal:{
    //                 color:'#98cab3'
    //             }
    //         },
    //         data:[5.5, 5.6, 5.7, 6.0, 6.2, 6.4, 6.0]
    //     }
    // ]
    title: {
        text: ''
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['警戒线','货值','参考指数货值']
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            data : ['周一','周二','周三','周四','周五','周六','周日']
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'警戒线',
            type:'line',
            stack: '总量',
            areaStyle: {normal: {}},
            data:[120, 132, 101, 134, 90, 230, 210]
        },
        {
            name:'货值',
            type:'line',
            stack: '总量',
            areaStyle: {normal: {}},
            data:[220, 182, 191, 234, 290, 330, 310]
        },
        {
            name:'参考指数货值',
            type:'line',
            stack: '总量',
            areaStyle: {normal: {}},
            data:[150, 232, 201, 154, 190, 330, 410]
        }

    ]
});
