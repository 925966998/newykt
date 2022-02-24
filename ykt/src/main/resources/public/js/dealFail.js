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
        nowrap: false,
        height: 'auto',
        sortName: 'id',
        //checkOnSelect: true,
        //sortOrder: 'asc',
        toolbar: '#tabelBut',
        columns: [[
            {field: 'name', title: '姓名', width: 100, align: 'center'},
            /*{field: 'phone', title: '手机号', width: 100, align: 'center'},*/
            {field: 'idCardNo', title: '身份证号', width: 100, align: 'center'},
            {field: 'openingBank', title: '开户行', width: 100, align: 'center'},
            {field: 'bankCardNo', title: '社保卡号', width: 100, align: 'center'},
            {field: 'projectName', title: '项目资金名称', width: 100, align: 'center'},
            {field: 'grantAmount', title: '发放金额', width: 100, align: 'center',
                formatter: function (val, row) {if (val == 0) {return '0.00';} else {return toMoney(val);}},
            },
            {field: 'departmentName', title: '发放部门', width: 100, align: 'center'},
            {field: 'status', title: '状态', width: 100, align: 'center',
                formatter: function (val, row) {
                    if (val == '0')
                        return '未发放';
                    if (val == '1')
                        return '发放成功';
                    if (val == '2')
                        return '发放失败';
                }
            },
            {field: 'failReason', title: '失败原因', width: 100, align: 'center'},
            {field: "opr", title: '操作', width: 100, align: 'center',
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
    $("#projectType").combobox({
        url: '/ky-ykt/project/queryByParams',
        queryParams: {state: 0},
        method: 'get',
        height: 26,
        width: '15%',
        valueField: 'id',
        textField: 'projectTypeName',
        loadFilter: function (data) {
            var obj = {};
            obj.id = '0';
            obj.name = '请选择'
            //在数组0位置插入obj,不删除原来的元素
            data.splice(0, 0, obj)
            return data;
        }
    });
    //$("#projectType").combobox('select', '0');
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
                    $("#projectId").val(data.projectId);
                    $("#departmentId").val(data.departmentId);
                    $("#phone").val(data.phone);
                    $("#grantAmount").val(data.grantAmount);
                    $("#idCardNo").val(data.idCardNo);
                    $("#bankCardNo").val(data.bankCardNo);
                    /*$("#county").val(data.countyName);*/
                   /* $("#town").val(data.townName);*/
                    $("#county").combobox('setValue', data.county);

                    $("#town").combobox('setValue', data.town);
                    $("#village").combobox('setValue', data.village);
                    /*$("#village").val(data.villageName);*/
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
                        type: 'POST',
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
                }else
                    return false;
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
    // 弹出提交下拉框
    uploadBox: function () {
        $("#addUploadBox").dialog({
            closed: false
        });
        $("#addUploadBox").form('clear');
        doQueryProject('projectCombo');
    },
    canUpload: function () {
        $("#addUploadBox").dialog({
            closed: true
        })
    },
    //提交多个
    reSubmitAudit: function () {
        $.messager.confirm('确定提交', '你确定再次提交？', function (flg) {
            if (flg) {
                console.log($("#projectCombo").combobox("getValue"));
                if($("#projectCombo").combobox("getValue") != ""){
                    $.ajax({
                        type: 'get',
                        url: "/ky-ykt/person/doSubmitAudit?projectTypeId=" + $("#projectCombo").combobox("getValue"),
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
                                    msg: '提交成功'
                                })

                            } else {
                                $.messager.show({
                                    title: '警示信息',
                                    msg: "提交失败"
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
                }else{
                    $.messager.show({
                        title: '警示信息',
                        msg: "项目资金不能为空"
                    })
                }
            }
        })
        $("#addUploadBox").dialog({
            closed: true
        });


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
$("#addBox").dialog({
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

//加载区县下拉框
$("#county").combobox({
    url: '/ky-ykt/areas/queryByLevel?level=2',
    method: 'get',
    valueField: 'id',
    textField: 'name',
    onChange: function (newValue, oldValue) {
        $("#town").combobox({
            url: '/ky-ykt/areas/queryByLevel?level=3&parentId=' + newValue,
            method: 'get',
            valueField: 'id',
            textField: 'name',
            onChange: function (newValue, oldValue) {
                //加载村组下拉框
                $("#village").combobox({
                    url: '/ky-ykt/areas/queryByLevel?level=4&parentId=' + newValue,
                    method: 'get',
                    valueField: 'id',
                    textField: 'name'
                });
            }
        });
    }
});

// 弹出框加载
$("#addUploadBox").dialog({
    title: "确认提交",
    width: 480,
    height: 140,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})

function doQueryProject(id) {
    $("#" + id).combobox({
        url: '/ky-ykt/userProjectType/queryProject',
        //url: '/ky-ykt/project/queryAllProject',
        //queryParams: {flag: 2, state: 0},
        method: 'get',
        valueField: 'id',
        textField: 'projectTypeName',
    });
}