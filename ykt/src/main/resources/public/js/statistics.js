/**
 * Created by Administrator on 2017/11/8.
 */
$(function () {
    statistics();
    statistics1();
    statistics2();
    statistics3();
    statistics4();
})
$("#paymentDepartment").combobox({
    url: '/ky-ykt/department/queryByParams',
    method: 'get',
    height: 26,
    width: '15%',
    valueField: 'id',
    textField: 'departmentName',
    loadFilter: function (data) {
        var obj = {};
        obj.id = '';
        obj.departmentName = '请选择'
        //在数组0位置插入obj,不删除原来的元素
        data.splice(0, 0, obj)
        return data;
    }
})
$("#projectType").combobox({
    url: '/ky-ykt/projectType/queryByParams',
    method: 'get',
    height: 26,
    width: '15%',
    valueField: 'id',
    textField: 'name',
    loadFilter: function (data) {
        var obj = {};
        obj.id = '';
        obj.name = '请选择'
        //在数组0位置插入obj,不删除原来的元素
        data.splice(0, 0, obj)
        return data;
    }
});
function statistics1 () {
    $.ajax({
        url: '/ky-ykt/statistics/statistic',
        type: 'get',
        dataType: 'json',
        success: function (res) {
            var myChart = echarts.init($("#chart01")[0]);
            var datas = res.datas;
            var q = [];
            var qq = [];
            for (var i = 0; i < datas.length; i++) {
                var x = {name: datas[i].projectType, value: datas[i].num}
                q.push(x)
                qq.push(datas[i].projectType)
            }
            console.log(q)
            console.log(qq)
            option = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {

                    left: 'left',
                    data: qq
                },
                series: [
                    {
                        name: '资金项目属性统计',
                        type: 'pie',
                        radius: '55%',
                        center: ['50%', '60%'],
                        data: q,
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
            console.log(myChart)
        }
    })

};
function statistics2() {
    $.ajax({
        url: '/ky-ykt/project/queryCount',
        type: 'get',
        dataType: 'json',
        success: function (res) {
            $('#totalNum').text((parseInt(res.finishState) + parseInt(res.ingState)));
            $('#finishNum').text(res.finishState);
            $('#ingNum').text(res.ingState);
            var myChart = echarts.init($("#chart03")[0]);
            option = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {

                    left: 'left',
                    data: ['发放完成', '发放中']
                },
                series: [
                    {
                        name: '发放统计',
                        type: 'pie',
                        radius: '55%',
                        center: ['50%', '60%'],
                        data: [
                            {value: res.finishState, name: '发放完成'},
                            {value: res.ingState, name: '发放中'},

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
        }
    })

};
function statistics3() {
    $.ajax({
        url: '/ky-ykt/statistics/dateStatistic',
        type: 'get',
        dataType: 'json',
        success: function (res) {
            var data = [];
            var datas = [];
            var datatotal = [];
            for (var i = 0; i < res.length; i++) {
                data.push(res[i].date);
                if (res[i].paymentAmount == null) {
                    datas.push(0);
                } else {
                    datas.push(res[i].paymentAmount)
                }
                if (res[i].surplusAmount == null) {
                    datatotal.push(0);
                } else {
                    datatotal.push(res[i].surplusAmount)
                }

            }
            console.log(data)
            console.log(datas)
            console.log(datatotal)
            var myChart = echarts.init($("#chart04")[0]);
            option = {
                title: {
                    text: '近半年资金发放统计分析'
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        label: {
                            backgroundColor: '#6a7985'
                        }
                    }
                },
                legend: {
                    data: ['剩余金额', '已发放金额']
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
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: data
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '剩余金额',
                        type: 'line',
                        stack: '总量',
                        areaStyle: {},
                        data: datatotal
                    },
                    {
                        name: '已发放金额',
                        type: 'line',
                        stack: '总量',
                        areaStyle: {},
                        data: datas
                    }
                ]
            };

            myChart.setOption(option);
        }
    })

};
function statistics4() {
    $.ajax({
        url: '/ky-ykt/project/queryByParams',
        type: 'get',
        dataType: 'json',
        data: {projectId: window.location.href.split("=")[1]},
        success: function (res) {
            var myChart = echarts.init($("#chart02")[0]);
            var dataAxis = [];
            var data = [];
            var datax = [];
            for (var i = 0; i < res.length; i++) {
                dataAxis.push(res[i].projectName);
                data.push(res[i].totalAmount);
                datax.push(res[i].totalAmount);
            }
            var yMax = 500;
            var dataShadow = [];

            for (var i = 0; i < data.length; i++) {
                dataShadow.push(yMax);
            }

            option = {
                title: {
                    text: '资金项目资金展示分析',
                },
                xAxis: {
                    data: dataAxis,
                    type: 'category',
                    splitLine: {show: false},
                },
                yAxis: {
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#999'
                        }
                    }
                },
                dataZoom: [
                    {
                        type: 'inside'
                    }
                ],
                series: [
                    { // For shadow
                        type: 'bar',
                        itemStyle: {
                            color: 'rgba(0,0,0,0.05)'
                        },
                        barGap: '-100%',
                        barCategoryGap: '40%',
                        data: dataShadow,
                        animation: false
                    },
                    {
                        type: 'bar',
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#83bff6'},
                                    {offset: 0.5, color: '#188df0'},
                                    {offset: 1, color: '#188df0'}
                                ]
                            )
                        },
                        emphasis: {
                            itemStyle: {
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#2378f7'},
                                        {offset: 0.7, color: '#2378f7'},
                                        {offset: 1, color: '#83bff6'}
                                    ]
                                )
                            }
                        },
                        data: data
                    },
                    {
                        name: '总费用',
                        type: 'bar',
                        stack: '总量',
                        label: {
                            show: true,
                            position: 'inside'
                        },
                        data: data
                    }
                ]
            };

