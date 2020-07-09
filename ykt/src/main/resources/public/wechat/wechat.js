//点击提交
$("#btn").click(function () {
    $.ajax({
        url: "/ky-ykt/wechat/weChatLogin",
        type: "POST",
        data: {
            name: $("#name").val().trim(),
            idCardNo: $("#idCardNo").val().trim(),
            bankCardNo: $("#bankCardNo").val().trim()
        },
        beforeSend: function () {
            $.messager.progress({
                text: '查询中。。。'
            });
        },
        success: function (data) {
            $.messager.progress('close');
            if (data.code == 10000) {
                console.log(data.data);
                sessionStorage.setItem("person", JSON.stringify(data.data));
                window.location.href = "../web/wechatList.html";
            } else {
                $.messager.alert("查询失败", data.data, 'info');
                window.location.href = "../web/wechatError.html";
            }
        },
        error: function (err) {
            $.messager.progress('close');
            $.messager.alert("查询失败", data.data, 'info');
            window.location.href = "../web/wechatError.html";
        }
    })
})

//修改个人资料
$("#updateBtn").click(function () {
    $.ajax({
        url: "/ky-ykt/wechat/wechatPerson",
        type: "POST",
        data: {
            name: $("#name").val().trim(),
            idCardNo: $("#idCardNo").val().trim(),
            bankCardNo: $("#bankCardNo").val().trim()
        },
        beforeSend: function () {
            $.messager.progress({
                text: '查询中。。。'
            });
        },
        success: function (data) {
            $.messager.progress('close');
            if (data.code == 10000) {
                console.log(data.data);
                sessionStorage.setItem("person", JSON.stringify(data.data));
                window.location.href = "../web/wechatperson.html";
            } else {
                $.messager.alert("查询失败", data.data, 'info');
                window.location.href = "../web/wechatError.html";
            }
        },
        error: function (err) {
            $.messager.progress('close');
            $.messager.alert("查询失败", data.data, 'info');
            window.location.href = "../web/wechatError.html";
        }
    })
})