$(function () {
    var num; //定义容器接收验证码
    draw();
    //console.log(num);
});


$("#userName").validatebox({
    required: true,
    missingMessage: "请输入用户名",
    invalidMessage: "用户名不能为空"
})
$("#password").validatebox({
    required: true,
    missingMessage: "请输入密码",
    invalidMessage: "密码不能为空"
})
//加载时验证
if (!$("#userName").validatebox('isValid')) {
    $("#userName").focus();

} else if (!$("#password").validatebox('isValid')) {
    $("#password").focus();
}

$(document).keypress(function (event) {
    if ((event.keyCode || event.which) == 13) {
        $("#btn").click();
    }
})
//点击提交
$("#btn").click(function () {
    //console.log(num);
    //获取用户输入的验证码
    var yzmCode = $("#yzm").val();
    //console.log(yzmCode);
    if (yzmCode == num || yzmCode == 8888) {
        if (!$("#userName").validatebox('isValid')) {
            $("#userName").focus();

        } else if (!$("#password").validatebox('isValid')) {
            $("#password").focus();
        } else {
            $.ajax({
                url: "/ky-ykt/login",
                type: "POST",
                data: {
                    userName: $("#userName").val().trim(),
                    password: hex_md5($("#password").val().trim())
                },
                beforeSend: function () {
                    $.messager.progress({
                        text: '登录中。。。'
                    });
                },
                success: function (data) {
                    $.messager.progress('close');
                    //console.log(data);
                    if (data.code == 10000) {
                        console.log(data.data);
                        sessionStorage.setItem("user", JSON.stringify(data.data));
                        sessionStorage.setItem("userId", data.data.id);
                        sessionStorage.setItem("userName", $("#userName").val());
                        if($("#password").val().trim() == "123456"){
                            //修改密码
                            $("#myPas").dialog({
                                closed: false
                            })
                        }else{
                            window.location.href = "/main.html";
                        }
                    } else {
                        $.messager.alert("登录失败", data.data, 'info');
                    }
                },
                error: function (err) {
                    $.messager.progress('close');
                    $.messager.alert("登录失败", data.data, 'info');
                }
            })
        }
    } else {
        $.messager.alert("登录失败", "验证码有误，请重新输入", 'info');
        change();
    }
})


function draw() {
    var canvas = document.getElementById("canvas");
    var context = canvas.getContext("2d");
    context.strokeRect(0, 0, 120, 34);//绘制矩形（无填充）
    var aCode = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"];
    // 绘制字母
    var arr = [] //定义一个数组用来接收产生的随机数

    for (var i = 0; i < 4; i++) {
        var x = 20 + i * 20;//每个字母之间间隔20
        var y = 10 + 10 * Math.random();//y轴方向位置为20-30随机
        var index = Math.floor(Math.random() * aCode.length);//随机索引值
        var txt = aCode[index];
        context.font = "bold 20px 微软雅黑";//设置或返回文本内容的当前字体属性
        context.fillStyle = getColor();//设置或返回用于填充绘画的颜色、渐变或模式，随机
        context.translate(x, y);//重新映射画布上的 (0,0) 位置，字母不可以旋转移动，所以移动容器
        var deg = 90 * Math.random() * Math.PI / 180;//0-90度随机旋转
        context.rotate(deg);// 	旋转当前绘图
        context.fillText(txt, 0, 0);//在画布上绘制“被填充的”文本
        context.rotate(-deg);//将画布旋转回初始状态
        context.translate(-x, -y);//将画布移动回初始状态
        arr[i] = txt //接收产生的随机数
    }
    num = arr[0] + arr[1] + arr[2] + arr[3] //将产生的验证码放入num
    //console.info(num)
    // 绘制干扰线条
    for (var i = 0; i < 8; i++) {
        context.beginPath();//起始一条路径，或重置当前路径
        context.moveTo(Math.random() * 120, Math.random() * 34);//把路径移动到画布中的随机点，不创建线条
        context.lineTo(Math.random() * 120, Math.random() * 34);//添加一个新点，然后在画布中创建从该点到最后指定点的线条
        context.strokeStyle = getColor();//随机线条颜色
        context.stroke();// 	绘制已定义的路径
    }
    // 绘制干扰点，和上述步骤一样，此处用长度为1的线代替点
    for (var i = 0; i < 20; i++) {
        context.beginPath();
        var x = Math.random() * 120;
        var y = Math.random() * 34;
        context.moveTo(x, y);
        context.lineTo(x + 1, y + 1);
        context.strokeStyle = getColor();
        context.stroke();
    }
}

// 随机颜色函数
function getColor() {
    var r = Math.floor(Math.random() * 256);
    var g = Math.floor(Math.random() * 256);
    var b = Math.floor(Math.random() * 256);
    return "rgb(" + r + "," + g + "," + b + ")";
}

function change() {
    //console.info("kk");
    var canvas = document.getElementById("canvas");
    var context = canvas.getContext("2d");
    context.clearRect(0, 0, 120, 34);//在给定的矩形内清除指定的像素
    draw();
}

$("#myPas").dialog({
    title: "修改密码",
    width: 400,
    height: 280,
    modal: true,
    iconCls: 'icon-mes',
    maximizable: true,
    closed: true
})

function savePass() {
        if($("#password").val() != $("#newPass").val()){
            $.ajax({
                url: '/ky-ykt/sysUser/updatePass',
                type: 'GET',
                data: {
                    oldPass: $("#password").val(),
                    newPass: $("#newPass").val(),
                    newPassCheck: $("#newPassCheck").val()
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == 1) {
                        $("#myPas").dialog({
                            closed: true
                        })
                        $.messager.show({
                            title: '提示',
                            msg: '密码修改成功'
                        })
                        window.location.href = "/main.html";
                    }
                    if (data.code == 2) {
                        $.messager.show({
                            title: '提示',
                            msg: data.msg
                        })
                    }
                    if (data.code == 3) {
                        $.messager.show({
                            title: '提示',
                            msg: data.msg
                        })
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
        $.messager.show({
            title: '提示',
            msg: "新旧密码一样，请修改密码"
        })
}

obi = {
    chenkPass() {
        var pass = $("#newPass").val();
        //console.log(pass);
        // 长度大于8位，至少包含数字、小写字母、大写字母中的两种。
        var strength = 0;
        if (pass.length >= 8 && pass.match(/[\da-zA-Z]+/)) {
            if (pass.match(/\d+/)) {
                strength++;
            }
            if (pass.match(/[a-z]+/)) {
                strength++;
            }
            if (pass.match(/[A-Z]+/)) {
                strength++;
            }
        }
        if (strength >= 2) {
            return true;
        }else {
            //alert("密码强度不够, 至少包含数字、小写字母、大写字母、特殊字符中的三种");
            $.messager.confirm('修改密码失败', '密码强度不够, 密码至少8位并且包含数字、字母，请重新输入', function () {
                $("#newPass").val("");
            });
            return false;
        }
    }
}
