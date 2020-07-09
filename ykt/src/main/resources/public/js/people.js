// 加载树
function doQuery(url) {
    $("#table").datagrid({
        method: "get",
        iconCls: "icon-left02",
        url: url,
        queryParams: {flag: 2,projectType:0},
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
            },
            {
                field: "opr",
                title: '操作',
                width: 120,
                align: 'center',
                formatter: function (val, row) {
                    s = '<a  id="add" data-id="98" class=" operA"  onclick="obj.show(\'' + row.id + '\')">查看</a> ';
                    e = '<a  id="add" data-id="98" class=" operA"  onclick="obj.edit(\'' + row.id + '\')">编辑</a> ';
                    //c = '<a  id="sub" data-id="98" class=" operA"  onclick="obj.submitAudit(\'' + row.id + '\')">提交</a> ';
                    d = '<a  id="del" data-id="98" class=" operA01"  onclick="obj.delOne(\'' + row.id + '\')">删除</a> ';
                    f = '<a  id="add" data-id="98" class=" operA"  onclick="obj.replace(\'' + row.id + '\')">发放记录</a> ';
                    return s +e+ d + f;
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

$("#areaId").combotree({
    url: '/ky-ykt/areas/queryByParentId',
    method: "get",
    height: 26,
    //width: '15%',
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
    $("#projectType").combobox({
        url: '/ky-ykt/projectType/queryByParams',
        method: 'get',
        height: 26,
        width: '15%',
        valueField: 'id',
        textField: 'name',
        loadFilter: function (data) {
            var obj = {};
            obj.id = '0';
            obj.name = '请选择'
            //在数组0位置插入obj,不删除原来的元素
            data.splice(0, 0, obj)
            return data;
        }
    });
    $("#projectType").combobox('select', '0');
    // 加载表格
    doQuery('/ky-ykt/personUpload/queryPage');
    doQueryProject('findProjectId');
})

function doQueryProject(id) {
    $("#" + id).combobox({
        url: '/ky-ykt/project/queryByParams',
        method: 'get',
        data: {flag: 2},
        height: 26,
        width: '15%',
        valueField: 'id',
        textField: 'projectName',
    });

}

/*function doQueryAreas(id) {
    $("#" + id).combobox({
        url: '/ky-ykt/areas/queryAreas?level='+2,
        method: 'get',
        height: 26,
        width: '15%',
        valueField: 'id',
        textField: 'name',
        onselect:function (rec) {
            $('#findTown').combobox('setValue', "");
            $('#findVillage').combobox('setValue', "");
            var url = '/ky-ykt/areas/queryAreas?parentId=' + rec.id;//url为java后台查询事级列表的方法地址
            $('#findTown').combobox('reload', url);
        }
    });
//市区
    $('#findTown').combobox({
        valueField: 'id', //值字段
        textField: 'name', //显示的字段
        editable: false, //不可编辑，只能选择
        value: '',
        onSelect: function(rec) {
            $('#findVillage').combobox('setValue', "");
            var url = '/ky-ykt/areas/queryAreas?parentId=' + rec.id;//url为java后台查询区县级列表的方法地址
            $('#findVillage').combobox('reload', url);
        }
    });
//区 县
    $('#findVillage').combobox({
        valueField: 'id',
        textField: 'name',
        panelHeight: 'auto',
        editable: false,
    });

}*/

function doQueryDepartment(id) {
    $("#id").combobox({
        url: '/ky-ykt/department/queryByParams',
        method: 'get',
        height: 26,
        width: '15%',
        valueField: 'id',
        textField: 'departmentName'
    })

}

obj = {
    // 查询
    find: function () {
        doQuery('/ky-ykt/personUpload/queryPage?' + $("#tableFindForm").serialize())
    },
    show: function (id) {
        $("#showBox").dialog({
            closed: false
        });
        $.ajax({
            url: '/ky-ykt/personUpload/queryById?id=' + id,
            type: 'get',
            dataType: 'json',
            beforeSend: function () {
                $.messager.progress();
            },
            success: function (data) {
                $.messager.progress('close');
                var data = data.data;
                $("#showName").text(data.name);
                $("#showPhone").text(data.phone);
                $("#showGrantAmount").text(data.grantAmount);
                $("#showIdCardNo").text(data.idCardNo);
                $("#showCounty").text(data.countyName + data.townName + data.villageName);
                $("#showAddress").text(data.address);
                $('#showProjectName').text(data.projectName);
                $("#showDepartmentName").text(data.departmentName);
                $("#showBankCardNo").text(data.bankCardNo);
                $("#showOpeningBank").text(data.openingBank);
                // $('#showStatus').text(str);
                //$('#showAuditReason').text(data.auditReason);
                // $('#showPushReason').text(data.pushReason);
                //$('#showSubmitTime').text(data.submitTime);
                //$('#showAuditTime').text(data.auditTime);
                //$('#showPushTime').text(data.pushTime);

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
    //发放记录
    replace: function (id) {

        $("#replaceBox").dialog({
            closed: false
        });

        $("#replaceTable").html("");

        $.ajax({
            url: '/ky-ykt/personPeplacement/queryBypersonId?id=' + id,
            type: 'get',
            dataType: 'json',
            beforeSend: function () {
                $.messager.progress();
            },
            success: function (data) {
                console.log(data);
                $.messager.progress('close');
                for (var i = 0; i <= data.length; i++) {
                    console.log(data[i]);
                    $("#replaceTable").append('<tr><td>' + data[i].createTime + '发放了' + data[i].grantAmount + '元</td></tr>');
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

    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false

        });
        $("#addForm").form('clear');
    },
    excel: function () {
        window.location.href = '/ky-ykt/personUpload/personUploadExport?flag=2&' + $("#tableFindForm").serialize()
    },
    // 编辑
    edit: function (id) {
        $("#addBox").dialog({
            closed: false,
        });
        $("#addForm").form('clear');
        //$('#project').hide();
        //$('#department').hide()
        //doQueryProject('projectId');
        //doQueryDepartment('departId');
        console.log(id);
        $.ajax({
            url: '/ky-ykt/personUpload/queryById?id=' + id,
            type: 'get',
            dataType: 'json',
            beforeSend: function () {
                $.messager.progress();
            },
            success: function (data) {
                $.messager.progress('close');
                var data = data.data;
                console.log(data);
                if (data) {
                    $("#id").val(data.id);
                    $("#name").val(data.name);
                    $("#phone").val(data.phone);
                    $("#grantAmount").val(data.grantAmount);
                    $("#idCardNo").val(data.idCardNo);
                    $("#bankCardNo").val(data.bankCardNo);
                    //$("#grantAmount").val(data.grantAmount);
                    $("#openingBank").val(data.openingBank);
                    //$("#county").val('data.county');
                    //$("#county").combobox('setValue', data.county);
                    //$("#address").val(data.address);
                    $("#countCombo").combobox('setValue', data.county);
                    $("#townCombo").combobox('setValue', data.town);
                    $("#villageCombo").combobox('setValue', data.village);
                    $("#address").val(data.address);
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
                        url: '/ky-ykt/personUpload/saveOrUpdate',
                        type: 'POST',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data: form2Json("addForm"),
                        success: function (data) {
                            console.log($("#id").val())
                            $("#table").datagrid('reload');
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
                } else
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
    canUpload: function () {
        $("#addUploadBox").dialog({
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
                        url: "/ky-ykt/personUpload/deleteForce?id=" + ids.join(','),
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
                    url: '/ky-ykt/personUpload/deleteForce?id=' + id,
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
    subAll: function () {
        var rows = $("#table").datagrid("getSelections");
        if (rows.length > 0) {
            $.messager.confirm('确定提交审核', '你确定要提交审核你选择的记录吗？', function (flg) {
                if (flg) {
                    var ids = [];
                    for (i = 0; i < rows.length; i++) {
                        ids.push(rows[i].id);

                    }
                    var num = ids.length;
                    $.ajax({
                        type: 'POST',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        url: "/ky-ykt/personUpload/doSubmitAudit",
                        data: ids.join(","),
                        beforeSend: function () {
                            $("#table").datagrid('loading');
                        },
                        success: function (data) {
                            if (data) {
                                $("#table").datagrid('reload');
                                $.messager.show({
                                    title: '提示',
                                    msg: num + '个用户被提交'
                                })

                            } else {
                                $.messager.show({
                                    title: '警示信息',
                                    msg: "提交失败"
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
            $.messager.alert('提示', '请选择要提交的记录', 'info');
        }

    },
    upload: function () {
        $.ajax({
            type: 'post',
            url: '/ky-ykt/personUpload/import',
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
                    $.messager.alert('提示', data.data, 'error');
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

// 弹出框加载
$("#showBox").dialog({
    title: "查看",
    width: 500,
    height: 400,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})

// 弹出框加载
$("#replaceBox").dialog({
    title: "查看发放记录",
    width: 250,
    height: 300,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})

//加载区县下拉框
$("#countCombo").combobox({
    url: '/ky-ykt/areas/queryByLevel?level=2',
    method: 'get',
    valueField: 'id',
    textField: 'name',
    onChange: function (newValue, oldValue) {
        $("#townCombo").combobox({
            url: '/ky-ykt/areas/queryByLevel?level=3&parentId=' + newValue,
            method: 'get',
            valueField: 'id',
            textField: 'name',
            onChange: function (newValue, oldValue) {
                //加载村组下拉框
                $("#villageCombo").combobox({
                    url: '/ky-ykt/areas/queryByLevel?level=4&parentId=' + newValue,
                    method: 'get',
                    valueField: 'id',
                    textField: 'name'
                });
            }
        });
    }
});


