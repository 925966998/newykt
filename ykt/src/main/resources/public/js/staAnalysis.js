/**
 * Created by Administrator on 2017/11/8.
 */
$("#calder").calendar({
        height:200

})
$("#rightTab").tabs({
       height:250,
        border:false
})
$(function(){
        var myChart = echarts.init($("#chart01")[0]);
//app.title = '堆叠柱状图';

        option = {

                tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {

                        left: 'left',
                        data: ['正常','异常']
                },
                series : [
                        {
                                name: '访问来源',
                                type: 'pie',
                                radius : '55%',
                                center: ['50%', '60%'],
                                data:[
                                        {value:335, name:'正常'},
                                        {value:310, name:'异常'},

                                ],
                                itemStyle: {
                                        emphasis: {
                                                shadowBlur: 10,
                                                shadowOffsetX: 0,
                                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                                        }
                                }
                        }
                ]
        };
        myChart.setOption(option);
});
$(function(){
        var myChart = echarts.init($("#chart02")[0]);
//app.title = '堆叠柱状图';

        option = {
                tooltip : {
                        trigger: 'axis',
                        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                },
                legend: {
                        data:['直接访问', '邮件营销','联盟广告','视频广告','搜索引擎']
                },

                calculable : false,
                xAxis : [
                        {
                                type : 'value'
                        }
                ],
                yAxis : [
                        {
                                type : 'category',
                                data : ['周一','周二','周三','周四','周五','周六','周日']
                        }
                ],
                series : [
                        {
                                name:'直接访问',
                                type:'bar',
                                stack: '总量',
                                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                                data:[320, 302, 301, 334, 390, 330, 320]
                        },
                        {
                                name:'邮件营销',
                                type:'bar',
                                stack: '总量',
                                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                                data:[120, 132, 101, 134, 90, 230, 210]
                        },
                        {
                                name:'联盟广告',
                                type:'bar',
                                stack: '总量',
                                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                                data:[220, 182, 191, 234, 290, 330, 310]
                        },
                        {
                                name:'视频广告',
                                type:'bar',
                                stack: '总量',
                                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                                data:[150, 212, 201, 154, 190, 330, 410]
                        },
                        {
                                name:'搜索引擎',
                                type:'bar',
                                stack: '总量',
                                itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
                                data:[820, 832, 901, 934, 1290, 1330, 1320]
                        }
                ]
        };

        myChart.setOption(option);
});
$(function(){
        var myChart = echarts.init($("#chart03")[0]);
//app.title = '堆叠柱状图';

        option = {

                tooltip : {
                        trigger: 'axis'
                },
                legend: {
                        data:['最高气温','最低气温']
                },

                calculable : true,
                xAxis : [
                        {
                                type : 'category',
                                boundaryGap : false,
                                data : ['周一','周二','周三','周四','周五','周六','周日']
                        }
                ],
                yAxis : [
                        {
                                type : 'value',
                                axisLabel : {
                                        formatter: '{value} °C'
                                }
                        }
                ],
                series : [
                        {
                                name:'最高气温',
                                type:'line',
                                data:[11, 11, 15, 13, 12, 13, 10],
                                markPoint : {
                                        data : [
                                                {type : 'max', name: '最大值'},
                                                {type : 'min', name: '最小值'}
                                        ]
                                },
                                markLine : {
                                        data : [
                                                {type : 'average', name: '平均值'}
                                        ]
                                }
                        },
                        {
                                name:'最低气温',
                                type:'line',
                                data:[1, -2, 2, 5, 3, 2, 0],
                                markPoint : {
                                        data : [
                                                {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
                                        ]
                                },
                                markLine : {
                                        data : [
                                                {type : 'average', name : '平均值'}
                                        ]
                                }
                        }
                ]
        };
        myChart.setOption(option);
});