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
                field: 'userName',
                title: '用户名',
                width: 100,
                align: 'center'


            },
            {
                field: 'fullName',
                title: '真实姓名',
                width: 100,
                align: 'center'


            },
            /*
            {
                field: 'idCardNo',
                title: '身份证号',
                width: 100,
                align: 'center'
            },
            */
            {
                field: 'phone',
                title: '手机号',
                width: 100,
                align: 'center'


            },
            {
                field: 'roleName',
                title: '角色',
                width: 100,
                align: 'center'
            },
            {
                field: 'departmentName',
                title: '所属部门',
                width: 100,
                align: 'center'
            },
            {
                field: 'status',
                title: '状态',
                width: 100,
                align: 'center',
                formatter: function (val, row) {
                    if (val == '0') {
                        return '<div style="color: green">正常</div>';
                    } else {
                        return '<div style="color: red">注销</div>';
                    }

                }
            },
            {
                field: "opr",
                title: '操作',
                width: 100,
                align: 'center',
                formatter: function (val, row) {
                    r = '<a  id="repass" data-id="98" class=" operA01"  onclick="obj.repass(\'' + row.id + '\')">重置密码</a> ';
                    e = '<a  id="add" data-id="98" class=" operA"  onclick="obj.edit(\'' + row.id + '\')">编辑</a> ';
                    d = '<a  id="del" data-id="98" class=" operA01"  onclick="obj.delOne(\'' + row.id + '\')">删除</a> ';
                    return r + e + d;
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

}

$(function () {
    // 加载表格
    doQuery('/ky-ykt/sysUser/queryPage');
    doQueryDepartAndRole('departmentId', 'role');
})

function doQueryDepartAndRole(did, rid) {
    // 加载部门下拉框
    $("#" + did).combobox({
        url: '/ky-ykt/department/queryByParams',
        method: 'get',
        valueField: 'id',
        textField: 'departmentName'
    });
    $("#" + rid).combobox({
        url: '/ky-ykt/role/queryByParams',
        method: 'get',
        valueField: 'id',
        textField: 'roleName'
    });
}


obj = {
    // 查询
    find: function () {
        doQuery('/ky-ykt/sysUser/queryPage?' + $("#tableFindForm").serialize())
    },
    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false

        });
        $("#addForm").form('clear');
        doQueryDepartAndRole('departmentByDialog', 'roleByDialog');
    },
    // 编辑
    edit: function (id) {
        $("#addBox").dialog({
            closed: false,
        })
        doQueryDepartAndRole('departmentByDialog', 'roleByDialog');
        $.ajax({
            url: '/ky-ykt/sysUser/queryById?id=' + id,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                var data = data.data;
                if (data) {
                    $("#id").val(data.id);
                    $("#userName").val(data.userName);
                    $("#phone").val(data.phone);
                    $("#fullName").val(data.fullName);
                    $("#idCardNo").val(data.idCardNo);
                    $('#roleByDialog').combobox('setValues', data.roleId);
                    $('#departmentByDialog').combobox('setValues', data.departmentId);
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
    // 提交表单
    sum: function () {
        $('#addForm').form('submit', {
            onSubmit: function () {
                var lag = $("#addForm").form('validate');
                console.log(lag)
                if (lag == true) {
                    $.ajax({
                        url: '/ky-ykt/sysUser/saveOrUpdate',
                        type: 'POST',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data: form2Json("addForm"),
                        success: function (data) {
                            if ($("#id").val()) {
                                $.messager.show({
                                    title: '提示',
                                    msg: '修改成功'
                                })
                            } else {
                                $.messager.show({
                                    title: '提示',
                                    msg: '新增成功'
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
                } else {
                    return false;
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
    // 重置表单
    res: function () {
        $("#addForm").form('clear');

    },
    // 取消表单
    can: function () {
        $("#addBox").dialog({
            closed: true

        })

    },
    repass: function (id) {
        $.messager.confirm('提示信息', '是否重置密码', function (flag) {
            if (flag){
                $.ajax({
                    type: 'POST',
                    url: "/ky-ykt/reset/" + id,
                    beforeSend: function () {
                        $("#table").datagrid('loading');
                    },
                    success: function (data) {
                        $("#table").datagrid('reload');
                        if (data.code == 10000) {
                            $.messager.show({
                                title: '提示',
                                msg: '密码重置成功123456'
                            })
                        } else {
                            $.messager.show({
                                title: '提示',
                                msg: '密码重置失败'
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
                        url: "/ky-ykt/sysUser/deleteForce?id=" + ids.join(','),
                        beforeSend: function () {
                            $("#table").datagrid('loading');

                        },
                        success: function (data) {
                            if (data) {

                                $("#table").datagrid('reload');
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
        id = $("#table").datagrid('getSelected').id;
        $.messager.confirm('提示信息', '是否删除所选择记录', function (flg) {
            if (flg) {
                $.ajax({
                    type: 'get',
                    url: '/ky-ykt/sysUser/deleteForce?id=' + id,
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


    }
}

// 弹出框加载
$("#addBox").dialog({
    title: "新增数据",
    width: 500,
    height: 400,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})

