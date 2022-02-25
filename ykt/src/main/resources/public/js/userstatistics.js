$("#operDepartment").combobox({
    url: '/ky-ykt/department/queryByParamsSearch',
    method: 'get',
    height: 26,
    width: '15%',
    valueField: 'id',
    textField: 'departmentName',
    loadFilter: function (data) {
        var obj = {};
        obj.id = '';
        obj.departmentName = '请选择'
        //在数组0位置插入obj,不删除原来的元素
        data.splice(0, 0, obj)
        return data;
    }
})
$("#areaId").combotree({
    url: '/ky-ykt/areas/queryByParentId',
    method: "get",
    height: 26,
    width: '15%',
    valueField: 'id',
    textField: 'text',
    onLoadSuccess: function () {
        $("#areaId").combotree('tree').tree("collapseAll");
    },
    onSelect: function () {
        var t = $("#areaId").combotree('tree');
        var n = t.tree('getSelected');
        var text = n.id;
        $("#areaId").combotree('setValue', text);
    }
})
$("#projectType").combobox({
    url: '/ky-ykt/projectType/queryProjectTree',
    method: 'get',
    height: 26,
    width: '15%',
    valueField: 'id',
    textField: 'name',
    loadFilter: function (data) {
        var obj = {};
        obj.id = '';
        obj.name = '请选择'
        //在数组0位置插入obj,不删除原来的元素
        data.splice(0, 0, obj)
        return data;
    }
});

$(function (){
    var userId = sessionStorage.getItem("userId");
    if (userId != '223b6557-3969-4b1d-9b81-296786a546de') {
        $("#areaId").hide();
    }
})
obj = {
    // 查询
    find: function () {
        $("#table").datagrid('load', {
            projectType: $("#projectType").val(),
            userName: $("#userName").val(),
            operDepartment: $("#operDepartment").val(),
            idCardNo: $("#idCardNo").val(),
            startTime: $("#startTime").val(),
            endTime: $("#endTime").val(),
            areaId: $("#areaId").val(),
            batchNumber: $("#batchNumber").val(),
            status: $("#status").val(),
        })
    },
    excel: function () {
        window.location.href = '/ky-ykt/statistics/excel?flag=2&projectType='+ $("#projectType").val()+'&userName='+$("#userName").val()
        +'&operDepartment='+$("#operDepartment").val()+'&idCardNo'+$("#idCardNo").val()+'&startTime='+$("#startTime").val()
        +'&endTime='+$("#endTime").val()+'&areaId='+$("#areaId").val()+'&batchNumber='+$("#batchNumber").val()+'&status='+$("#status").val();
    },
}
// 加载表格
$("#table").datagrid({
    title: "数据列表",
    iconCls: "icon-left02",
    url: '/ky-ykt/statistics/queryPage',
    fitColumns: true,
    striped: true,
    pagination: true,
    pageSize: 20,
    method: "GET",
    width: '100%',
    rownumbers: true,
    //pageList: [10, 20],
    pageNumber: 1,
    nowrap: false,
    singleSelect: true,
    height: 'auto',
    sortName: 'id',
    checkOnSelect: true,
    sortOrder: 'asc',
    showFooter: true,
    toolbar: '#tabelBut',
    columns: [[
        {field: 'userName', title: '姓名', width: 100, align: 'center'},
        {field: 'bankCardNo', title: '银行卡号', width: 120, align: 'center'},
        {field: 'idCardNo', title: '身份证号', width: 120, align: 'center'},
        {field: 'projectName', title: '资金发放名称', width: 100, align: 'center'},
        {field: 'grantAmount', title: '发放金额', width: 50, align: 'center',
            formatter: function (val, row) {if (val == 0) {return '0.00';} else {return toMoney(val);}},
        },
        {field: 'jtzz', title: '所在区县', width: 100, align: 'center',
            formatter: function (value, row, index) {
                return  row.county + '' + row.town+ '' + row.village+''+row.address;
            }
        },
       /* {field: 'town', title: '所在乡镇', width: 100, align: 'center',},
        {field: 'village', title: '所在村组', width: 100, align: 'center',},
        {field: 'address', title: '详细地址', width: 100, align: 'center'},*/
        {field: 'status', title: '发放结果', width: 100, align: 'center',
            formatter: function (value, row, index) {
                if (value == 1) {
                    return '发放成功';
                } else if (value == 2) {
                    return '发放失败';
                } else if (value == 3) {
                    return '未提交';
                } else if (value == 4) {
                    return '审核中';
                }
            }
        },
        /*
        {
            field: 'projectTypeName',
            title: '补贴项目类型',
            width: 100,
            align: 'center'

        },
        */
        {field: 'departmentName', title: '主管部门', width: 100, align: 'center'},
    ]],
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