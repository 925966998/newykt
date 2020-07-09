/**
 * Created by Administrator on 2017/11/8.
 */

$(function () {
    //上传
    $('#file').ace_file_input({
        no_file: '请选择EXCEL ...',
        btn_choose: '选择',
        btn_change: '更改',
        droppable: true,
        // onchange:null,
        // thumbnail:false, //| true | large
        //onchange:''
        //

    }).on('file.error.ace', function (event, info) {//不匹配上面的文件格式就会跳出弹框提示
        alert("选择Excel格式的文件导入！");
    });
});

function fileupload(qbz) {
    var fileType = qbz.value.substr(qbz.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
    $.ajax({
        type: 'post',
        url: '/ky-ykt/projectSource/upload',
        processData: false,
        cache: false,
        contentType: false,
        data: new FormData($('#uploadForm')[0]),
        beforeSend: function () {
            $.messager.progress();
        },
        success: function (data) {
            $.messager.progress('close');
            $('#fileId').val(data);

        },
    })

}

$("#department").combobox({
    url: '/ky-ykt/department/queryByParams',
    method: 'get',
    height: 26,
    width: '70%',
    valueField: 'id',
    textField: 'departmentName'
})
$("#projectType").combobox({
    url: '/ky-ykt/projectType/queryByParams',
    method: 'get',
    height: 26,
    width: '70%',
    valueField: 'id',
    textField: 'name'
});
obj = {

    // 查询
    find: function () {
        $("#table").datagrid('load', {
            projectName: $("#nameSearch").val()
        })

    },
    // 添加
    addBox: function () {
        $("#id").val("");
        $("#addForm").form('clear');
        $("#addBox").dialog({
            closed: false
        });
    },
    // 编辑
    edit: function (id) {
        $("#addBox").dialog({
            closed: false
        })
        $.ajax({
            url: '/ky-ykt/projectSource/select',
            type: 'get',
            dataType: 'json',
            data: {id: id},
            success: function (res) {
                if (res != null) {
                    $('#addForm').form('load', {
                        projectName: res.projectName,
                        totalAmount: res.totalAmount,
                        centerAmount: res.centerAmount,
                        provinceAmount: res.provinceAmount,
                        note: res.note,
                        cityAmount: res.cityAmount,
                        countyAmount: res.countyAmount,
                        id: id,
                        documentNum: res.documentNum,

                    })
                    $("#projectType").combobox('setValue', res.projectType);
                    if (res.department != "0") {
                        $("#department").combobox('setValue', res.department);
                    } else {
                        $("#department").combobox('setValue', '');
                    }
                    $('#startTime').datetimebox('setValue', new Date(res.startTime).Format("yyyy-MM-dd HH:mm"))
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '更新失败'

                    })

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

    },
    look: function () {
        $("#lookTail").dialog({
            closed: false

        })

    },
    reset: function () {
        $("#addForm").form('clear');
    },
    sum: function () {
        $("#addForm").form('submit', {
            url: "/ky-ykt/projectSource/saveOrUpdate",
            //method: "post",
            onSubmit: function () {
                return $(this).form('validate')

            },
            success: function (data) {
                if (data.code = '10000') {
                    $("#table").datagrid('loaded');
                    $("#table").datagrid('load');
                    $("#addBox").dialog({
                        closed: true

                    })
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存成功'
                    })
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存失败'
                    })
                }

            },
            error: function (request) {
                if (request.status == 401) {
                    $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                        if (r) {
                            parent.location.href = "/login.html";
                        }
                    });
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存失败'
                    })
                }
            }
        })

    },
    // 删除多个
    del: function () {
        var rows = $("#table").datagrid("getSelections");
        if (rows.length > 0) {
            $.messager.confirm('确定删除', '你确定要删除你选择的记录吗？', function (flg) {
                if (flg) {
                    var ids = [];
                    for (i = 0; i < rows.length; i++) {
                        ids.push(rows[i].id);

                    }
                    var num = ids.length;
                    $.ajax({
                        type: 'get',
                        url: "/ky-ykt/projectSource/deleteMoney",
                        data: {
                            ids: ids.join(',')
                        },
                        beforesend: function () {
                            $("#table").datagrid('loading');

                        },
                        success: function (data) {
                            if (data.code = '10000') {

                                $("#table").datagrid('loaded');
                                $("#table").datagrid('load');
                                $("#table").datagrid('unselectAll');
                                $.messager.show({
                                    title: '提示',
                                    msg: num + '个用户被删除'
                                })

                            } else {
                                $.messager.show({
                                    title: '警示信息',
                                    msg: "信息删除失败"
                                })

                            }

                        },
                        error: function (request) {
                            if (request.status == 401) {
                                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                                    if (r) {
                                        parent.location.href = "/login.html";
                                    }
                                });
                            } else {
                                $.messager.show({
                                    title: '提示',
                                    msg: '信息删除失败'
                                })
                            }
                        }
                    })
                }

            })

        } else {
            $.messager.alert('提示', '请选择要删除的记录', 'info');
        }

    },

    //删除一个
    delOne: function (id) {
        $.messager.confirm('提示信息', '是否删除所选择记录', function (flg) {
            if (flg) {
                $.ajax({
                    type: 'get',
                    url: '/ky-ykt/projectSource/deleteOne',
                    data: {
                        id: id
                    },
                    beforesend: function () {
                        $("#table").datagrid('loading');

                    },
                    success: function (data) {
                        if (data.code = '1000') {
                            $("#table").datagrid("loaded");
                            $("#table").datagrid("load");
                            $("#table").datagrid("unselectRow");
                            $.messager.show({
                                title: '提示信息',
                                msg: "信息删除成功"
                            })
                        } else {
                            $.messager.show({
                                title: '警示信息',
                                msg: "信息删除失败"
                            })

                        }

                    },
                    error: function (request) {
                        if (request.status == 401) {
                            $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                                if (r) {
                                    parent.location.href = "/login.html";
                                }
                            });
                        } else {
                            $.messager.show({
                                title: '提示',
                                msg: '信息删除失败'
                            })
                        }
                    }
                })

            }

        })
    }
}
// 加载表格
$("#table").datagrid({
    title: "数据列表",
    iconCls: "icon-left02",
    url: '/ky-ykt/projectSource/queryPage',
    fitColumns: true,
    striped: true,
    method: "GET",
    pagination: true,
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
    toolbar: '#tabelBut',
    columns: [[
        {
            checkbox: true,
            field: 'no',
            width: 100,
            align: 'center'
        },
        {
            field: 'projectName',
            title: '资金来源名称',
            width: 100,
            align: 'center',
        },
        {
            field: 'documentNum',
            title: '文号',
            width: 100,
            align: 'center'
        }, {
            field: 'departmentName',
            title: '所属单位',
            width: 100,
            align: 'center'
        },
        {
            field: 'projectTypeName',
            title: '资金类型',
            width: 100,
            align: 'center',
        },
        {
            field: 'startTime',
            title: '开始发放时间',
            width: 100,
            align: 'center',
            formatter: function (value, row, index) {
                if (value != null) {
                    return new Date(value).Format("yyyy-MM-dd HH:mm")
                }

            }
        },
        {
            field: 'totalAmount',
            title: '总金额',
            width: 100,
            align: 'center'
        },
        {
            field: 'zijin',
            title: '资金占比',
            width: 100,
            align: 'center',
            formatter: function (value, row, index) {
                return '中央：' + row.centerAmount + '<br>省：' + row.provinceAmount + '<br>市：' + row.cityAmount + '<br>区县：' + row.countyAmount;
            }
        },
        {
            field: 'surplusAmount',
            title: '结余金额',
            width: 100,
            align: 'center',
        }, {
            field: 'note',
            title: '描述',
            width: 100,
            align: 'center',
        },
        {
            field: "opr",
            title: '操作',
            width: 100,
            align: 'center',
            formatter: function (val, row) {
                e = '<a  id="add" data-id="98" class=" operA"  onclick="obj.edit(\'' + row.id + '\')">编辑</a> ';
                d = '<a  id="add" data-id="98" class=" operA01"  onclick="obj.delOne(\'' + row.id + '\')">删除</a> ';
                return e + d;

            }

        }
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