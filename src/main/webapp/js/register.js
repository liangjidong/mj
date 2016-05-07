/**
 * Created by jiangli on 2015//6/03.
 */
$(function(){
    var array =[0,1,2];
    init();
    codePicture();
    checkNull(array);     //检查输入框是否为空
    checkIsSame();       //检查两次密码输入是否相同
    register(array);     //提交注册信息
});
//初始化
function init(){
    $(".register input").focus(function(){
        $(this).parents(".form-group").removeClass("has-success has-feedback");
        $(this).next().removeClass("fa");
    });
}
//输入框为空的检查函数
function checkNull(array){
    for(var i=0;i<array.length;i++){
        $(".form-group input").eq(i).blur(function(){
            if($(this).val()==null || $(this).val()==""){
                $(this).parents(".form-group").addClass("has-warning");
                $(this).next().addClass("fa fa-warning").css("color","#8a6d3b");
                $(this).parent().next().html("输入不能为空!");
            }else{
                $(this).parents(".form-group").addClass("has-success");
                $(this).next().addClass("fa fa-check").css("color","#3c763d");
            }
        }).focus(function(){
            $(this).parents(".form-group").removeClass("has-success has-warning");
            $(this).next().removeClass("fa fa-check fa fa-warning");
            $(this).parent().next().html("");
        });
    }
}
//获取后台传来的验证码图片
function codePicture(){

    refreshPicture();    //重新获得验证码图片

    function refreshPicture(){
        $(".refresh-code").click(function(){
            $.ajax({
                url:"api/user/identifyPic",
                type:"GET"
            });
            $(".code-image").attr("src","api/user/identifyPic");
        })
    }
}

//注册按钮点击事件
function register(){
    $(".register-btn").click(function(){
        var status = false;
        var i = 0;
        $(".form-group").each(function(){
            status = $(this).hasClass("has-success");
            status? ++i:i;
        });
        var user = {
            name:$(".register #loginName").val(),
            password:$(".register #password").val()
        };
        var codeNumber = $(".register #confirm-code").val();
        if (i==3) {
            $.ajax({
                url: "api/user?code=" + codeNumber,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(user),
                success: function (data) {
                    if (data.status == 0) {
                        $("#mymodal .modal-body p").html("操作成功!");
                        $("#mymodal").modal("toggle");

                    }
                    if (data.status == 6) {
                        $("#mymodal .modal-body p").html("该用户已存在！");
                        $("#mymodal").modal("toggle");
                    }
                    if (data.status == 1) {
                        $("#mymodal .modal-body p").html("操作失败!");
                        $("#mymodal").modal("toggle");
                    }
                    if (data.status == 10) {
                        $("#mymodal .modal-body p").html("验证码失败!");
                        $("#mymodal").modal("toggle");
                    }
                },
                error: function () {
                    $("#mymodal .modal-body p").html("Ajax 发送失败！");
                    $("#mymodal").modal("toggle");
                }
            });
        }else{
            $("#mymodal .modal-body p").html("请检查输入格式是否正确");
            $("#mymodal").modal("toggle");
        }
    });
}
//检查两次输入密码是否相同
function checkIsSame() {
    $(".register #confirmPassword").blur(function () {
        if ($(this).val() != "") {
            var val1 = $(".register #password").val();
            var val2 = $(this).val();
            if (val1 != val2) {
                $(this).parents(".form-group").removeClass("has-success").addClass("has-warning");
                $(this).next().removeClass("fa fa-check").addClass("fa fa-warning").css("color", "#8a6d3b");
                $(this).parent().next().html("两次输入密码不一致！");
            }
        }
    });
}