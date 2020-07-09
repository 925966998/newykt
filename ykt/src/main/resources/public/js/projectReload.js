function doQueryProject(id) {
    $("#" + id).combobox({
        url: '/ky-ykt/project/queryByParams',
        queryParams: {flag: 2, status: 0},
        method: 'get',
        valueField: 'id',
        textField: 'projectName',

    });
}
// 弹出框加载
$("#addUploadBox").dialog({
    title: "再次发放",
    width: 300,
    height: 200,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
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
    canUpload: function () {
        $("#addUploadBox").dialog({
            closed: true

        })

    },
    look: function () {
        $("#lookTail").dialog({
            closed: false
        })

    },
    reloadPull: function () {
        $.ajax({
            type: 'get',
            url: "/ky-ykt/person/reloadPull",
            data: {
                projectId:getUrlParam('projectId'),
                newProjectId:$("#projectCombo").combobox("getValue")
            },
            success: function (data) {
                if(data.code==10000){
                    $.messager.show({
                        title: '再次发放',
                        msg: "再次发放成功，请发放录入进行提交"
                    })
                    $("#addUploadBox").dialog({
                        closed: true

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
    },
    uploadBox: function () {
        $("#addUploadBox").dialog({
            closed: false
        });
        $("#addUploadBox").form('clear');
        doQueryProject('projectCombo');
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
    method: "get",
    iconCls: "icon-left02",
    url: '/ky-ykt/person/queryPage',
    queryParams: {flag: 2, status: 1, projectId: getUrlParam('projectId')},
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
            field: 'grantAmount',
            title: '发放金额',
            width: 100,
            align: 'center'
        },
        {
            field: 'countyName',
            title: '所属区县',
            width: 100,
            align: 'center'
        },
        {
            field: 'townName',
            title: '所属乡镇',
            width: 100,
            align: 'center'
        },
        {
            field: 'villageName',
            title: '所属村组',
            width: 100,
            align: 'center'
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
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}