/**
 * Created by Administrator on 2015/8/1 0001.
 */
$(function() {
    var currentPage = 1;
    var userID = sessionStorage.userId;
    if (!userID) {
        location.href = "/mj/login.html";
    }
    getProductList(currentPage);//获取产品列表
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
    function getProductList(currentPage){
        $.ajax({
            type:"GET",
            dataType:"json",
            url: "api/mjproduct/query/"+userID+"?currentPage="+currentPage,
            success:function(data) {

                if(data.status == 13){
                    alert("登录失效，请重新登录");
                    window.parent.location.href = "login.html";
                }
                if (data.status == 0) {
                    $(".pagination").empty().remove();
                    var json = data.body;
                    var strHTML = "";
                    $.each(json, function (index, el) {
                        var url = el.pictureObj ? el.pictureObj.url : '';
                        //var pictureId = el.pictureObj ? el.pictureObj.id : '';
                        var videoName = el.videoObj ? el.videoObj.name : '';
                        var publishTime = el.createtime?el.createtime:'';
                        var title = el.title?el.title:"";
                        var number = el.clicktimes?el.clicktimes:0;

                       // strHTML += "<tr><td style='border: 1px #808080;text-align: center' class='proWatchPhoto' ><img  style='border: 1px #808080;width: 80px;height: 55px;display: none' class='product-picture' src="+url+"></td>";
                        strHTML += "<tr><td  style='border: 1px #808080;text-align: center' >" + el.id + "</td>";
                        strHTML += "<td  style='border: 1px #808080;text-align: center' >" + title + "</td>";//产品名称
                        strHTML += "<td style='border: 1px #808080;text-align: center' >"+ format(publishTime, 'yyyy-MM-dd HH:mm:ss')+"</td>"
                        strHTML += "<td style='border: 1px #808080;text-align: center'  >" + number + "</td>";

                    })
                    $(".allproduct tbody").html(strHTML);
                    currentPage = data.currentPage;
                    pagination(data.currentPage,data.totalPage);
                }
            }
        })
    };
    function pagination(currentPage,totalPage){
        var htmlCode = '<nav style="padding-left:60%;margin-top:10px;"><ul class="pagination pagination-sm">';
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
    $(".pre").live("click",function(){
        if(!$(this).hasClass("disabled")){
            currentPage--;
            getProductList(currentPage);
        }
    });
    $(".next").live("click",function(){
        if(!$(this).hasClass("disabled")){
            currentPage++;
            getProductList(currentPage);
        }
    });
    $(".page-number").live("click",function(){
        currentPage = parseInt($(this).find("a").html());
        getProductList(currentPage);
    });
})
