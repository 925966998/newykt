/**
 * Created by Administrator on 2017/11/8.
 */
obj = {
    // 查询
    find: function () {
        $("#table").datagrid('load', {
            roleName: $("#roleNameSearch").val(),
        })

    },
    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false

        });
        $("#roleForm").form('clear');
        queryTree(null);
    },
    // 编辑
    edit: function (id) {
        $("#addBox").dialog({
            closed: false,
        })
        $.ajax({
            url: '/ky-ykt/role/select',
            type: 'get',
            dataType: 'json',
            data: {id: id},
            success: function (res) {
                if (res != null) {
                    $('#roleForm').form('load', {
                        roleName: res.roleName,
                        roleCode: res.roleCode,
                        note: res.note,
                        id: id,
                    })
                    queryTree(id)
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
        $("#roleForm").form('load', {});


    },
    sum: function () {
        $("#roleForm").form('submit', {
            url: "/ky-ykt/role/saveOrUpdate",
            onSubmit: function () {
                return $(this).form('validate')

            },
            beforeSend: function () {
                $.messager.progress();
            },
            success: function (data) {
                data = JSON.parse(data);
                var roleId = $("#id").val();
                if (!roleId) {
                    roleId = data.data;
                }
                if (data.code = '10000') {
                    $("#table").datagrid('reload');
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存成功'
                    });
                    $.ajax({
                        url: '/ky-ykt/roleMenu/save?roleId=' + roleId,
                        type: 'POST',
                        dataType: 'json',
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify($('#tree').tree('getChecked', ['checked', 'indeterminate'])),
                        beforeSend: function () {
                            $.messager.progress();
                        },
                        success: function () {
                            $.messager.progress('close');
                        }, error: function (request) {
                            $.messager.progress('close');
                            $.messager.show({
                                title: '提示',
                                msg: '权限保存失败'
                            })
                        }
                    });
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '信息保存失败'
                    })
                }
                $("#addBox").dialog({
                    closed: true

                })
                $.messager.progress('close');
            },
            error: function (request) {
                $.messager.progress('close');
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
    reset: function () {
        $("#roleForm").form('clear');
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
                        url: "/ky-ykt/role/deleteMoney",
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
                    url: '/ky-ykt/role/deleteOne',
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
    url: '/ky-ykt/role/queryPage',
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
            field: 'roleCode',
            title: '编号',
            width: 100,
            align: 'center'


        },
        {
            field: 'roleName',
            title: '角色名称',
            width: 100,
            align: 'center'


        },
        {
            field: 'note',
            title: '角色描述',
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
    title: "角色信息",
    width: '80%',
    height: '80%',
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})

function queryTree(id) {
    $('#tree').tree({
        url: "/ky-ykt/menu/menuTree/" + id,
        method: "get",
        animate: true,
        checkbox: true,
        lines: true
    });
}