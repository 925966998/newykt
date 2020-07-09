/**
 * Created by Administrator on 2017/11/8.
 */
$("#panleBox").panel({
    fit:true,
    title:'地图'
})
$(".mapfind01Ul li").click(function () {
    $("#con02").addClass("divDispay");
    $("#con01").removeClass("divDispay");

})
function redo() {
    $("#con01").addClass("divDispay");
    $("#con02").removeClass("divDispay");


}
// 加载树
$("#tree").tree({
    url:'json/userTree.json',
    animate:true,
    checkbox:true,
    lines:true,
    dnd:true,
    width:200,
    onContextMenu:function (e,node) {
        e.preventDefault();
        $("#tree").tree('select',node.target);
        $("#mean").menu('show',{
            left:e.pageX,
            top:e.pageY
        })

    }
})
$("#parentPart").combotree({
    url:'json/userTree.json',

    checkbox:true

})
$(".mapAtext").click(function () {
    $("#mapLine").panel({
        closed:false
    })


})

function mapLinefind() {
    $("#mapLine").panel({
        closed:true
    })

}
$("#mapCon01").click(function () {
    $("#mapLine").panel({
        closed:true
    })

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

            x : 'center',
            data:['类型一','类型二','类型三','类型四'],
            textStyle:{color:'#1580d9'}
        },

        calculable : false,
        series : [
            {
                name:'发售渠道',
                type:'pie',
                radius : ['40%', '60%'],
                center: ['50%', '60%'],
                itemStyle : {
                    normal : {
                        label : {
                            show : false
                        },
                        labelLine : {
                            show : false
                        }
                    },
                    emphasis : {
                        label : {
                            show : true,
                            position : 'center',
                            textStyle : {
                                fontSize : '13',
                                fontWeight : 'bold'
                            }
                        }
                    }
                },
                data:[
                    {value:335, name:'类型一',itemStyle:{
                        normal:{color:'#ff7d2e'}
                    }},
                    {value:310, name:'类型二',itemStyle:{
                        normal:{color:'#ffb400'}
                    }},
                    {value:234, name:'类型三',itemStyle:{
                        normal:{color:'#f0573d'}
                    }},
                    {value:135, name:'类型四',itemStyle:{
                        normal:{color:'#3d4145'}
                    }}

                ]
            }
        ]
    };
    myChart.setOption(option);
});
$(function(){
    var myChart = echarts.init($("#chart02")[0]);

    var labelTop = {
        normal : {
            label : {
                show : true,
                position : 'center',
                formatter : '{b}',
                textStyle: {
                    baseline : 'bottom'
                }
            },
            labelLine : {
                show : false
            }
        }
    };
    var labelBottom = {
        normal : {
            color: '#ccc',
            label : {
                show : true,
                position : 'center'
            },
            labelLine : {
                show : false
            }
        },
        emphasis: {
            color: 'rgba(0,0,0,0)'
        }
    };
    var labelFromatter = {
        normal : {
            label : {
                formatter : function (params){
                    return 100 - params.value + '%'
                },
                textStyle: {
                    baseline : 'top'
                }
            }
        },
    }

    var radius = [25, 40];
    option = {
        legend: {
            x : 'center',

            data:[
                '已经处理','未处理']
        },


        series : [
            {
                type : 'pie',
                center : ['25%', '55%'],
                radius : radius,
                x: '0%', // for funnel
                itemStyle : labelFromatter,
                data : [
                    {name:'other', value:56, itemStyle : labelBottom},
                    {name:'已经处理', value:54,itemStyle : labelTop}
                ]
            },
            {
                type : 'pie',
                center : ['70%', '55%'],
                radius : radius,
                x:'20%', // for funnel
                itemStyle : labelFromatter,
                data : [
                    {name:'other', value:56, itemStyle : labelBottom},
                    {name:'未处理', value:44,itemStyle : labelTop}
                ]
            }
        ]
    };

    myChart.setOption(option);
});


$("#mapCon01").click(function (e) {
    e=e||window.event;
    var x=e.pageX;
    var y=e.pageY;
    $(".mapTail").css({'left':x,'top':y});
    $(".mapTail").show();



})
function closeTail() {
    $(".mapTail").hide();


}