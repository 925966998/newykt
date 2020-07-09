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
    url: '/ky-ykt/projectType/queryByParams',
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
        })

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
    pageSize: 10,
    method: "GET",
    width: '100%',
    rownumbers: true,
    pageList: [10, 20],
    pageNumber: 1,
    nowrap: true,
    singleSelect: true,
    height: 'auto',
    sortName: 'id',
    checkOnSelect: true,
    sortOrder: 'asc',
    toolbar: '#tabelBut',
    columns: [[
        {
            field: 'userName',
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
            field: 'projectName',
            title: '资金发放名称',
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
            field: 'county',
            title: '所在区县',
            width: 100,
            align: 'center',
        }, {
            field: 'town',
            title: '所在乡镇',
            width: 100,
            align: 'center',
        }, {
            field: 'village',
            title: '所在村组',
            width: 100,
            align: 'center',
        },
        {
            field: 'address',
            title: '详细地址',
            width: 100,
            align: 'center'

        }, {
            field: 'status',
            title: '发放结果',
            width: 100,
            align: 'center',
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
        {
            field: 'departmentName',
            title: '主管部门',
            width: 100,
            align: 'center'

        },
    ]],
})
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