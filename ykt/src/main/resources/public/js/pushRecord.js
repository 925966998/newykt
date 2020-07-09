// 加载表格
function doQuery(url) {
    $("#table").datagrid({
        method: "get",
        iconCls: "icon-left02",
        url: url,
        queryParams: {flag: 2,state:0,record:0},
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
            /*{
                checkbox: true,
                field: 'no',
                width: 100,
                align: 'center'
            },*/
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
                field: 'grantAmount',
                title: '发放金额',
                width: 100,
                align: 'center'
            },
            {
                field: 'status',
                title: '是否提交',
                width: 100,
                align: 'center',
                formatter:function(status){
                    switch (status) {
                        case '1':  return '<div>发放成功</div>';
                        case '2':  return '<div>发放失败</div>';
                        case '3':  return '<div>未提交</div>';
                        case '4':  return '<div>已提交</div>';
                    }
                }
            },
            {
                field: "opr",
                title: '操作',
                width: 120,
                align: 'center',
                formatter: function (val, row) {
                    s = '<a  id="add" data-id="98" class=" operA"  onclick="obj.show(\'' + row.id + '\')">查看</a> ';
                   // e = '<a  id="add" data-id="98" class=" operA"  onclick="obj.edit(\'' + row.id + '\')">编辑</a> ';
                    //c = '<a  id="sub" data-id="98" class=" operA"  onclick="obj.submitAudit(\'' + row.id + '\')">提交</a> ';
                   // d = '<a  id="del" data-id="98" class=" operA01"  onclick="obj.delOne(\'' + row.id + '\')">删除</a> ';
                    return s ;
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
    doQuery('/ky-ykt/person/queryPage');
    doQueryProject('findProjectId');
})

function doQueryProject(id) {
    $("#" + id).combobox({
        url: '/ky-ykt/project/queryByParams',
        queryParams: {flag: 2},
        method: 'get',
        valueField: 'id',
        textField: 'projectName',
    });
}

/*

function doQueryDepartment(id) {
    $("#" + id).combobox({
        url: '/ky-ykt/department/queryByParams',
        data:{flag:2},
        method: 'get',
        valueField: 'id',
        textField: 'departmentName',
    })
}
*/


obj = {
    // 查询
    find: function () {
        doQuery('/ky-ykt/person/queryPage?' + $("#tableFindForm").serialize())
    },
    show: function (id) {
        $("#showBox").dialog({
            closed: false
        });
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
                $("#showName").text(data.name);
                $("#showPhone").text(data.phone);
                $("#showGrantAmount").text(data.grantAmount);
                $("#showIdCardNo").text(data.idCardNo);
                $("#showCounty").text(data.county);
                $("#showAddress").text(data.address);
                $('#showProjectName').text(data.projectName);
                $("#showDepartmentName").text(data.departmentName);
                $("#showBankCardNo").text(data.bankCardNo);
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
    //提交审核
    submitAudit: function (id) {
        $.messager.confirm('是否提交', '确定要提交审核吗?', function (r) {
            if (r) {
                $.ajax({
                    url: '/ky-ykt/person/doSubmitAudit',
                    type: 'post',
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    data: id,
                    beforeSend: function () {
                        $.messager.progress();
                    },
                    success: function (data) {
                        $.messager.progress('close');
                        $("#table").datagrid('reload')
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
        });
    },
    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false

        });
        $("#addForm").form('clear');
        doQueryProject('projectId');
        $('#project').show();
        $('#department').show();
    },
    // 弹出上传下拉框
    uploadBox: function () {
        $("#addUploadBox").dialog({
            closed: false
        });
        $("#addUploadBox").form('clear');
        doQueryProject('projectCombo');
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
                    $("#id").val(data.id);
                    $("#name").val(data.name);
                    $("#phone").val(data.phone);
                    $("#grantAmount").val(data.grantAmount);
                    $("#idCardNo").val(data.idCardNo);

                    $("#countCombo").combobox('setValue', data.county);
                    $("#address").val(data.address);
                    $("#bankCardNo").val(data.bankCardNo);
                   /* $("#projectId").combobox('setValue', data.projectId);
                    $("#departmentId").combobox('setValue', data.departmentId);*/
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
                console.log("==============================");
                console.log($("#id").val());
                if (lag == true) {
                    console.log($('#addForm').serialize())
                    console.log(form2Json("addForm"))
                    $.ajax({
                        url: '/ky-ykt/person/saveOrUpdate',
                        type: 'POST',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data: form2Json("addForm"),
                        success: function (data) {
                            console.log($("#id").val());
                            $("#table").datagrid('reload');
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
                                $.messager.alert('提示', data.data, 'error');
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

    // 身份证校验
    checkIdCard: function () {
        $.ajax({
            url: '/ky-ykt/person/checkIdCard',
            type: 'get',
            contentType: "application/json; charset=utf-8",
            data: {"idCardNo": $("#idCardNo").val()},
            beforeSend: function () {
                $.messager.progress();
            },
            success: function (data) {
                $.messager.progress('close');
                if (data) {
                    $.messager.alert('提示', '此身份证号已存在，请重新输入', 'error');
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
                        url: "/ky-ykt/person/deleteForce?id=" + ids.join(','),
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
    upload: function () {
        $.ajax({
            type: 'post',
            url: '/ky-ykt/person/import',
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

//上传人员信息
$("#personupload").click(function () {
    //弹出框
    $("#addUploadBox").dialog({
        closed: true,
    })
    var lag = $(this).form('validate');
    if (lag == true) {
        $.ajax({
            type: 'post',
            url: '/ky-ykt/person/import?' + $("#uploadDialogForm").serialize(),
            processData: false,
            cache: false,
            contentType: false,
            data: new FormData($('#uploadForm')[0]),
            beforeSend: function () {
                $.messager.progress();
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
    } else
        return false;
})

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
$("#addUploadBox").dialog({
    title: "确认上传",
    width: 300,
    height: 200,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})
/*

//加载区县下拉框
$("#countCombo").combobox({
    url: '/ky-ykt/areas/queryByCounty',
    method: 'get',
    valueField: 'id',
    textField: 'cname',

})
*/

$("#comsub").click(function () {
    $.ajax({
        type: 'POST',
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: "/ky-ykt/person/doSubmit/" + $("#projectCombo").combobox('getValue'),
        beforeSend: function () {
            //$("#addUploadBox").datagrid('close');
            $("#table").datagrid('loading');
        },
        success: function (data) {
            if (data) {
                $("#addUploadBox").datagrid('close');
                $.messager.progress('close');
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





