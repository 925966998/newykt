obj = {
    // 查询
    find: function () {
        $("#table").datagrid('load', {
            state: 3,
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
    queryParams: {state: 3},
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
        {field: 'state', title: '上卡状态', width: 100, align: 'center',
            formatter: function (val, row) {
                switch (val) {
                    case 0:  return '<div>待审核</div>';
                    case 1:  return '<div>审核通过，待发放</div>';
                    case 2:  return '<div>审核不通过</div>';
                    case 3:  return '<div>审核通过，已发放</div>';
                    case 4:  return '<div>已发送上卡</div>';
                    case 5:  return '<div>上卡成功</div>';
                    case 6:  return '<div>上卡失败</div>';
                }
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
        {field: 'totalAmount', title: '总金额', width: 100, align: 'center',
            formatter: function (val, row) {if (val == 0) {return '0.00';} else {return toMoney(val);}}},
        {field: 'paymentAmount', title: '发放金额', width: 100, align: 'center',
            formatter: function (val, row) {if (val == 0) {return '0.00';} else {return toMoney(val);}}},
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

// 将数字转换成金额显示
function toMoney(num) {
    if (num) {
        if (num == "0") {
            return '0.00';
        }
        if (isNaN(num)) {
            //alert('金额中含有不能识别的字符');
            return;
        }
        num = typeof num == 'string' ? parseFloat(num) : num // 判断是否是字符串如果是字符串转成数字
        num = num.toFixed(2); // 保留两位
        //console.log(num)
        num = parseFloat(num); // 转成数字
        num = num.toLocaleString(); // 转成金额显示模式
        // 判断是否有小数
        if (num.indexOf('.') === -1) {
            num = num + '.00';
        } else {
            //console.log(num.split('.')[1].length)
            num = num.split('.')[1].length < 2 ? num + '0' : num;
        }
        return num; // 返回的是字符串23,245.12保留2位小数
    } else {
        return num = null;
    }
}