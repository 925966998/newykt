// 加载表格
function doQuery(url,data) {
    // 加载表格
    $("#table").datagrid({
        title: "数据列表",
        iconCls: "icon-left02",
        url: url,
        queryParams: data,
        fitColumns: true,
        striped: true,
        pagination: true,
        pageSize: 10,
        method: "GET",
        width: '100%',
        rownumbers: true,
        pageList: [10, 20],
        pageNumber: 1,
        nowrap: true,
        height: 'auto',
        sortName: 'id',
        checkOnSelect: true,
        singleSelect: true,
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
                field: 'projectTypeName',
                title: '项目名称',
                width: 100,
                align: 'center'
            },
            {
                field: 'departmentNames',
                title: '所属单位',
                width: 100,
                align: 'center'
            },
            {
                field: 'totalAmount',
                title: '剩余金额',
                width: 100,
                align: 'center'
            },
            {
                field: 'paymentAmount',
                title: '发放金额',
                width: 100,
                align: 'center'
            },
            {
                field: 'startTime',
                title: '开始发放时间',
                width: 100,
                align: 'center',
                formatter: function (value, row, index) {
                    if (value != null) {
                        return new Date(value).Format("yyyy-MM-dd HH:mm")
                    }
                }
            },
            {
                field: 'endTime',
                title: '结束时间',
                width: 100,
                align: 'center',
                formatter: function (value, row, index) {
                    if (value != null) {
                        return new Date(value).Format("yyyy-MM-dd HH:mm")
                    }
                }
            },
            {
                field: 'state',
                title: '发放状态',
                width: 100,
                align: 'center',
                formatter:function(status){
                    switch (status) {
                        case 0:  return '<div>待审核</div>';
                        case 1:  return '<div>审核通过，待发放</div>';
                        case 2:  return '<div>审核不通过</div>';
                        case 3:  return '<div>审核通过，已发放</div>';
                    }
                }
            },
            {
                field: 'reason',
                title: '未发放原因',
                width: 100,
                align: 'center'
            },
             {
                 field: "opr",
                 title: '操作',
                 width: 100,
                 align: 'center',
                 formatter: function (val, row) {
                     return '<a  id="look"   class=" operA" class="easyui-linkbutton"  href="../web/personName.html?projectId='+row.id+'")">发放名单</a> ';
                 }
             }
        ]],
        onClickRow: function (rowIndex, rowData) {
            var rows = $("#table").datagrid("getSelections");
            if (rows.length > 1) {
                $.messager.alert('提示', '每次选择一条审批记录', 'info');
            }
        }
    })
}

