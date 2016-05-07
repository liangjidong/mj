var formData = {
    pictureId: 0,
    webUrl: '',
    title: '',
    description: '',
    arType: 1
};

$(function() {
    var userID = sessionStorage.userId;
    $('#submit-btn').click(submit);
    $('.product-form input[name!="file"]').bind('change', valueChange);
    selectProductClass();

    function selectProductClass() {
        var config = {
            defaultClass: 'web'
        };

        var control = $('#select-class'),
            productForm = $('.product-form'),
            controlLabel = 'data-product-class';

        control.delegate('button', 'click', function() {
            var selected = $(this).attr(controlLabel);
            control.trigger('productClass.change', selected);
        });

        control.bind('productClass.change', function(evt, selected) {
            control.find('>button').removeClass('active');
            control.find('>[' + controlLabel + '=' + selected + ']').addClass('active');
        });

        productForm.bind('productClass.change', function(evt, selected) {
            changeFormData(selected);
            $('.product-form input[name!="file"]').unbind('change', valueChange);
            productForm.find('>[' + controlLabel + ']').removeClass('active');
            productForm.find('>[' + controlLabel + '=' + selected + ']').addClass('active');
            $('.product-form input[name!="file"]').bind('change', valueChange);
        });

        function changeFormData(selected) {
            switch (selected) {
                case 'web':
                    formData.arType = 1;
                    formData.webUrl = '';
                    delete formData.videoId;
                    break;
                case 'video':
                    formData.arType = 2;
                    formData.videoId = 0;
                    delete formData.webUrl;
                    getVideos();
                    break;
                default:
                    console.log('无法匹配为何种类型!');
                    break;
            }
        }

        //默认激活
        control.trigger('productClass.change', config.defaultClass);
    }

    function submit() {

        var checked = checkNull();
        if (checked) {
            console.log("dddd");

            $.ajax({
                url: 'api/mjproduct?userId'+userID,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                dataType: 'json',
                success: function(res) {
                    if(res.status == 13){
                        alert("登录失效，请重新登录");
                        window.parent.location.href = "login.html";
                    }
                    else{
                    alert(res.message);
                    window.location.reload();
                    }
                }
            });
        }
    }

    function checkNull () {
        var inputs = $('.product-form input');
        for (var key in formData) {
            for (var i = 0, len = inputs.length; i < len; i++) {
                var _this = inputs.eq(i);
                if (_this.attr('name') === key) {
                    if (key === 'webUrl' || key === 'title' || key === 'description') {
                        var value = _this.val();
                    } else {
                        var value = formData[key];
                    }
                    console.log(key + '===' + value);
                    if (value === 0 || value.length === 0) {
                        var statusElement = inputs.eq(i).parent().siblings('.status');
                        statusElement.html('不能为空!');
                        return false;
                    }
                    formData[key] = value;
                }
            }
        }
        return true;
    }

    function getVideos(){ //获取链接视频列表
        $(document).on('click','.linkVideo',function(){
            $('#Modal').modal('show');
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
                        selectVideo();
                    }
                },
                error: function (data) {
                    alert("ajax获取失败");
                }
            });
            $('#Modal').on('hidden.bs.modal', function (e) {
                $('#Modal .confirm').unbind('click');
            });
        });

    }
    function selectVideo(){ //选择视频链接
        $(document).on("click","#Modal ul>a",function(){
            $('#Modal ul>a').removeClass('active');
            $(this).addClass("active");
        });
        $('#Modal .confirm').click(function(){
            var id=$('#Modal a.active').attr('data-Id');
            formData.videoId=id;
            $('#Modal').modal('hide');
            $('.linkVideo').parents('.form-group').find('.status').html('选取成功：'+formData.videoId);
        });
    }
});

function valueChange() {
    var _this = $(this),
        statusElement = _this.parent().siblings('.status');

    console.log(1);
    statusElement.text('');
}

function uploadFile(id) {
    var _self = $('#' + id),
        which,
        url,
        statusElement = _self.parent().siblings('.status');

    if (id === 'add-photo') {
        which = 'pictureId';
        url = 'picture';
    } else {
        which = 'videoId';
        url = 'video';
    }




    $.ajaxFileUpload({
        url: 'api/' + url + '/upload',
        securteuri: false,
        fileElementId: id,
        contentType: 'multipart/form-data',
        dataType: 'json',
        beforeSend: beforeSend,
        success: success
    });

    function beforeSend() {
        formData[which] = 0;
        statusElement.text('上传中...');
    }

    function success(res) {
        if(res.status == 13){
            alert("登录失效，请重新登录");
            window.parent.location.href = "login.html";
        }
        if (res.status === 0) {
            formData[which] = res.body;
            statusElement.text('上传成功！');
        } else {
            statusElement.text(res.message);
        }
    }
}

