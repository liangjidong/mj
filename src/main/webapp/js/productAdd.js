/**
 * Created by JiangLi on 2015/7/7 0007.
 */

$(function(){
    var videoId ;
    var   productName;
    var  productMes ;
    var index = 0;
    var userID = sessionStorage.userId;
    var status1 = 0;
    if(!userID) {
        location.href = "/mj/login.html";
    }
    //上传图片
    checkNull();//判断输入框是否为空
    $(".close").show();
    /*$("#submit-photo").bind("Addp",function(){
        console.log(2);

        ajaxFileUpload1();
    })
    //上传视频
    $("#submit-video").bind("Addv",function(){
        console.log(3);

        //ajaxFileUpload2();
    })*/

    //添加产品
    $("#submit-product").click(function() {
        if($("#productId").val() == ""){
            $(".product-id .status").html("*输入不能为空或空格！");
            return false;
        }
        if($("#productMes").val() == ""){
            $(".product-mes .status").html("*输入不能为空或空格！");
            return false;
        }

        if (isNull(productId)) {
            $(".product-id .status").html("*输入不能为空或空格！")
            return false;
        }
        if (isNull(productMes)) {
            $(".product-mes .status").html("*输入不能为空或空格！")
            return false;
        }
        $("#add-model").modal("toggle");
        /*$("#submit-photo").trigger("Addp");
        $("#submit-video").trigger("Addv");*/
            ajaxFileUpload1();
            ajaxFileUpload2();



    })

        //上传图片ajax函数
        function ajaxFileUpload1() {
            console.log(1);
            if($("#productId").val() == "" || $("#productMes").val() == ""){
                return false;
            }
            $(".photoId").html("图片正在上传中.....");
            $.ajaxFileUpload
            (
                {
                    url: 'api/picture/upload?userId=' + userID, //用于文件上传的服务器端请求地址
                    secureuri: false, //是否需要安全协议，一般设置为false
                    fileElementId: 'add-photo', //文件上传域的ID
                    dataType: 'json', //返回值类型 一般设置为json
                    success: function (data) {
                        if (data.status == 0) {
                            console.log(data);
                            var photoId =data.body ;
                            $(".photoId").html("上传成功！ 图片ID： <span id='photo-id'>" + photoId + "</span>");
                            status1 = 1;
                            sessionStorage.photoId=data.body;
                            console.log(photoId);
                            $("#submit-video").trigger("Addv");
                            index ++;
                            console.log("index=" + index );
                            if(index == 2){
                                mjproduct();
                            }
                        }
                        if (data.status == 1) {
                            var message = data.message;
                            $(".photoId").html("上传失败！" + message);
                            $("#add-model .modal-body p").html("上传失败！" +"原因：图片"+ message)
                            $(".close").show();
                        }
                    }
                }
            )
            return false;
        }

        //上传视频ajax函数
        function ajaxFileUpload2() {
            console.log(2);
            if($("#productId").val() == "" || $("#productMes").val() == ""){
                return false;
            }
            $(".videoId").html("视频正在上传中..请稍等...");
            $.ajaxFileUpload
            (
                {
                    url: 'api/video/upload?userId=' + userID, //用于文件上传的服务器端请求地址
                    secureuri: false, //是否需要安全协议，一般设置为false
                    fileElementId: 'add-video', //文件上传域的ID
                    dataType: 'json', //返回值类型 一般设置为json

                    success: function (data) {
                        if (data.status == 0 && data.body) {
                            sessionStorage.videoId=data.body;
                            videoId = data.body;
                            $(".videoId").html("上传成功！ 视频ID： <span id='video-id'>" + videoId + "</span>");
                             productName = $("#productId").val();
                             productMes = $("#productMes").val();
                             photoId = parseInt($("#photo-id").html());

                            if(!videoId){
                                return false;
                            }
                            index ++;
                            console.log("index=" + index );

                            if(index == 2){

                                mjproduct();
                            }


                        }
                        if (data.status == 1) {
                            var message = data.message;
                            $(".videoId").html("上传失败！" + message);
                            $("#add-model .modal-body p").html("上传失败！" +"原因：视频"+ message);
                        }
                    }
                }
            )
            return false;
        }

        /*检查输入字符串是否为空或者全部都是空格
         输入：str
         返回：如果全是空返回true,否则返回false
         */
        function isNull(str) {
            if (str == "") {
                return true;
            }
            var regu = "^[ ]+$";
            var re = new RegExp(regu);
            return re.test(str);
        };
        function checkNull() {
            $("#productId").blur(function () {
                var productName = $("#productId").val();
                if (isNull(productName)) {
                    $(".product-id .status").html("*输入不能为空或空格！")
                } else {
                    $(".product-id .status").html(" ");
                }
            })
            $("#productMes").blur(function () {
                var productMes = $("#productMes").val();
                if (isNull(productMes)) {
                    $(".product-mes .status").html("*输入不能为空或空格！")
                } else {
                    $(".product-mes .status").html(" ");
                }
            })
        }

    function mjproduct(){
        var prodata = {
            pictureId: sessionStorage.photoId,
            videoId: videoId,
            title: productName,
            description: productMes
        }
        console.log(prodata);
        $.ajax({
            type: "POST",
            url: "api/mjproduct?userId=" + userID,
            data: JSON.stringify(prodata),
            contentType: "application/json",
            success: function (data) {
                if (data.status == 0) {
                    $("#add-model").hide();
                    alert("上传产品成功！");
                    location.href = "../productManage_back.html";
                }
            }
        })
    }


})

