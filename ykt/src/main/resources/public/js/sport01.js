/**
 * Created by Administrator on 2017/11/8.
 */


$("#addr").combobox({
        url:'json/addr.json',
        valueField:'id',
        textField:'text'
})
$("#work").combobox({
        url:'json/addr.json',
        valueField:'id',
        textField:'text'
})
$('.easyui-filebox').filebox({
        buttonText:'选择文件'
})
$("#con").focus(function () {
        $(this).val("");
        $(this).css('color','#333');

})
$("#con").blur(function () {
        $(this).val("请输入提案内容");
        $(this).css('color','#999');

})
$.fn.datebox.defaults.formatter = function(date){
        var y = date.getFullYear();
        var m = date.getMonth()+1;
        var d = date.getDate();
        return y+'年'+m+'月'+d+'日';
}