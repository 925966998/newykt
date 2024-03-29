/**
 * Created by Administrator on 2017/11/8.
 */
$("#department").combobox({
    url: '/ky-ykt/department/queryByParams',
    method: 'get',
    height: 26,
    width: '70%',
    valueField: 'id',
    textField: 'departmentName'
})
$("#projectTypeParent").combobox({
    url: '/ky-ykt/projectTypeParent/queryByParams',
    method: 'get',
    height: 26,
    width: '70%',
    valueField: 'id',
    textField: 'projectTypeName'
})
$("#type").combobox({
    data: [{'value': '1', 'text': '个人类'}, {'value': '2', 'text': '项目类'}],
    valueField: 'value',
    textField: 'text',
    height: 26,
    width: '70%',
});
obj = {

    // 查询
    find: function () {
        $("#table").datagrid('load', {
            name: $("#nameSearch").val()
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
            url: '/ky-ykt/projectType/select',
            type: 'get',
            dataType: 'json',
            data: {id: id},
            success: function (res) {
                if (res != null) {
                    $('#addForm').form('load', {
                        name: res.name,
                        note: res.note,
                        policyBasis: res.policyBasis,
                        payFlag: res.payFlag,
                        payTarget: res.payTarget,
                        id: id,
                    })
                    $("#department").combobox('setValue', res.department);
                    $("#projectTypeParent").combobox('setValue', res.projectTypeParent);
                    $("#type").combobox('setValue', res.type);
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
            url: "/ky-ykt/projectType/saveOrUpdate",
            method: "post",
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
                        url: "/ky-ykt/projectType/deleteMoney",
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
                    url: '/ky-ykt/projectType/deleteOne',
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
    url: '/ky-ykt/projectType/queryPage',
    fitColumns: true,
    striped: true,
    method: "GET",
    pagination: true,
    pageSize: 20,
    width: '100%',
    rownumbers: true,
    //pageList: [10, 20],
    pageNumber: 1,
    nowrap: false,
    height: 'auto',
    sortName: 'id',
    checkOnSelect: true,
    sortOrder: 'asc',
    toolbar: '#tabelBut',
    columns: [[
        /*{checkbox: true, field: 'no', width: 100, align: 'center'},*/
        {field: 'name', title: '补助项目名称', width: 100, align: 'center',},
        /*{field: 'projectTypeParentName',title: '项目类别',width: 100,align: 'center'},
        {field: 'type',title: '资金类型',width: 100,align: 'center',
            formatter: function (value, row, index) {
                if (value == 1) {return "个人类";}
                if (value == 2) {return "项目类";}
            }
        },
        */
        {field: 'policyBasis', title: '政策依据', width: 100, align: 'center',},
        {field: 'payFlag', title: '发放标准', width: 100, align: 'center',},
        {field: 'payTarget', title: '发放对象', width: 100, align: 'center',},
        {field: 'departmentName', title: '所属部门', width: 100, align: 'center',},
        {field: 'note', title: '备注说明', width: 100, align: 'center'},
        {field: "opr", title: '操作', width: 100, align: 'center',
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
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})