// Enable data zoom when user click bar.
            var zoomSize = 6;
            myChart.on('click', function (params) {
                console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
                myChart.dispatchAction({
                    type: 'dataZoom',
                    startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
                    endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
                });
            });
            myChart.setOption(option);
        }
    });

};
function statistics(){
    $.ajax({
        url: '/ky-ykt/statistics/statisticCount',
        type: 'get',
        dataType: 'json',
        data: {projectType: $("#projectType").val(),
            paymentDepartment: $("#paymentDepartment").val(),
            startTime: $("#startTime").val(),
            endTime: $("#endTime").val()},
        success: function (res) {
            if(res!=null){
                $('#paymentAmount').text(res);
            }else{
                $('#paymentAmount').text(0);
            }

        },
        error: function (request) {
            if (request.status == 401) {
                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                    if (r) {
                        parent.location.href = "/login.html";
                    }
                });
            }
        }
    })
}
obj = {

    // 查询
    find: function () {
        $("#table").datagrid('load', {
            projectType: $("#projectType").val(),
            paymentDepartment: $("#paymentDepartment").val(),
            startTime: $("#startTime").val(),
            endTime: $("#endTime").val(),
        })
        statistics();
        statistics1();
        statistics2();
        statistics3();
        statistics4();
    },
}
// 加载表格
$("#table").datagrid({
    title: "数据列表",
    iconCls: "icon-left02",
    url: '/ky-ykt/statistics/projectPage',
    fitColumns: true,
    striped: true,
    pagination: true,
    pageSize: 10,
    method: "GET",
    width: '100%',
    rownumbers: true,
    pageList: [10, 20],
    pageNumber: 1,
    nowrap: true,
    height: 'auto',
    sortName: 'id',
    checkOnSelect: true,
    singleSelect: true,
    sortOrder: 'asc',
    toolbar: '#tabelBut',
    columns: [[
        {
            field: 'projectName',
            title: '项目名称',
            width: 100,
            align: 'center'
        },
        {
            field: 'projectTypeName',
            title: '补贴项目名称',
            width: 100,
            align: 'center',
        },
        {
            field: 'startTime',
            title: '发放时间',
            width: 100,
            align: 'center',
            formatter: function (value, row, index) {
                if (value != null) {
                    return new Date(value).Format("yyyy-MM-dd HH:mm")
                }
            }
        }, {
            field: 'totalAmount',
            title: '总金额',
            width: 100,
            align: 'center'
        },
        {
            field: 'paymentAmount',
            title: '发放金额',
            width: 100,
            align: 'center'
        }, {
            field: 'departmentName',
            title: '所属单位',
            width: 100,
            align: 'center'
        },
    ]],
})
// 弹出框加载
$("#addBox").dialog({
    title: "信息内容",
    width: 500,
    height: 300,
    closed: true,
    modal: true,
    shadow: true
})
// 加载物流详情
$("#lookTail").dialog({
    title: "信息内容",
    width: 650,
    height: 410,
    closed: true,
    modal: true,
    shadow: true
})
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}