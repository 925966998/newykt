// 加载树
function doQuery(url) {
    $("#table").datagrid({
        method: "get",
        iconCls: "icon-left02",
        url: url,
        fitColumns: true,
        striped: true,
        pagination: true,
        pageSize: 10,
        width: '100%',
        rownumbers: true,
        pageNumber: 1,
        nowrap: true,
        height: 'auto',
        sortName: 'id',
        //checkOnSelect: false,
        //sortOrder: 'asc',
        toolbar: '#tabelBut',
        columns: [[
            {
                field: 'name',
                title: '姓名',
                width: 100,
                align: 'center'
            },
            {
                field: 'phone',
                title: '手机号',
                width: 100,
                align: 'center'
            },
            {
                field: 'idCardNo',
                title: '身份证号',
                width: 100,
                align: 'center'
            },
            {
                field: 'openingBank',
                title: '开户行',
                width: 100,
                align: 'center'
            },
            {
                field: 'bankCardNo',
                title: '社保卡号',
                width: 100,
                align: 'center'
            },
            {
                field: 'projectName',
                title: '项目资金名称',
                width: 100,
                align: 'center'
            },
            {
                field: 'grantAmount',
                title: '发放金额',
                width: 100,
                align: 'center'
            },
            {
                field: 'departmentName',
                title: '发放部门',
                width: 100,
                align: 'center'
            },
            {
                field: 'status',
                title: '状态',
                width: 100,
                align: 'center',
                formatter: function (val, row) {
                    if (val == '0')
                        return '未发放';
                    if (val == '1')
                        return '发放成功';
                    if (val == '2')
                        return '发放失败';
                }
            },
            {
                field: 'failReason',
                title: '失败原因',
                width: 100,
                align: 'center'

            }/*,
            {
                field: "opr",
                title: '操作',
                width: 100,
                align: 'center',
                formatter: function (val, row) {
                    e = '<a  id="add" data-id="98" class=" operA"  onclick="obj.edit(\'' + row.id + '\')">编辑</a> ';
                    d = '<a  id="del" data-id="98" class=" operA01"  onclick="obj.delOne(\'' + row.id + '\')">删除</a> ';
                    return e + d;

                }
            }*/
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
}

$(function () {
    // 加载表格
    doQuery('/ky-ykt/person/queryPage?status=2');
})

obj = {
    // 查询
    find: function () {
        doQuery('/ky-ykt/person/queryPage?status=2&' + $("#tableFindForm").serialize())
    },
    // 编辑
    edit: function (id) {
        $("#addBox").dialog({
            closed: false,
        });
        $("#addForm").form('clear');

        $.ajax({
            url: '/ky-ykt/person/queryById?id=' + id,
            type: 'get',
            dataType: 'json',
            beforeSend: function () {
                $.messager.progress();
            },
            success: function (data) {
                $.messager.progress('close');
                var data = data.data;
                if (data) {
                    $("#id").val(id);
                    $("#name").val(data.name);
                    $("#phone").val(data.phone);
                    $("#grantAmount").val(data.grantAmount);
                    $("#idCardNo").val(data.idCardNo);
                    $("#bankCardNo").val(data.bankCardNo);
                    $("#county").val(data.county);
                    $("#address").val(data.address);
                    $("#openingBank").val(data.openingBank);
                }

            },
            error: function (request) {
                $.messager.progress('close');
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
    // 提交表单
    sum: function () {
        $('#addForm').form('submit', {
            onSubmit: function () {
                var lag = $(this).form('validate');
                if (lag == true) {
                    $.ajax({
                        url: '/ky-ykt/person/saveOrUpdate',
                        type: 'post',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data: form2Json("addForm"),
                        success: function (data) {
                            $.messager.progress('close');
                            $("#table").datagrid('reload');
                            $.messager.show({
                                title: '提示',
                                msg: '提交成功'
                            })
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
            },
            success: function () {
                $.messager.progress('close');
                $("#addBox").dialog({
                    closed: true

                })
                $("#table").datagrid('reload')
            }
        });

    },
    // 取消
    can: function () {
        $("#addBox").dialog({
            closed: true
        })

    },
    //提交多个
    submitToBuss: function () {

        $.messager.confirm('确定提交', '你确定要提交推送吗？', function (flg) {
            if (flg) {

                $.ajax({
                    type: 'POST',
                    url: "/ky-ykt/person/submitToBuss",
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    beforeSend: function () {
                        $("#table").datagrid('loading');

                    },
                    success: function (data) {
                        if (data) {
                            $("#table").datagrid('reload');
                            $.messager.show({
                                title: '提示',
                                msg: '推送成功'
                            })

                        } else {
                            $.messager.show({
                                title: '警示信息',
                                msg: "推送失败"
                            })

                        }

                    },
                    error: function (request) {
                        $("#table").datagrid('reload');
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

        })
    },
    //删除一个
    delOne: function (id) {
        id = $("#table").datagrid('getSelected').id;
        $.messager.confirm('提示信息', '是否删除所选择记录', function (flg) {
            if (flg) {
                $.ajax({
                    type: 'get',
                    url: '/ky-ykt/person/deleteForce?id=' + id,
                    beforeSend: function () {
                        $("#table").datagrid('loading');

                    },
                    success: function (data) {
                        if (data) {
                            $("#table").datagrid("reload");
                            $.messager.show({
                                title: '提示信息',
                                msg: "数据删除成功"
                            })
                        } else {
                            $.messager.show({
                                title: '警示信息',
                                msg: "数据删除失败"
                            })

                        }

                    }, error: function (request) {
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

        })
    },
    // 重置表单
    res: function () {
        $("#addForm").form('clear');

    },
    // 取消表单
    can: function () {
        $("#addBox").dialog({
            closed: true

        })

    }
}
// 弹出框加载
$("#auditBox").dialog({
    title: "编辑数据",
    width: 500,
    height: 300,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})
// 弹出框加载
$("#showBox").dialog({
    title: "查看",
    width: '100%',
    height: '100%',
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})
$("#upload").click(function () {
    $.ajax({
        type: 'post',
        url: '/ky-ykt/person/compareExcel',
        processData: false,
        cache: false,
        contentType: false,
        data: new FormData($('#uploadForm')[0]),
        beforeSend: function () {
            $.messager.progress({
                text: '上传中。。。'
            });
        },
        success: function (data) {
            console.log(data)
            $.messager.progress('close');
            $("#table").datagrid('reload')
            if (data.code != 10000) {
                $.messager.alert('提示', data.data, '上传失败');
            } else {
                $.messager.show({
                    title: '提示',
                    msg: '上传成功'
                })
            }

        },
        error: function (request) {
            $.messager.progress('close');
            if (request.status == 401) {
                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                    if (r) {
                        parent.location.href = "/login.html";
                    }
                });
            }
        }
    })

})