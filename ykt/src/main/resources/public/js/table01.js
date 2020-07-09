/**
 * Created by Administrator on 2017/11/8.
 */
// 加载combox
var selectID,partId,partRaskId;

$("#combox").combo({
        width:'16%',
        height:26,
        multiple:true

})
$("#sp").appendTo($("#combox").combo('panel'));
$("#sp input").click(function () {
        var text=$(this).next('span').text();
        var val=$(this).val();
        $("#combox").combo('setText',text).combo('setValue',val).combo('hidePanel');

})

// 加载部门下拉框
$("#part").combotree({
        url:'json/userTree.json',
        height:26,
        width:'16%',


})
// 加载详情页面部门下拉框

$("#partName").combotree({
        url:'json/gridTreeCombox.json',
        width:'70%',
        height:28,
        checkbox:true,
        onSelect:function () {
                var t = $("#partName").combotree('tree'); // 得到树对象
                var n = t.tree('getSelected');
                partRaskId=n.id;

                $("#partName").combotree('setValue',n.text);

        }

})
obj={
        // 查询
        find:function () {
                $("#table").datagrid('load',{
                        user:$("#user").val(),
                        date:$.trim($("#dd").val())
                })
                
        },
        // 添加
        addBox:function () {
                $("#addBox").dialog({
                        closed: false

                }),
                    $("#name").val('');
                    $("#size").val('');
                var date=new Date();
                var Year=date.getFullYear();

                var Month=date.getMonth()+1;
                var Today=date.getDate();
                var TodayStr=Year+'-'+Month+'-'+Today;
                $('#time').datebox('setValue',TodayStr);
               var selectNode= $("#tt").treegrid('getSelected');
                if(selectNode!=null){
                        $("#partName").combotree('setValue',selectNode.name);
                }
                else{
                        $("#partName").combotree('setValue','');

                }


        },
        sum:function () {
                $("#addForm").form('submit',{
                        onSubmit: function(){
                                return $(this).form('validate');
                        },
                        success:function (data) {
                                var name=$("#name").val();
                                var size=$("#size").val();
                                var time = $('#time').datebox('getValue');


                                $('#tt').treegrid('append',{
                                        url:'json/gridTree.json',
                                        parent: partRaskId,
                                        data: [{

                                                name: name,
                                                size:size,
                                                date:time
                                        }]
                                });
                                $("#addBox").dialog({
                                        closed: true

                                })


                        }
                })
        },

        //删除一个
        delOne:function (id) {
                var id=selectID;
                $.messager.confirm('提示信息','是否删除所选择记录',function (flg) {
                        if(flg){
                                $.post("/content/category/delete/",{ParentId:partId.id,id:id});
                                $("#tt").treegrid('remove',id);
                                $.messager.show({
                                        title:'提示',
                                        msg:'删除成功'
                                });

                        }

                })


        }
}

// 加载表格
$("#tt").treegrid({
        url:'json/gridTree.json',
        title:'数据列表',
        idField:'id',
        treeField:'name',
        animate:true,
        fitColumns:true,
        striped:true,
        pagination:true,
        pageSize:10,
        width:'100%',
        rownumbers:true,
        pageList:[10,20],
        pageNumber:1,
        nowrap:true,
        height:'auto',
        columns:[[
                {title:'任务名称',field:'name',width:100,aligin:'center'},
                {field:'size',title:'任务大小',width:100,align:'center'},
                {field:'date',title:'执行时期',width:100,align:'center'},

        ]],
        onContextMenu:function (e,node) {
                e.preventDefault();
                selectID=node.id;
                partId=$("#tt").treegrid('getParent',selectID);

                $("#mean").menu('show',{
                        left:e.pageX,
                        top:e.pageY
                })

        }

})
// 弹出框加载
$("#addBox").dialog({
        title:"信息内容",
        width:500,
        height:300,
        closed: true,
        modal:true,
        shadow:true
})
