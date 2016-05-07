/**
 * Created by Jiangli on 2015/7/26 0026.
 */
$(function() {
    var currentPage = 1;
    var userID = sessionStorage.userId;
    if(!userID) {
        location.href = "/mj/login.html";
    }

    $(".allproduct tbody").html('<p>数据加载中。。。</p>');
    var product={};
    product.getProductList=getProductList;
    product.format=format;
    product.previewModal=previewModal;
    product.getVideos=getVideos;             //获取链接视频列表
    product.selectVideo=selectVideo;             //从链接视频列表中选取
    product.deleteProduct=deleteProduct;             //删除产品
    product.modifyProduct=modifyProduct;             //修改产品
    product.lookProductInfo=lookProductInfo;             //查看产品详情

    product.getProductList(currentPage);
    trainPicture();//训练图片
    product.previewModal();//模态框预览
    product.deleteProduct();
    product.modifyProduct();
    product.lookProductInfo();


    //转换时间格式
    function format(time, format) {
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
                    if(data.status == 13){
                        alert("登录失效，请重新登录");
                        window.parent.location.href = "login.html";
                    }
                    if (data.status == 0) {
                        alert("训练成功");
                        $("#train-model .close").show();
                        $("#train-model .modal-body p").html("训练成功！");
                    }
                }
            })
        });
    }
    function getProductList(currentPage){
        //var base = "http://192.168.17.153:8081/mj/";

        $.ajax({
            type:"GET",
            dataType:"json",
            url: "api/mjproduct/query?userId="+userID+"&currentPage="+currentPage,
            success:function(data) {
                console.log("data ==" +data);
                if(data.status == 13){
                    alert("登录失效，请重新登录");
                    window.parent.location.href = "login.html";
                }
                if (data.status == 0) {
                    var productArray = data.body;
                    var basiclocal = "http://localhost:8081/mj/videos/";
                    var remotelocal = "http://120.25.241.211:8080/mj/videos/";
                    $(".allproduct tbody").empty();
                    $.each(productArray, function (index, el) {
                        $(".allproduct tbody").append($('#productTable>table>tbody').html());
                        var target=$('.allproduct tbody>tr').eq(index);

                        //数据过滤
                        var Time=product.format(el.createtime,'yyyy-MM-dd HH:mm:ss');
                        var status = "未知状态";
                        if(el.status == 0){
                            status = "已训练";
                        }else if(el.status == 1){
                            status = "未训练";
                        }
                        var picUrl=el.pictureObj&&el.pictureObj.url?el.pictureObj.url:'';
                        var videoUrl=el.videoObj&&el.videoObj.finalUrl?el.videoObj.finalUrl:'';

                        $(target).find('td').eq(0).find('img').attr('src',picUrl);   //产品id
                        $(target).find('td').eq(1).html(el.id).attr({
                            'data-webUrl': el.webUrl,
                            'data-webVideoUrl': el.webVideoUrl
                        });   //产品id
                        $(target).find('td').eq(2).html(el.title);   //产品名称
                        $(target).find('td').eq(3).html(el.description);   //产品描述
                        $(target).find('td').eq(4).html(el.clicktimes);   //产品点击量
                        $(target).find('td').eq(5).html(Time);   //创建时间
                        $(target).find('td').eq(6).html(status);   //是否训练
                        $(target).find('.photoModal').attr('data-value',picUrl);   //产品图片预览
                        $(target).find('.videoModal').attr('data-value',videoUrl);   //产品视频预览
                        $(target).find('td').eq(8).html(el.pictureId);   //图片id
                        $(target).find('td').eq(9).html(el.videoId);   //视频id
                        $(target).find('.targetModal').attr('data-value',videoUrl);   //链接视频
                    });
                }
                $(".pagination").empty().remove();
                currentPage = data.currentPage;
                 pagination(data.currentPage,data.totalPage);
            }
        })
    }


    function previewModal(){
        $('.allproduct').on('click','button',function(){
            //var src = $(this).find(".videoModal").attr("data-value");
            //console.log("src ==" +src);
            var src = "";
            var modalData={
                photo:{
                    title:'预览图片',
                    body:'<p><img alt="产品图片"></p>'
                },
                video:{
                    title:'预览视频',
                    body:'<video width="555" height="400" controls="controls">'+
                    '<source src='+src+' type="video/mp4" />'+
                    ' <source src='+src+' type="video/ogg" />'+
                    '<source src='+src+' type="video/webm" />'+
                    '<object data='+src+' width="555" height="400">'+
                    '  <embed src='+src+' width="555" height="400" />'+
                    ' </object>'+
                    '</video>'
                },
                target:{
                    title:'链接视频',
                    body:'<ul class="list-group"></ul>'
                }/*,
                 modification:{
                 title:'修改产品',
                 body:$('#modifyForm').html()
                 }*/
            };

            var target=$(this).attr('data-name');
            var value=$(this).attr('data-value');
            if(modalData.hasOwnProperty(target)){
                $('#Modal').modal('show');
                $('#Modal .modal-title').html(modalData[target].title);
                $('#Modal .modal-body').html(modalData[target].body);
                var productId=$(this).parents('tr').find('.productId').html();
                var oldTitle = $(this).parents("tr").find(".oldTitle").html();
                var oldIntro = $(this).parents("tr").find(".oldIntro").html();

                switch (target){
                    case 'photo':
                        $('#Modal .modal-body img').attr('src',value);
                        break;
                    case 'video':  //预览链接视频
                        $('#Modal .modal-body video').attr('src',value);

                        break;
                    case 'target':
                        product.getVideos(productId);
                        break;
                    /*case 'modification': //修改产品
                        modifyProduct(productId,oldTitle,oldIntro);
                        break;*/
                    default:
                        $('#Modal .modal-body').html('<p>啊，被你发现了</p>');
                }
            }
        });
    }
    function getVideos(productId){ //获取链接视频列表
        $.ajax({
            type: "GET",
            dataType: "json",
            url: "api/video/queryAll",

            success: function (data) {
                if(data.status == 13){
                    alert("登录失效，请重新登录");
                    window.parent.location.href = "login.html";
                }
                if (data.status == 0) {
                    //$(".pagination").empty().remove();
                    var ulHTML = "";
                    if(data.body){
                        $.each(data.body, function (index,el) {
                            ulHTML += '<a href="#" class="list-group-item" data-Id="'+el.id+'">'+el.name+'</a>';
                        });
                    }else{
                        ulHTML='<li>暂时还没有视频哦</li>';
                    }
                    $('#Modal ul').append(ulHTML);
                    /*currentPage = data.currentPage;
                     pagination(data.currentPage,data.totalPage);*/
                    product.selectVideo(productId);
                }
            },
            error: function (data) {
                alert("ajax获取失败");
            }
        });
        $('#Modal').on('hidden.bs.modal', function (e) {
            $('#Modal .confirm').unbind('click');
        });
    }
    function selectVideo(productId){ //选择视频链接
        $(document).on("click","#Modal ul>a",function(){
            $('#Modal ul>a').removeClass('active');
            $(this).addClass("active");
        });
        $('#Modal .confirm').click(function(){
            var id=$('#Modal a.active').attr('data-Id');
            $.ajax({
                type: "PATCH",
                url: "api/mjproduct/changeVideo?productId="+productId+"&videoId="+id,
                success:function(response){
                    if(response.status == 13){
                        alert("登录失效，请重新登录");
                        window.parent.location.href = "login.html";
                    }
                    //videotype.html(newVideoId);
                    if(response.status==0){
                        alert('链接成功');
                        $('#Modal').modal('hide');
                    }
                }
            })
        });
    }
    function deleteProduct() {
        $(".allproduct").on('click','.deleteProduct',function () {
            var value=confirm('是否确认删除？');
            if(value){
                var productId = $(this).parents("tr").children(".productId").html();
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: "api/mjproduct/" + productId,
                    success: function (data) {
                        if(data.status == 13){
                            alert("登录失效，请重新登录");
                            window.parent.location.href = "login.html";
                        }
                        if (data.status == 0) {
                            alert("删除成功");
                            location.reload();
                        }
                    }
                })
            }
        })
    }
    function modifyProduct(){
        $(document).on('click','.modifyModal',function(){
            $('#Modification').modal('show');
            var productId=$(this).parents('tr').find('.productId').html();
            var oldTitle = $(this).parents("tr").find(".oldTitle").html();
            var oldIntro = $(this).parents("tr").find(".oldIntro").html();
            var oldWebUrl = $(this).parents("tr").find(".productId").attr('data-webUrl');
            var oldWebVideoUrl = $(this).parents("tr").find(".productId").attr('data-webVideoUrl');

            $('#productName').val(oldTitle);
            $('#productIntro').val(oldIntro);
            $('#web-video').val(oldWebVideoUrl);
            $('#web-url').val(oldWebUrl);
            $('#ModificationLabel').attr('data-Id',productId);
        });
        $('#Modification').on('click','.confirm',function(){
            var data = {
                id:$('#ModificationLabel').attr('data-Id'),  //产品id
                title:$('#productName').val(),//"产品名称"
                description:$('#productIntro').val(),//"产品描述"
                webUrl: $('#web-url').val(),
                webVideoUrl: $('#web-video').val()
            };
            $.ajax({
                url: "api/mjproductedit/",
                data: JSON.stringify(data),
                type:"PATCH",
                contentType:"application/json",
                success: function (data) {
                    if(data.status == 13){
                        alert("登录失效，请重新登录");
                        window.parent.location.href = "login.html";
                    }
                    if (data.status == 0 ){
                        alert('修改成功！');
                        //$('#Modification').modal('hide');
                        window.location.reload();
                    }else{
                        alert("修改失败！");
                    }
                }
            });
        });
    }
    function lookProductInfo(){
        $(document).on('click','.watch-product',function(){
            var productId=$(this).parents('tr').find('.productId').html();
            sessionStorage.productId=productId;
            location.href='productInfo.html';
        });
    }

    //分页
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