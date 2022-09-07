obj = {
    // 查询
    find: function () {
        $("#table").datagrid('load', {
            state: 1,
            flag: 1,
            projectName: $("#projectNameSearch").val(),
        })

    },
    pull: function () {
        var rows = $("#table").datagrid("getSelections");
        console.log(rows.length)
        if (rows.length > 1) {
            $.messager.alert('提示', '每次选择一条发放记录', 'info');
        } else if (rows.length < 1) {
            $.messager.alert('提示', '请选择一条发放记录', 'info');
        } else {
            window.location.href = '/ky-ykt/projectDetail/export?id=' + rows[0].id
        }
    },
    pullBank: function () {
        var rows = $("#table").datagrid("getSelections");
        console.log(rows.length)
        if (rows.length > 1) {
            $.messager.alert('提示', '每次选择一条发放记录', 'info');
        } else if (rows.length < 1) {
            $.messager.alert('提示', '请选择一条发放记录', 'info');
        } else {
            $.messager.confirm('确定上卡', '你确定要发放你选择的记录吗？', function (flg) {
                if (flg) {
                    $.ajax({
                        url: '/ky-ykt/bankFile/pullAllInfo?projectId=' + rows[0].id,
                        type: 'get',
                        dataType: 'json',
                        success: function (res) {
                            console.log(res)
                            $("#table").datagrid('reload')
                            $.messager.alert('提示', res.data);
                        },
                        error: function () {
                            $.messager.show({
                                title: '提示',
                                msg: '发放失败'
                            })
                        }
                    })
                }
            })
        }
    },
    pullLook: function (id) {
        $("#pullLookBox").dialog({
            closed: false
        });
    },
}

// 加载表格
$("#table").datagrid({
    title: "数据列表",
    iconCls: "icon-left02",
    url: '/ky-ykt/projectDetail/queryPage',
    queryParams: {state: 4, flag: 1},
    fitColumns: true,
    striped: true,
    pagination: true,
    pageSize: 10,
    method: "GET",
    width: '100%',
    rownumbers: true,
    pageList: [10, 20,50,100],
    pageNumber: 1,
    nowrap: false,
    singleSelect: true,
    height: 'auto',
    sortName: 'id',
    checkOnSelect: true,
    sortOrder: 'asc',
    toolbar: '#tabelBut',
    columns: [[
        {checkbox: true, field: 'no', width: 100, align: 'center'},
        {field: 'projectTypeName', title: '项目名称', width: 100, align: 'center'},
        {field: 'cardState', title: '上卡状态', width: 100, align: 'center',
            formatter: function (val, row) {
                if (val == '5')
                    return '已上卡，请等待结果';
            }
        },
        {field: 'startTime', title: '开始发放时间', width: 100, align: 'center',
            formatter: function (value, row, index) {
                if (value != null) {
                    return new Date(value).Format("yyyy-MM-dd HH:mm")
                }
            }
        },
        {field: 'endTime', title: '结束时间', width: 100, align: 'center',
            formatter: function (value, row, index) {
                if (value != null) {
                    return new Date(value).Format("yyyy-MM-dd HH:mm")
                }
            }
        },
        {field: 'totalAmount', title: '总金额', width: 100, align: 'center'},
        {field: 'paymentAmount', title: '发放金额', width: 100, align: 'center'},
        {field: 'departmentName', title: '发放单位', width: 100, align: 'center'},
        /*
        {field: "opr", title: '操作', width: 100, align: 'center',
            formatter: function (val, row) {
                c = '<a  id="look"   onclick="obj.pullLook(\'' + row.id + '\')">查看</a> ';
                return c;
            }
        }
        */
    ]],
    onLoadError: function (request) {
        if (request.status == 401) {
            $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                if (r) {
                    parent.location.href = "/login.html";
                }
            });
        }
    }
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

// 加载物流详情
$("#pullLookBox").dialog({
    title: "发放信息",
    width: 650,
    height: 200,
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