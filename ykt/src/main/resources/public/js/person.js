// 加载表格
function doQuery(url) {
    $("#table").datagrid({
        method: "get",
        iconCls: "icon-left02",
        url: url,
        queryParams: {flag: 2, state: 0, status: 3},
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
                field: 'openingBank',
                title: '开户行',
                width: 100,
                align: 'center'
            },
            {
                field: 'bankCardNo',
                title: '银行卡号',
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
            },
            {
                field: "opr",
                title: '操作',
                width: 120,
                align: 'center',
                formatter: function (val, row) {
                    //s = '<a  id="add" data-id="98" class=" operA"  onclick="obj.show(\'' + row.id + '\')">查看</a> ';
                    e = '<a  id="add" data-id="98" class=" operA"  onclick="obj.edit(\'' + row.id + '\')">编辑</a> ';
                    //c = '<a  id="sub" data-id="98" class=" operA"  onclick="obj.submitAudit(\'' + row.id + '\')">提交</a> ';
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
    doQuery('/ky-ykt/person/queryPage');
    doQueryProject('findProjectId');
    /*
    $.ajax({
        type: 'get',
        url: '/ky-ykt/person/getSessionRoleCode',
        success: function (data) {
            if (data != null && data != '' && data != 'undefined') {
                if (parseInt(data) == 4) {
                    $('#pltj').hide();
                }
            } else {
                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                    if (r) {
                        parent.location.href = "/login.html";
                    }
                });
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
    */
})

function doQueryProject(id) {
    $("#" + id).combobox({
        url: '/ky-ykt/project/queryByParams',
        //url: '/ky-ykt/project/queryAllProject',
        //queryParams: {flag: 2, state: 0},
        queryParams: {state: 0},
        method: 'get',
        valueField: 'id',
        textField: 'projectTypeName',
    });
}

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
                $("#showCounty").text(data.cname);
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
        doQueryProject('projectCombox');
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
                    $("#openingBank").val(data.openingBank);

                    $("#countCombo").combobox('setValue', data.county);

                    $("#townCombo").combobox('setValue', data.town);

                    $("#villageCombo").combobox('setValue', data.village);
                    $("#address").val(data.address);
                    $("#bankCardNo").val(data.bankCardNo);
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
            url: '/ky-ykt/person/import?projectId=' + $("#projectCombo").combobox("getValue"),
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
        $("#addUploadBox").dialog({
            closed: true
        });
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

function doSubmit() {
    /*  var projectId = $("#projectCombo").combobox('getValue');
      if (projectId == "" || projectId == null) {
          $.messager.alert('提示',qp "项目资金不能为空，请重新选择", 'error');
          return false;
      }*/
    $("#addUploadBox").dialog({
        closed: true
    });
    $.ajax({
        type: 'POST',
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: "/ky-ykt/person/doSubmit/",
        beforeSend: function () {
            $.messager.progress();
        },
        success: function (data) {
            console.log(data);
            $.messager.progress('close');
            $("#table").datagrid('reload');
            if (data.code == 10000) {
                $("#table").datagrid('reload');
                $.messager.show({
                    title: '提示',
                    msg: '提交成功'
                })
            } else {
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





