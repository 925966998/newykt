/**
 * Created by Administrator on 2017/11/8.
 */
// 加载combox
$("#combox").combo({
    width: '16%',
    height: 26,
    multiple: true

})
$("#sp input").click(function () {
    var text = $(this).next('span').text();
    var val = $(this).val();
    $("#combox").combo('setText', text).combo('setValue', val).combo('hidePanel');

})

obj = {
    // 查询
    find: function () {
        $("#table").datagrid('load', {
            userName: $("#user").val(),
            startTime: $.trim($("#startTime").val()),
            endTime: $.trim($("#endTime").val())
        })

    },
    look: function () {
        $("#lookTail").dialog({
            closed: false
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
                        url: "/ky-ykt/userLog/deleteMoney",
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
                    url: '/ky-ykt/userLog/deleteOne',
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
    url: '/ky-ykt/userLog/queryPage',
    fitColumns: true,
    striped: true,
    method: "GET",
    pagination: true,
    pageSize: 15,
    width: '100%',
    rownumbers: true,
    pageList: [15, 20, 50],
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
            field: 'id',
            title: '编号',
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
            field: 'ip',
            title: 'ip',
            width: 100,
            align: 'center'
        },
        {
            field: 'content',
            title: '内容',
            width: 100,
            align: 'center'
        }, {
            field: 'description',
            title: '描述',
            width: 100,
            align: 'center'
        },
        {
            field: 'module',
            title: '模块名称',
            width: 100,
            align: 'center'
        },
        {
            field: 'createTime',
            title: '时间',
            width: 100,
            align: 'center'
        }/*,
        {
            field: "opr",
            title: '操作',
            width: 100,
            align: 'center',
            formatter: function (val, row) {
                d = '<a  id="add" data-id="98" class=" operA01"  onclick="obj.delOne(\'' + row.id + '\')">删除</a> ';
                return d;
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