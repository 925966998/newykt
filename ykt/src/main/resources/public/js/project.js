$("#paymentDepartment").combobox({
    url: '/ky-ykt/department/queryByParams',
    method: 'get',
    height: 26,
    width: '70%',
    valueField: 'id',
    textField: 'departmentName'
})
$("#projectSourceId").combobox({
    url: '/ky-ykt/projectSource/queryByParams',
    method: 'get',
    height: 26,
    width: '70%',
    valueField: 'id',
    textField: 'projectName'
})
$("#projectName").combobox({
    url: '/ky-ykt/projectType/queryByParams',
    method: 'get',
    height: 26,
    width: '70%',
    valueField: 'id',
    textField: 'name'
});
$(function () {
    //上传
    $('#file').ace_file_input({
        no_file: '请选择EXCEL ...',
        btn_choose: '选择',
        btn_change: '更改',
        droppable: true,
        // onchange:null,
        // thumbnail:false, //| true | large
        //onchange:''
        //

    }).on('file.error.ace', function (event, info) {//不匹配上面的文件格式就会跳出弹框提示
        alert("选择Excel格式的文件导入！");
    });

});

function fileupload(qbz) {
    var fileType = qbz.value.substr(qbz.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
    $.ajax({
        type: 'post',
        url: '/ky-ykt/project/upload',
        processData: false,
        cache: false,
        contentType: false,
        data: new FormData($('#uploadForm')[0]),
        beforeSend: function () {
            $.messager.progress();
        },
        success: function (data) {
            $.messager.progress('close');
            $('#fileId').val(data);

        },
    })

}

obj = {
    // 查询
    find: function () {
        $("#table").datagrid('load', {
            flag: 1,
            projectName: $("#projectNameSearch").val()
        })

    },
    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false
        });
        $("#addForm").form('clear');
    },
    // 编辑
    edit: function (id) {
        var ID;
        $("#addBox").dialog({
            closed: false
        })
        $.ajax({
            url: '/ky-ykt/project/select',
            type: 'get',
            dataType: 'json',
            data: {id: id},
            success: function (res) {
                if (res != null) {
                    $('#addForm').form('load', {
                        projectName: res.projectName,
                        totalAmount: res.totalAmount,
                        batchNumber: res.batchNumber,
                        id: id,
                        documentNum: res.documentNum,
                        centerAmount: res.centerAmount,
                        provinceAmount: res.provinceAmount,
                        cityAmount: res.cityAmount,
                        countyAmount: res.countyAmount,

                    })
                    $("#projectSourceId").combobox('setValue', res.projectSourceId);
                    $("#projectName").combobox('setValue', res.projectType);
                    if (res.paymentDepartment != "0") {
                        $("#paymentDepartment").combobox('setValue', res.paymentDepartment);
                    } else {
                        $("#paymentDepartment").combobox('setValue', '');
                    }
                    $('#startTime').datetimebox('setValue', new Date(res.startTime).Format("yyyy-MM-dd HH:mm"))
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

    // 乡镇分配
    detail: function (id) {
        $("#detailForm").empty();
        var rows = $("#table").datagrid("getSelections");
        if (rows.length > 0) {
            $("#detailBox").dialog({
                closed: false
            });
            $.ajax({
                url: '/ky-ykt/areas/queryByLevel',
                type: 'get',
                dataType: 'json',
                data: {level: 3},
                success:function (res) {
                    //console.log(res);
                    for (var i = 0; i < res.length; i++) {
                        $("#detailForm").append("<div class='formDiv'><label>"+res[i].name+':'+"</label> " +
                            "<input style='width: 60%;height: 20px; border: 1px solid #95B8E7; " +
                            "data-options='min:0,precision:2' name='areaAmount' " +
                            "type='text' id='"+res[i].id+"' >");
                        var newline= document.createElement("br");
                        $("#detailForm").append(newline);
                    }
                    getMx(rows[0].id);
                }
            })
            $("#projectId").val(rows[0].id);
        } else {
            $.messager.alert('提示', '请选择一条项目记录', 'info');
        }
    },
    upstate: function () {
        var rows = $("#table").datagrid("getSelections");
        if (rows.length > 0) {
            $.messager.confirm('确定删除', '你确定要选择的记录发放完成吗？', function (flg) {
                if (flg) {
                    var ids = [];
                    for (i = 0; i < rows.length; i++) {
                        ids.push(rows[i].id);
                    }
                    var num = ids.length;
                    $.ajax({
                        type: 'GET',
                        url: '/ky-ykt/project/upstate',
                        data: {
                            ids: ids.join(',')
                        },
                        beforesend: function () {
                            $("#table").datagrid('loading');

                        },
                        success: function (data) {
                            if (data) {

                                $("#table").datagrid('loaded');
                                $("#table").datagrid('load');
                                $("#table").datagrid('unselectAll');
                                $.messager.show({
                                    title: '提示',
                                    msg: '发放完成'
                                })

                            } else {
                                $.messager.show({
                                    title: '警示信息',
                                    msg: "信息删除失败"
                                })

                            }

                        }
                    })
                }

            })

        } else {
            $.messager.alert('提示', '请选择发放完成记录', 'info');
        }
    },
    look: function (id) {
        $("#lookTail").dialog({
            closed: false
        })
        $.ajax({
            url: '/ky-ykt/projectDetail/queryByParams',
            type: 'get',
            dataType: 'json',
            data: {projectId: id},
            success: function (res) {
                if (res != null) {
                    var rows = res;
                    var topstr = '';
                    var downstr = '';
                    for (var i = 0; i < rows.length; i++) {
                        topstr += '<div class="lookTailDiv01">' + new Date(rows[i].startTime).Format("yyyy-MM-dd") + '</div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="circleStyle01"></span>\n' +
                            '        </div>\n' +
                            '\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="lineH magingLeft"></span>\n' +
                            '        </div>\n' +
                            '\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="lookTailDiv02">*</div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="circleStyle02 backGreen "></span>\n' +
                            '        </div>\n' +
                            '\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="lineH magingLeft"></span>\n' +
                            '        </div>\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="lookTailDiv02">*</div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="circleStyle02 backRed "></span>\n' +
                            '        </div>\n' +
                            '        <div class="clear"></div>\n' +
                            '        <div class="floatRight maginRt">\n' +
                            '            <span class="lineH magingLeft"></span>\n' +
                            '        </div><div class="clear"></div>';

                        if (i > 0) {
                            downstr += '       <div class="clear"></div>\n<div class="divBorder magintTop03 floatLeft borderGreen"><span class="footBlood">发放到:' + rows[i].departmentName + '--发放金额:' + rows[i].paymentAmount + '</span>';
                            if (rows[i].remark != null && rows[i].remark != '') {
                                downstr += '<span class="footBlood">— 描述：' + rows[i].remark + '</span></div></div>\n';
                            } else {
                                downstr += '</div>\n';
                            }
                            downstr += '        <div class="clear"></div>\n' +
                                '        <div class="divBorder magintTop02  floatLeft borderGreen"><span class="footBlood">已发放总金额：' + rows[i].totalAmount + '|剩余金额：' + rows[i].surplusAmount;
                            if (rows[i].reason != null && rows[i].reason != '') {
                                downstr += '-此次未发放原因：' + rows[i].reason + '</span></div>\n'
                            } else {
                                downstr += '</span></div>\n';
                            }
                        } else {
                            downstr += '       <div class="clear"></div>\n<div class="divBorder magintTop01 floatLeft borderGreen"><span class="footBlood">发放到:' + rows[i].departmentName + '--发放金额:' + rows[i].paymentAmount + '</span>';
                            if (rows[i].remark != null && rows[i].remark != '') {
                                downstr += '<span class="footBlood">— 描述：' + rows[i].remark + '</span></div></div>\n';
                            } else {
                                downstr += '</div>\n';
                            }
                            downstr += '        <div class="clear"></div>\n' +
                                '        <div class="divBorder magintTop02  floatLeft borderGreen"><span class="footBlood">已发放总金额：' + rows[i].totalAmount + '|剩余金额：' + rows[i].surplusAmount;
                            if (rows[i].reason != null && rows[i].reason != '') {
                                downstr += '-此次未发放原因：' + rows[i].reason + '</span></div>\n'
                            } else {
                                downstr += '</span></div>\n';
                            }
                        }
                    }
                    $('#topDetail').empty();
                    $('#downDetail').empty();
                    $('#topDetail').append(topstr);
                    $('#downDetail').append(downstr);
                } else {
                    $.messager.show({
                        title: '提示',
                        msg: '查询失败'

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
    sum: function () {
        $("#addForm").form('submit', {
            url: "/ky-ykt/project/saveOrUpdate",
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
    detailSum: function () {
        var id = $("#projectId").val();
        //console.log(id);
        var areaAmountLength = document.getElementsByName("areaAmount").length;
        //console.log(areaAmountLength);
        //读取文本中的值
        var areaAmountList = new Array();
        for (var i = 0; i < areaAmountLength; i++) {
            var a = document.getElementsByName("areaAmount")[i].value;//根据name获得对象中的值
            //console.log(a);
            areaAmountList.push({areaId: i, areaAmount: a})
        }
        //console.log(JSON.stringify(areaAmountList));
        $.ajax({
            url: "/ky-ykt/project/saveProjectAreas?projectId=" + id,
            type: "post",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(areaAmountList),
            dataType: "json",
            success: function (data) {
                if (data.code = '10000') {
                    $("#detailBox").dialog({
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
    // 重置表单
    res: function () {
        $("#addForm").form('clear');

    },
    detailRes: function () {
        $("#detailForm").form('clear');

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
                        type: 'GET',
                        url: "/ky-ykt/project/deleteMoney",
                        data: {
                            ids: ids.join(',')
                        },
                        beforesend: function () {
                            $("#table").datagrid('loading');

                        },
                        success: function (data) {
                            if (data) {

                                $("#table").datagrid('loaded');
                                $("#table").datagrid('load');
                                $("#table").datagrid('unselectAll');
                                $.messager.show({
                                    title: '提示',
                                    msg: num + '条记录被删除'
                                })

                            } else {
                                $.messager.show({
                                    title: '警示信息',
                                    msg: "信息删除失败"
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
        id = $("#table").datagrid('getSelected').id;
        $.messager.confirm('提示信息', '是否删除所选择记录', function (flg) {
            if (flg) {
                $.ajax({
                    type: 'post',
                    url: '',
                    data: {
                        ID: id
                    },
                    beforesend: function () {
                        $("#table").datagrid('loading');

                    },
                    success: function (data) {
                        if (data) {
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
    url: '/ky-ykt/project/queryPage',
    fitColumns: true,
    striped: true,
    queryParams: {flag: 1},
    pagination: true,
    pageSize: 10,
    method: "GET",
    width: '100%',
    rownumbers: true,
    pageList: [10, 20],
    pageNumber: 1,
    nowrap: false,
    singleSelect: true,
    height: 'auto',
    sortName: 'id',
    checkOnSelect: true,
    sortOrder: 'asc',
    toolbar: '#tabelBut',
    columns: [[
        /*{checkbox: true, field: 'no', width: 100, align: 'center'},*/
        {field: 'projectTypeName', title: '项目名称', width: 100, align: 'center'},
        {field: 'projectSourceName', title: '资金来源', width: 100, align: 'center'},
        {field: 'documentNum', title: '文号', width: 100, align: 'center'},
        {field: 'departmentName', title: '所属单位', width: 100, align: 'center'},
        {field: 'startTime', title: '开始发放时间', width: 100, align: 'center',
            formatter: function (value, row, index) {
                if (value != null) {return new Date(value).Format("yyyy-MM-dd HH:mm")}}
        },
        {field: 'totalAmount', title: '总金额', width: 100, align: 'center',
            formatter: function (val, row) {if (val == 0) {return '0.00';} else {return toMoney(val);}},
        },
        {field: 'zijin', title: '资金占比', width: 100, align: 'left',
            formatter: function (value, row, index) {
                return '中央：' + row.centerAmount + '<br>省：' + row.provinceAmount+ '<br>市：' + row.cityAmount + '<br>区县：' + row.countyAmount;
            }
        },
        {field: 'paymentAmount', title: '发放金额', width: 100, align: 'center',
            formatter: function (val, row) {if (val == 0) {return '0.00';} else {return toMoney(val);}},
        },
        {field: 'surplusAmount', title: '剩余金额', width: 100, align: 'center',
            formatter: function (val, row) {if (val == 0) {return '0.00';} else {return toMoney(val);}},
        },
        {field: 'state', title: '发放状态', width: 100, align: 'center',
            formatter: function (value, row, index) {
                if (value == 0) {return '发放中';}
                if (value == 1) {return '发放完成';}}
        },
        {field: "opr", title: '操作', width: 100, align: 'center',
            formatter: function (val, row) {
                c = '<a  id="look"  data-id="98" class=" operA" onclick="obj.look(\'' + row.id + '\')">查看</a> ';
                a = '<a  id="add" data-id="98" class=" operA"  onclick="obj.edit(\'' + row.id + '\')">编辑</a> ';
                /*b = '<a  id="detail" data-id="98" class=" operA"  onclick="obj.detail(\'' + row.id + '\')">明细</a> ';*/
                return a + c;
            }
        }
    ]]
})
// 弹出框加载
$("#addBox").dialog({
    title: "信息内容",
    width: 500,
    height: 300,
    closed: true,
    modal: true,
    shadow: true,
    resizable: true,
    minimizable: true,
    maximizable: true,
})
// 加载物流详情
$("#lookTail").dialog({
    title: "信息内容",
    width: 650,
    height: 410,
    closed: true,
    modal: true,
    shadow: true,
    resizable: true,
    minimizable: true,
    maximizable: true,
})
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

// 弹出框加载
$("#detailBox").dialog({
    title: "乡镇发放明细",
    width: 400,
    height: 300,
    resizable: true,
    minimizable: true,
    maximizable: true,
    closed: true,
    modal: true,
    shadow: true
})

//乡镇明细回显
function getMx(projectId){
    //console.log(projectId);
    $.ajax({
        url: '/ky-ykt/project/projectAreasSelect',
        type: 'get',
        dataType: 'json',
        async: false,
        data: {id: projectId},
        success: function (res) {
            if (res != null) {
                //console.log(res);
                if(res.length != 0){
                    $("#detailForm").empty();
                    for (var i = 0; i < res.length; i++) {
                        $("#detailForm").append("<div class='formDiv'><label>"+res[i].name+':'+"</label> " +
                            "<input style='width: 60%;height: 20px; border: 1px solid #95B8E7; " +
                            "data-options='min:0,precision:2' name='areaAmount' " +
                            "type='text' id='"+res[i].id+"' value='"+res[i].areaAmount+"'>");
                        var newline= document.createElement("br");
                        $("#detailForm").append(newline);
                    }
                    //console.log(projectId);
                    $("#projectId").val(projectId);
                }
            } else {
                $.messager.show({
                    title: '提示',
                    msg: '更新失败'
                })
            }
        },
    })
}

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