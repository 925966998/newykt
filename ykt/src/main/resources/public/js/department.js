/**
 * Created by Administrator on 2017/11/8.
 */
// 加载combox
$("#combox").combo({
    width: '16%',
    height: 26,
    multiple: true

});
//区域树
$("#areaId").combotree({
    url: '/ky-ykt/areas/queryByParentId',
    method: "get",
    height: 26,
    width: '70%',
    valueField: 'id',
    //state:'closed',
    textField: 'text',
    onSelect: function () {
        var t = $("#areaId").combotree('tree');
        var n = t.tree('getSelected');
        var text = n.id;
        $("#areaId").combotree('setValue', text);
    },
    //默认树节点是关闭状态
    onLoadSuccess: function () {
        $("#areaId").combotree('tree').tree("collapseAll");
    },
})

$(function () {
    doqueryParentId();
    $("#departmentTree").tree({
// 左侧tree部分的数据
        url: "/ky-ykt/department/queryByParentId",
        method: "get",
        onClick: function (node) {
            console.log(node)
            if (node.id == null || node.id == 'null' || node.id == 'undefined') {
                $("#table").datagrid('load', {})
                // $("#parentId").combobox('setValue', '');
                $("#parentId").combotree('setValue', '');
            } else {
                $("#table").datagrid('load', {
                    parentId: node.id,
                })
                $("#parentId").combotree('setValue', node.id);
                // $("#parentId").combobox('setValue', node.id);
            }

        }
    });
});
$("#sp input").click(function () {
    var text = $(this).next('span').text();
    var val = $(this).val();
    $("#combox").combo('setText', text).combo('setValue', val).combo('hidePanel');

})

// 加载详情页面部门下拉框
function doqueryParentId(){
    $("#parentId").combotree({
        url: '/ky-ykt/department/queryByParentId',
        method: "get",
        height: 26,
        width: '70%',
        valueField: 'id',
        textField: 'text',
        onSelect: function () {
            var t = $("#parentId").combotree('tree');
            var n = t.tree('getSelected');
            var text = n.id;
            $("#parentId").combotree('setValue', text);
        }
    })
}

// $("#parentId").combobox({
//     url: '/ky-ykt/department/queryByParams',
//     method: 'get',
//     height: 26,
//     width: '70%',
//     valueField: 'id',
//     textField: 'departmentName'
// })
$("#isUse").combobox({
    data: [{'value': '0', 'text': '是'}, {'value': '1', 'text': '否'}],
    valueField: 'value',
    textField: 'text',
    height: 26,
    width: '70%',
});

obj = {

    // 查询
    find: function () {
        $("#table").datagrid('load', {
            departmentName: $("#departmentNameSearch").val(),
            departmentNum: $.trim($("#departmentNumSearch").val())
        })

    },
    // 添加
    addBox: function () {
        $("#addForm").form('clear');
        $("#id").val("");
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
            url: '/ky-ykt/department/select',
            type: 'get',
            dataType: 'json',
            data: {id: id},
            success: function (res) {
                if (res != null) {
                    $('#addForm').form('load', {
                        departmentName: res.departmentName,
                        departmentNum: res.departmentNum,
                        note: res.note,
                        id: id,
                        areaId:res.areaId,
                    })
                    $("#isUse").combobox('setValue', res.isUse);
                    if (res.parentId != "0") {
                        $("#parentId").combotree('setValue', res.parentId);
                        // $("#parentId").combobox('setValue', res.parentId);
                    } else {
                        $("#parentId").combotree('setValue','');
                    }

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
            url: "/ky-ykt/department/saveOrUpdate",
            method: "post",
            onSubmit: function () {
                return $(this).form('validate')

            },
            success: function (data) {
                // var departmentName = $("#departmentName").val();
                // var departmentNum = $("#departmentNum").val();
                // var note = $("#note").val();
                // var parentId = $("#parentId").combotree('getValue');
                // var isUse = $("#isUse").combobox('getValue');
                // $("#table").datagrid('insertRow', {
                //     index: 1,
                //     row: {
                //         departmentName: departmentName,
                //         departmentNum: departmentNum,
                //         note: note,
                //         parentId: parentId,
                //         isUse: isUse
                //     }
                // })
                if (data.code = '10000') {
                    doqueryParentId();
                    $("#table").datagrid('loaded');
                    $("#table").datagrid('load');
                    $("#addBox").dialog({
                        closed: true

                    })
                    $('#departmentTree').tree('reload');
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
                        url: "/ky-ykt/department/deleteMoney",
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
                                $('#departmentTree').tree('reload');
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
                    url: '/ky-ykt/department/deleteOne',
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
                            $('#departmentTree').tree('reload');
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
    url: '/ky-ykt/department/queryPage',
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
            field: 'departmentNum',
            title: '部门编号',
            width: 100,
            align: 'center',
        },
        {
            field: 'departmentName',
            title: '部门名称',
            width: 100,
            align: 'center'
        },
        {
            field: 'isUse',
            title: '是否启用',
            width: 100,
            align: 'center',
            formatter: function (value, row, index) {
                if (value == 0) {
                    return '是'
                } else if (value == 1) {
                    return '否'
                }
            }
        },
        {
            field: 'note',
            title: '备注说明',
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
    title: "信息内容",
    width: 500,
    height: 300,
    closed: true,
    modal: true,
    shadow: true
})