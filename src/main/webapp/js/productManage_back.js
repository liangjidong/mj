/**
 * Created by Jiangli on 2015/7/26 0026.
 */
$(function() {
    var currentPage = 1;
    var userID = sessionStorage.userId;
    if(!userID) {
        location.href = "/mj/login.html";
    }
    getProductList(currentPage);//获取产品列表
    trainPicture();//训练图片
    preWatch();//预览图片和视频
    modifyProduct();
    var format = function (time, format) {
        var t = new Date(time);
        var tf = function (i) {
            return (i < 10 ? '0' : '') + i
        };
        return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function (a) {
            switch (a) {
                case 'yyyy':
                    return tf(t.getFullYear());
                    break;
                case 'MM':
                    return tf(t.getMonth() + 1);
                    break;
                case 'mm':
                    return tf(t.getMinutes());
                    break;
                case 'dd':
                    return tf(t.getDate());
                    break;
                case 'HH':
                    return tf(t.getHours());
                    break;
                case 'ss':
                    return tf(t.getSeconds());
                    break;
            }
        })
    }
    //训练图片
    function trainPicture(){
        $("#check-picture").click(function(){
            $.ajax({
                type:"POST",
                url:"api/train",
                beforeSend:function(){
                    $("#train-model .close").hide();
                    $("#train-model .modal-body p").html("<img src='images/loading.gif' alt=' '/>"+"&nbsp;&nbsp;&nbsp;图片正在训练中,请稍等.......");
                    $("#train-model").modal("toggle");
                },
                success:function(data) {
                    if (data.status == 0) {
                        $("#train-model .close").show();
                        $("#train-model .modal-body p").html("训练成功！");
                    }
                }
            })
        });
    }
    //获取产品列表
    function getProductList(currentPage){
        var btn = "<div class='btn-group'><button class=' btn btn-primary  btn-sm'>选择链接视频</button>"
        btn +="<button class='btn btn-sm btn-primary dropdown-toggle' data-toggle='dropdown'>"
        btn +="<span class='caret'></span></button><ul class='dropdown-menu'> </ul></div>"
        $.ajax({
            type:"GET",
            dataType:"json",
            url:"api/mjproduct/query?userId="+userID+"&currentPage="+currentPage,
            success:function(data) {
                if(data.status == 13){
                    window.parent.location.href = "login.html";
                    //location.href = "login.html";
                }
                if (data.status == 0) {
                    var json = data.body;
                    var strHTML = "";
                    var basiclocal = "http://localhost:8081/mj/videos/";
                    var remotelocal = "http://120.25.241.211:8080/mj/videos/";
                    $.each(json, function (index, el) {
                        var url = el.pictureObj ? el.pictureObj.url : '';
                        var video_url = el.videoObj ? el.videoObj.finalUrl : '';
                        //var pictureId = el.pictureObj ? el.pictureObj.id : '';
                         var videoName = el.videoObj ? el.videoObj.name : '';
                        var publishTime = el.createtime?el.createtime:'';
                        // var videoUrl= el.videoObj?el.videoObj.originUrl:"";
                        var videoUrl = "videos/";
                        var status = "未知状态";
                        if(el.status == 0){
                            status = "已训练";
                        }else if(el.status == 1){
                            status = "未训练";
                        }else{
                            status = "未知状态";
                        }
                        var title = el.title?el.title:"";
                        var description = el.description?el.description:"";
                        var clicktimes = el.clicktimes?el.clicktimes:0;
                        var videoId = el.videoId?el.videoId:"";
                        strHTML += "<tr><td class='proWatchPhoto' ><img class='product-picture' src="+url+"></td>";
                        strHTML += "<td class='productId'>" + el.id + "</td>";
                        strHTML += "<td class='oldTitle'>" + title + "</td>";//产品名称
                        strHTML += "<td  class='oldDesCribe'>" + description + "</td>";//产品描述
                        strHTML += "<td >" + clicktimes + "</td>";
                        strHTML += "<td>"+ format(publishTime, 'yyyy-MM-dd HH:mm:ss')+"</td>"
                        strHTML += "<td>"+status+"</td>"//产品训练状态

                        strHTML += "<td><span style='display: none'>"+video_url+"</span><button class='btn-pho btn btn-warning btn-sm'>图片</button><br/><button class='btn-video btn btn-primary btn-sm' style='margin-top: 5px'>视频</button></td>";
                        strHTML += "<td class='pictureid'>" + el.pictureId + "</td>";
                        strHTML += "<td class='videotype'>" + videoId+ "</td>";
                        strHTML += "<td id="+index+">" +btn +"</td>";
                        strHTML += "<td><button class='watch-product btn btn-success btn-sm' >查看</button><button class='delect-product btn btn-danger btn-sm'>删除</button></br><button style='margin:2px 55px 0 0' class='modify-product btn btn-warning btn-sm'>修改</button></td></tr>";
                    })
                    $(".allproduct tbody").html(strHTML);
                    $(".pagination").empty().remove();
                    currentPage = data.currentPage;
                    pagination(data.currentPage,data.totalPage);
                    //获取链接视频列表并修改链接视频
                    $.ajax({
                        type: "GET",
                        dataType: "json",
                        url: "api/video/query.do?userId="+userID+"&currentPage="+currentPage,

                        success: function (data) {
                            if (data.status == 0) {
                               // $(".pagination").empty().remove();
                                var videojson = eval(data.body);
                                var ulHTML = "";
                                $.each(videojson, function (index) {
                                    ulHTML += "<li id="+index+"'>" + videojson[index].name + "</li>";
                                    $(".dropdown-menu").html(ulHTML);
                                })
                                //currentPage = data.currentPage;
                                //pagination(data.currentPage,data.totalPage);
                            }
                            $(document).on("click",".dropdown-menu li",function(){
                                $(this).parents(".dropdown-menu").css("display","none");
                                var pIndex = $(this).parents("tr").children(".pictureid").html();
                                var pIdx = parseInt(pIndex);
                                var vIndex = $(this).attr("id");
                                var vIdx = parseInt(vIndex);
                                var newVideoId = videojson[vIdx].id;
                                var  videotype = $(this).parents("tr").children(".videotype");
                                $.ajax({
                                    type: "PATCH",
                                    url: "api/picture/addvideo?pictureId="+pIdx+"&videoId="+newVideoId,
                                    success:function(){
                                        videotype.html(newVideoId);
                                    }
                                })
                            })
                        },
                        error: function (data) {
                            alert("ajax获取失败");
                        }
                    })
                    $(document).on("click",".dropdown-toggle",function() {
                        $(this).parents(".btn-group").children("ul").css("display","block");
                    })

                    delectProduct() ;//删除产品
                    watchProduct();//查看产品
                    function delectProduct() {
                        $(".delect-product").click(function () {
                            var check = confirm("是否删除");
                            if (!check){
                                return false;
                            }
                            var productId = $(this).parents("tr").children(".productId").html();
                            var list = $(this).parents("tr");
                            var json = {
                                id: productId
                            };
                            $.ajax({
                                type: "DELETE",
                                dataType: "json",
                                url: "api/mjproduct/" + productId,
                                data: JSON.stringify(json),
                                success: function (data) {
                                    if (data.status == 0) {
                                        list.empty().remove();
                                        alert("删除成功");
                                    }
                                }
                            })
                        })
                    }
                    function watchProduct(){
                        $(".watch-product").click( function(){
                                var productId = $(this).parents("tr").children(".productId").html();
                                var json = {
                                    id: productId
                                };
                                $("#product-message").css("display","block");
                                $("#check-picture,.allproduct").css("display","none");
                                $("#deletctView").click(function(){
                                    $("#product-message").css("display","none");
                                    $("#check-picture,.allproduct").css("display","block");
                                })
                                $.ajax({
                                    type: "GET",
                                    dataType: "json",
                                    url: "api/mjproduct/" + productId,
                                    data: JSON.stringify(json),
                                    success: function (data) {
                                        if (data.status == 0) {
                                            var proMes = eval(data.body);
                                            var inHTML = "";
                                            var url = proMes.pictureObj? proMes.pictureObj.url : '';
                                            var pictureName = proMes.pictureObj? proMes.pictureObj.name : '';
                                            var videoName = proMes.videoObj? proMes.videoObj.name : '';
                                            var videoUrl = proMes.videoObj?proMes.videoObj.finalUrl:"";
                                            var videoFormat = proMes.videoObj?proMes.videoObj.videoFormat:"";
                                            var videoSize = proMes.videoObj?proMes.videoObj.size:"";
                                            inHTML += "<tr><td>标记图片</td><td><img class='single-picture' style='width: 300px;height: 250px' src="+url+"></td></tr>";
                                            inHTML += "<tr><td>图片名称</td><td> "+pictureName+"</td></tr>";
                                            inHTML += "<tr><td>产品名称</td><td> "+proMes.title+"</td></tr>";
                                            inHTML += "<tr><td>产品描述</td><td> "+proMes.description+"</td></tr>";
                                            inHTML += "<tr><td>视频名称</td><td> "+videoName+"</td></tr>";
                                            inHTML += "<tr><td>视频地址</td><td> "+videoUrl+"</td></tr>";
                                            inHTML += "<tr><td>视频类型</td><td> "+videoFormat+"</td></tr>";
                                            inHTML += "<tr><td>视频大小</td><td> "+videoSize+"</td></tr>";
                                            $("#product-message tbody").html(inHTML);

                                        }
                                    }
                                })
                            }
                        )
                    }

                }
            }
        })
    };
    //预览图片和视频
    function preWatch(){
        preWatchPhoto();
        preWatchVideo();
        function preWatchPhoto(){
            //点击图片预览视频
            $(document).on("click",".proWatchPhoto",function(){
                var thisUrl = $(this).find(".product-picture").attr("src");
                $("#photo-model .pre-WatchPhoto").attr("src",thisUrl);
                $("#photo-model").modal("toggle");
            })
            //点击按钮预览视频
            $(document).on("click",".btn-pho",function(){
                var thisUrl = $(this).parents("tr").find(".product-picture").attr("src");
                $("#photo-model .pre-WatchPhoto").attr("src",thisUrl);
                $("#photo-model").modal("toggle");
            })
        }
        function preWatchVideo(){
            $(document).on("click",".btn-video",function() {
                var src = $(this).parents("tr").find("span").html();
                var videoHTML =  '<video width="555" height="400" controls="controls">'+
                    '<source src='+src+' type="video/mp4" />'+
                    ' <source src='+src+' type="video/ogg" />'+
                    '<source src='+src+' type="video/webm" />'+
                    '<object data='+src+' width="555" height="400">'+
                    '  <embed src='+src+' width="555" height="400" />'+
                    ' </object>'+
                    '</video>';
                console.log(src);
                $("#video-model .pre-WatchVideo").html(videoHTML);
                $("#video-model").modal("toggle");
            })
        }
    }
    //修改产品信息
    function modifyProduct(){
        $(document).on("click",".modify-product",function(){
            $("#modify-product").modal("toggle");
            var productId = $(this).parents("tr").find(".productId").html();
            var oldTitle = $(this).parents("tr").find(".oldTitle").html();
            var oldDesCribe = $(this).parents("tr").find(".oldDesCribe").html();
            console.log(oldTitle);

            $(document).on("click",".confirm-change",function(e){
                e.preventDefault();//阻止默认事件
                var newTitle = $("#productId").val() ? $("#productId").val() : oldTitle;
                var newDesCribe = $("#productMes").val() ? $("#productMes").val():oldDesCribe;
                var changeMes = {
                    id:productId,  //产品id
                    title:newTitle,//"产品名称"
                    description:newDesCribe//"产品描述"
                };
                console.log(changeMes);
                $.ajax({
                    url: "api/mjproductedit/",
                    data: JSON.stringify(changeMes),
                    type:"PATCH",
                    contentType:"application/json",
                    success: function (data) {
                        if (status == 0 ){
                            $("#closeBtn").trigger("click");
                            window.location.reload();
                        }else{
                            alert("修改失败！");
                        }
                    }
                })
            });
        });
    }
    function pagination(currentPage,totalPage){
        var htmlCode = '<nav style="padding-left:60%;margin-top:10px;"><ul class="pagination">';
        if(currentPage == 1){
            htmlCode += '<li class="disabled pre"><a href="javascript:;" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>';
        }else{
            htmlCode += '<li class="pre"><a href="javascript:;" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>';
        }
        for(var i=0;i<Math.ceil(totalPage/5);i++){
            if(i*5<currentPage && (i+1)*5>=currentPage){
                for(var j=i*5+1;j<=totalPage && j<=(i+1)*5;j++){
                    if(currentPage == j){
                        htmlCode += '<li class="page-number active"><a href="javascript:;">'+j+'</a></li>';
                    }else{
                        htmlCode += '<li class="page-number"><a href="javascript:;">'+j+'</a></li>';
                    }
                }
                break;
            }
        }
        if(currentPage == totalPage){
            htmlCode += '<li class="disabled next"><a href="javascript:;" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li></ul></nav>';
        }else{
            htmlCode += '<li class="next"><a href="javascript:;" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li></ul></nav>';
        }
        $(".allproduct").append(htmlCode);
    }
    $(document).on("click",".pre",function(){
        if(!$(this).hasClass("disabled")){
            currentPage--;
            getProductList(currentPage);
        }
    });
    $(document).on("click",".next",function(){
        if(!$(this).hasClass("disabled")){
            currentPage++;
            getProductList(currentPage);
        }
    });
    $(document).on("click",".page-number",function(){
        currentPage = parseInt($(this).find("a").html());
        getProductList(currentPage);
    });
});

