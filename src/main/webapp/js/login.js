/**
 * Created by jiangli on 2015//6/03.
 */
$(function(){
    var array = [0,1];
    //检查输入框是否为空
    checkNull(array);
    codePicture();
    function checkNull(array){
        for(var i=0;i<array.length;i++){
            $(".form-group input").eq(i).blur(function(){
                if($(this).val()==null || $(this).val()==""){
                    $(this).parents(".form-group").addClass("has-warning");
                    $(this).next().addClass("fa fa-warning").css("color","#8a6d3b");
                    $(this).parent().next().html("输入不能为空");
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
    //登陆的提交事件
	
    $(".loginSumbit").bind("click",function(){
        console.log("a");
        var number=0;
        var user = {
            name:$("#loginName").val(),
            password:$("#loginPassword").val()
        };
        console.log(user);
        var code = $("#confirm-code").val();
        if(user.name ){
            $("input").eq(0).parents(".form-group").addClass("has-success");
        }
        if(user.password){
            $("input").eq(1).parents(".form-group").addClass("has-success");
        }
        for(var i=0;i<array.length;i++){
            if($(".login-form .form-group").eq(i).hasClass("has-success")){
                number++;
            }
        }
        if(number != array.length){
            return false;
        }
        console.log("b");


        $.ajax({
            url:"api/user/login?code="+code,
            type:"POST",
            contentType:"application/json",
            data:JSON.stringify(user),
            success:function(data){

                if(data.status == 0){
                    var json = eval(data.body);
                    sessionStorage.userId = json.id;
                    var userName = json.name;
                    location.href="index.html";

                }
                //else{
                //    $("#login-model .modal-body p").html(data.message);
                //    $("#login-model").modal("toggle");
                //    return false;
                //}
                if(data.status == 7){
                    $("#login-model .modal-body p").html("用户不存在！");
                    $("#login-model").modal("toggle");
                    return null
                };
                if(data.status == 9){
                    $("#login-model .modal-body p").html("密码不正确!");
                    $("#login-model").modal("toggle");
                    return null;
                }
                if(data.status == 10){
                    $("#login-model .modal-body p").html("验证码错误!");
                    $("#login-model").modal("toggle");
                    return null;
                }
                }
            })
        });

});