/**
 * Created by Administrator on 2017/11/8.
 */
// 加载树
$("#tree").tree({
        url:'json/userTree.json',
        animate:true,
        checkbox:true,
        lines:true,
        dnd:true,
        onContextMenu:function (e,node) {
                e.preventDefault();
                $("#tree").tree('select',node.target);
                $("#mean").menu('show',{
                        left:e.pageX,
                        top:e.pageY
                })
        },
        onClick:function (node) {
              var partId=node.domId;
                var partName=node.text;
                var parentName=$("#tree").tree('getParent',node.target).text;
                $("#partId").val(partId);
                $("#partName").val(partName);
                $("#parentPart").combotree('setValue',parentName);


        }
})
// 加载部门下拉框
$("#parentPart").combotree({
        url:'json/userTree.json',
        width:'70%',
        height:28,
        checkbox:true,
        onSelect:function () {
                var t = $("#parentPart").combotree('tree'); // 得到树对象
                var n = t.tree('getSelected');
                $("#parentPart").combotree('setValue',n.text)
        }

})

// 增加部门
function addPart() {
        var selectNode=$("#tree").tree("getSelected");
        var id=parseInt(Math.random()*1000+1);
        $("#partId").val(id);
        $("#parentPart").combotree('setValue',selectNode.text);
        $("#partper").val("");
        $("#partEmail").val("");
        $("#partTele").val("");
        $("#note").val("");
        $("#partName").val("");
        $("#edit").hide();
        $("#save").show();


}
// 保存部门信息
function saveForm(){
        $('#formBox').form('submit', {
                onSubmit: function(){
                        return $(this).form('validate');
                },
                success:function(data){
                        var partName=$("#partName").val();
                        var selectNode=$("#tree").tree('getSelected');
                        $('#tree').tree('append',{
                                parent:selectNode.target,
                                data:[{
                                        text:partName,
                                        "iconCls":"icon-reload"
                                }]
                        });
                        $("#edit").show();
                        $("#save").hide();
                        $.messager.show({
                                title:'提示',
                                msg:'部门新增成功'
                        });
                        $("#partId").attr('readonly',true).css('background',"#eee");


                }
        });
}
// 修改部门
function editForm(){
        $('#formBox').form('submit', {
                onSubmit: function(){
                        return $(this).form('validate');
                },
                success:function(data){
                        var partName=$("#partName").val();
                        var selectNode=$("#tree").tree('getSelected');
                        $('#tree').tree('update',{
                                target: selectNode.target,
                                data:[{
                                        text:partName,

                                }]
                        });

                        $.messager.show({
                                title:'提示',
                                msg:'部门修改成功'
                        });



                }
        });
}
// 删除部门
function delPart() {
        var selectNode=$("#tree").tree('getSelected');
        $("#tree").tree("remove",selectNode.target);


}
// 初始化部门信息
function init() {
        var partRoot=$("#tree").tree('getRoot');
        var partId=partRoot.domId;
        var partName=partRoot.text;
        $("#partId").val(partId);
        $("#partName").val(partName);
        $("#parentPart").combotree('setValue',partName);

}