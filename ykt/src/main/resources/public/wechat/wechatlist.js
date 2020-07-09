// 加载表格
$("#table").datagrid({
    title: "交易记录",
    //iconCls: "icon-left02",
    url: '/ky-ykt/wechat/queryParam',
    //iconCls: "icon-left02",
    fitColumns: true,
    striped: true,
    method: "GET",
    agination: true,
    pageSize: 10,
    width: '100%',
    rownumbers: true,
    pageList: [10, 20],
    pageNumber: 1,
    nowrap: true,
    height: 'auto',
    sortName: 'id',
    checkOnSelect: true,
    sortOrder: 'asc',
    //toolbar: '#tabelBut',
    columns: [[
        {
            field: 'createTime',
            title: '发放时间',
            width: 100,
            align: 'center',
            formatter:
                function (value, row, index) {
                    return '<span style="font-size:15px">' + value + '</span>';//改变表格中内容字体的大小
                }
        },
        {
            field: 'grantAmount',
            title: '发放金额',
            width: 100,
            align: 'center',
            formatter:
                function (value, row, index) {
                    return '<span style="font-size:15px">' + value + '</span>';//改变表格中内容字体的大小
                }

        }
    ]],
})


$(function () {


    // $('.weui-tab').tab({
    //     defaultIndex: 0,
    //     activeClass: 'weui-bar__item_on',
    //     onToggle: function (index) {
    //         /*
    //         if (index == 0) {
    //             $("#c" + index).html("我是第一个0")
    //         } else if (index == 1) {
    //             $("#c" + index).html("我是第2个1")
    //         }
    //         */
    //         console.log(index)
    //
    //     }
    // });


})

