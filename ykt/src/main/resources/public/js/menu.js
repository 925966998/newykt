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
                field: 'menuName',
                title: '菜单名称',
                width: 100,
                align: 'center'
            },
            {
                field: 'menuUrl',
                title: '菜单链接',
                width: 100,
                align: 'center'
            },
            {
                field: 'menuIcon',
                title: '菜单图标',
                width: 100,
                align: 'center'
            },
            {
                field: 'menuClass',
                title: '菜单样式',
                width: 100,
                align: 'center'
            },
            {
                field: 'isFirstMenu',
                title: '是否一级菜单',
                width: 100,
                align: 'center',
                formatter: function (val, row) {
                    if (val == 0) return '否'
                    else return '是'
                }
            },
            {
                field: 'menuSort',
                title: '菜单顺序',
                width: 100,
                align: 'center'
            },
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
    doQuery('/ky-ykt/menu/queryPage');
})

function doQueryMenuLevel() {
    $("#parentIdCombo").combobox({
        url: '/ky-ykt/menu/queryByParams?isFirstMenu=1',
        method: 'get',
        valueField: 'id',
        textField: 'menuName',
        onChange: function (newValue, oldValue) {
            queryCurMostSort(newValue);
        }
    });

}

function queryCurMostSort(param) {
    var url = "";
    if (typeof param == "number") {
        url = '/ky-ykt/menu/queryCurMostSort?isFirstMenu=' + param;
    } else {
        url = '/ky-ykt/menu/queryCurMostSort?parentId=' + param;
    }
    $.ajax({
        url: url,
        type: 'get',
        success: function (data) {
            $("#menuSort").val(data + 1);
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
        doQuery('/ky-ykt/menu/queryPage?' + $("#tableFindForm").serialize())
    },
    hideOrShowSomeTab: function (val) {
        $("#displayMenuName").hide();
        $("#displayParentId").hide();
        if (val == 1) {
            $("#displayMenuName").hide();
            $("#displayParentId").hide();
            queryCurMostSort(1);
        } else {
            $("#displayMenuName").show();
            $("#displayParentId").show();
            $("#menuSort").val("");
        }
    },
    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false

        });
        $("#addForm").form('clear');
        $("input[name='isFirstMenu'][value='0']").attr("checked", true);
        doQueryMenuLevel();
        //queryCurMostSort(1);
    },
    // 编辑
    edit: function (id) {
        $("#addBox").dialog({
            closed: false,
        });
        $("#addForm").form('clear');
        doQueryMenuLevel();
        $.ajax({
            url: '/ky-ykt/menu/queryById?id=' + id,
            type: 'get',
            dataType: 'json',
            beforeSend: function () {
                $.messager.progress();
            },
            success: function (data) {
                $.messager.progress('close');
                var data = data.data;
                if (data) {
                    if (data.isFirstMenu == 0) {
                        $("#displayMenuName").show()
                        $("#displayParentId").show()
                    } else {
                        $("#displayMenuName").hide()
                        $("#displayParentId").hide()

                    }
                    $("#id").val(id);
                    $("input[name='isFirstMenu'][value=" + data.isFirstMenu + "]").attr("checked", true);
                    $("#menuName").val(data.menuName);
                    $("#menuIcon").val(data.menuIcon);
                    $("#menuUrl").val(data.menuUrl);
                    $("#menuClass").val(data.menuClass);
                    $('#parentIdCombo').combobox('setValues', data.parentId);
                    $("#menuSort").val(data.menuSort);
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
                        url: '/ky-ykt/menu/saveOrUpdate',
                        type: 'POST',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data: form2Json("addForm"),
                        success: function (data) {
                            $("#table").datagrid('reload')
                            if (data.code == 10000) {
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
                            } else {
                                $.messager.show({
                                    title: '提示',
                                    msg: data.data
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
        $("#addForm").form('reset');

    },
    // 取消表单
    can: function () {
        $("#addBox").dialog({
            closed: true

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
                        url: "/ky-ykt/menu/deleteForce?id=" + ids.join(','),
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
                    url: '/ky-ykt/menu/deleteForce?id=' + id,
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