$(function () {
    $("#projectNameSearch").combobox({
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
    $("#projectNameSearch").combobox('select', '0');
        // 加载表格
        doQuery('/ky-ykt/projectDetail/queryPage',{state: 0,flag:1});
        //document.getElementById("unCheckedProject").style.display = "none";
        $("#unCheckedProject").css('visibility', 'hidden');
})

obj = {
    // 已审核
    checkedProject: function () {
        // 加载表格
        //document.getElementById("unCheckedProject").style.display = "block";
        //document.getElementById("checkedProject").style.display = "none";
        //document.getElementById("audit").style.display = "none";
        $("#unCheckedProject").css('visibility', 'visible');
        $("#checkedProject").css('visibility', 'hidden');
        $("#audit").css('visibility', 'hidden');
        doQuery('/ky-ykt/projectDetail/queryPage',{state: 1,flag:1});
    },
    // 未审核
    unCheckedProject: function () {
        //document.getElementById("unCheckedProject").style.display = "none";
        //document.getElementById("checkedProject").style.display = "block";
        //document.getElementById("audit").style.display = "block";
        $("#unCheckedProject").css('visibility', 'hidden');
        $("#checkedProject").css('visibility', 'visible');
        $("#audit").css('visibility', 'visible');
        // 加载表格
        doQuery('/ky-ykt/projectDetail/queryPage',{state: 0,flag:1});
    },
    // 查询
    find: function () {
        $("#table").datagrid('load', {
            flag: 1,
            projectName: $("#projectNameSearch").val(),
        })
    },
    // 添加
    addBox: function () {
        $("#addBox").dialog({
            closed: false

        });
        $("#name").val('');
        $("#pass").val('');
        $("#part01").combotree('setValue', '');
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var str = y + '-' + m + '-' + d
        $("#time").datebox('setValue', str);


    },
    audit: function () {
        var rows = $("#table").datagrid("getSelections");
        console.log(rows.length)
        if (rows.length > 1) {
            $.messager.alert('提示', '每次选择一条审批记录', 'info');
        } else if (rows.length < 1) {
            $.messager.alert('提示', '请选择一条审批记录', 'info');
        } else {
            $("#auditBox").dialog({
                title: '审核信息',
                closed: false
            });
            $("#auditForm").form('reset');
            $("#id").val(rows[0].id);
            $.ajax({
                url: '/ky-ykt/projectDetail/auditInfo',
                data: {id: rows[0].id},
                type: 'get',
                dataType: 'json',
                success: function (res) {
                    $("#projectName").text(res.projectDetailEntity.projectName);
                    $("#paymentAmount").text(res.projectDetailEntity.paymentAmount);
                    $("#totalAmount").text(res.projectDetailEntity.totalAmount);
                    $("#pullNum").text(res.pullNum);
                    $("#operUser").text(res.projectDetailEntity.userName);
                    $("#operDepartment").text(res.projectDetailEntity.departmentName);
                },
            })
        }
    },
    dayin: function () {
        var rows = $("#table").datagrid("getSelections");
        if (rows.length > 1) {
            $.messager.alert('提示', '每次选择一条发放记录', 'info');
        } else if (rows.length < 1) {
            $.messager.alert('提示', '请选择一条发放记录', 'info');
        } else {
            $.ajax({
                url: '/ky-ykt/projectDetail/exporthm',
                data: {id: rows[0].id},
                type: 'get',
                dataType: 'json',
                beforeSend: function () {
                    $.messager.progress({
                        text: '打印信息采集中，请稍后...'
                    });
                },
                success: function (res) {
                    $.messager.progress('close');
                    window.open("../js/pdfjs/web/viewer.html?file="
                        + encodeURIComponent(res.fileUrl));
                        // + encodeURIComponent("/ky-ykt/projectDetail/pdfStreamHandeler?urlPath=" + res.fileUrl));
                }, error: function () {
                    $.messager.show({
                        title: '提示',
                        msg: '打印失败'

                    })

                }
            })
        }
    },
    hmpull: function () {
        var rows = $("#table").datagrid("getSelections");
        console.log(rows.length)
        if (rows.length > 1) {
            $.messager.alert('提示', '每次选择一条发放记录', 'info');
        } else if (rows.length < 1) {
            $.messager.alert('提示', '请选择一条发放记录', 'info');
        } else {
            window.location.href = '/ky-ykt/projectDetail/exporthm?id=' + rows[0].id

        }
    }, can: function () {
        $("#auditBox").dialog({
            closed: true
        })

    }, auditSum: function () {
        $("#auditForm").form('submit', {
            url: "/ky-ykt/projectDetail/audit",
            method: "post",
            onSubmit: function () {
                var state = $("input[name='state']:checked").val();
                if (state == 2) {
                    if (!$("#reason").val()) {
                        $.messager.alert("", "请输入失败原因", 'warning');
                        return false;
                    }
                }
                return $(this).form('validate')

            },
            success: function (data) {
                if (data.code = '10000') {
                    $("#table").datagrid('loaded');
                    $("#table").datagrid('load');
                    $("#auditBox").dialog({
                        closed: true

                    })
                    $.messager.show({
                        title: '提示',
                        msg: '审核完成'
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
    // 编辑
    edit: function (id) {
        var ID;
        $("#addBox").dialog({
            closed: false
        })
        $.ajax({
            url: '../json/table.json',
            type: 'get',
            dataType: 'json',
            success: function (res) {
                if (res) {
                    var data = res.rows;
                    $.each(data, function (index) {
                        ID = data[index].id;
                        if (id == ID) {
                            $('#addForm').form('load', {
                                id: id,
                                name: data[index].name,
                                pass: data[index].pass,
                                time: data[index].time

                            })
                            $("#part01").combotree('setValue', data[index].part);
                        }

                    })


                }

            },
            error: function () {
                $.messager.show({
                    title: '提示',
                    msg: '更新失败'

                })

            }
        })

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
                        topstr += '<div class="lookTailDiv01">' + new Date(rows[i].lastTime).Format("yyyy-MM-dd") + '</div>\n' +
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
                            downstr += '       <div class="clear"></div>\n<div class="divBorder magintTop03 floatLeft borderGreen"><span class="footBlood">发放到:' + rows[i].paymentDepartment + '--发放金额:' + rows[i].paymentAmount + '</span>';
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
                            downstr += '       <div class="clear"></div>\n<div class="divBorder magintTop01 floatLeft borderGreen"><span class="footBlood">发放到:' + rows[i].paymentDepartment + '--发放金额:' + rows[i].paymentAmount + '</span>';
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
            url: "",
            onSubmit: function () {
                return $(this).form('validate')

            },
            success: function (data) {
                var name = $("#name").val();
                var pass = $("#pass").val();
                var time = $("#time").datebox('getValue');
                var part = $("#part01").combotree('getValue');
                var id = parseInt(Math.random() * 100000);
                $("#table").datagrid('insertRow', {
                    index: 1,
                    row: {
                        id: id,
                        name: name,
                        pass: pass,
                        time: time,
                        part: part
                    }


                })
                $("#addBox").dialog({
                    closed: true

                })
                $.messager.show({
                    title: '提示',
                    msg: '信息保存成功'
                })


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
                        type: 'post',
                        url: "",
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
                                    msg: num + '个用户被删除'
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

// 弹出框加载
$("#addBox").dialog({
    title: "信息内容",
    width: 500,
    height: 300,
    closed: true,
    modal: true,
    shadow: true
})
// 加载物流详情
$("#lookTail").dialog({
    title: "信息内容",
    width: 650,
    height: 410,
    closed: true,
    modal: true,
    shadow: true
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