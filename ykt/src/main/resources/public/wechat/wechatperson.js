$(function () {
    $.ajax({
        url: '/ky-ykt/wechat/queryPerson',
        type: 'get',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            //var data = data.data;
            //console.log(data);
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
                        parent.location.href = "/web/wechatError.html";
                    }
                });
            }
        }
    })
})

//加载区县下拉框
$("#countCombo").combobox({
    url: '/ky-ykt/wechat/queryByLevel?level=2',
    method: 'get',
    valueField: 'id',
    textField: 'name',
    onChange: function (newValue, oldValue) {
        $("#townCombo").combobox({
            url: '/ky-ykt/wechat/queryByLevel?level=3&parentId=' + newValue,
            method: 'get',
            valueField: 'id',
            textField: 'name',
            onChange: function (newValue, oldValue) {
                //加载村组下拉框
                $("#villageCombo").combobox({
                    url: '/ky-ykt/wechat/queryByLevel?level=4&parentId=' + newValue,
                    method: 'get',
                    valueField: 'id',
                    textField: 'name'
                });
            }
        });
    }
});

obj = {
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
                        url: '/ky-ykt/wechat/saveOrUpdate',
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
                                    parent.location.href = "/web/wechat.html";
                                } else {
                                    $.messager.show({
                                        title: '提示',
                                        msg: '新增成功'
                                    })
                                    parent.location.href = "/web/wechat.html";
                                }
                                parent.location.href = "/web/wechat.html";
                            } else {
                                $.messager.alert('提示', data.data, 'error');
                            }

                        },
                        error: function (request) {
                            if (request.status == 401) {
                                $.messager.confirm('登录失效', '您的身份信息已过期请重新登录', function (r) {
                                    if (r) {
                                        parent.location.href = "/web/wechat.html";
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
                            parent.location.href = "/web/wechat.html";
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
}

