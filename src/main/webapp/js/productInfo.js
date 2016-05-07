/**
 * Created by Administrator on 2016/1/6.
 */
$(function(){
    getProduct();
    function getProduct(){
        console.log(sessionStorage.productId);
        var data={
            id:sessionStorage.productId
        };
        $.ajax({
            type: "GET",
            dataType: "json",
            url: "api/mjproduct/" + sessionStorage.productId,
            data: JSON.stringify(data),
            success: function (response) {
                var target=response.body;
                var picUrl=target.pictureObj&&target.pictureObj.url?target.pictureObj.url:'';
                var videoUrl=target.videoObj&&target.videoObj.finalUrl?target.videoObj.finalUrl:'';
                var videoName=target.videoObj&&target.videoObj.name?target.videoObj.name:'';
                var arType=target.arType==1?'关联网页':target.arType==2?'关联视频':'没有关联';
                var status = "未知状态";
                if(target.status == 0){
                    status = "已训练";
                }else if(target.status == 1){
                    status = "未训练";
                }

                $('ul>li').eq(0).find('img').attr('src',picUrl);//标记图片：
                $('ul>li').eq(1).find('div:last-child').html(target.title||'无');//产品名称：
                $('ul>li').eq(2).find('div:last-child').html(target.description||'无');//产品描述：
                $('ul>li').eq(3).find('div:last-child').html(videoName||'无');//视频名称：
                $('ul>li').eq(4).find('div:last-child').html(arType||'未定');//关联类型：
                $('ul>li').eq(5).find('div:last-child').html(target.webUrl||'无');//关联网址：
                $('ul>li').eq(6).find('div:last-child').html(status);//是否训练：
                $('ul>li').eq(7).find('div:last-child').html(target.clicktimes||0);//点击量：
            }
        });
    }
});