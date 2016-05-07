/**
 * Created by jiangli on 2015/6/11 0011.
 */
$(function(){
   var userID = sessionStorage.userId;
    //如果未登录直接跳转到登陆界面
    if(!userID) {
        location.href = "/mj/login.html";
    }
    manageRecord();//管理日志
    deleteUser();//删除用户
    //用户信息管理
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "api/user/query",
        success: function (data) {
            if (data.status == 0) {
                var userMessage = eval(data.body);
                var strHTML = "";
                $.each(userMessage, function (index,value) {
                    var nickName = value.nickName?value.nickName:"";
                    var phoneNumber = value.phoneNumber?value.phoneNumber:"";
                    strHTML += "<tr><td class='id' style='display: none'>" + userMessage[index].id + "</td>";
                    strHTML += "<td width='15%'class='user-nickname'>" + nickName + "</td>";
                    strHTML += "<td width='20%'class='username'>" + userMessage[index].name + "</td>";
                    strHTML += "<td width='20%' class='phoneNumber'>" + phoneNumber + "</td>";
                    strHTML += "<td style='display:none'class='groupId'>" +userMessage[index].groupId+ "</td>";
                    strHTML += "<td style='display:none'class='headImg'>" +userMessage[index].headImg+ "</td>";
                    strHTML += "<td style='display:none'class='adress'>" +userMessage[index].adress+ "</td>";
                    strHTML += "<td style='display:none'class='status'>" +userMessage[index].status+ "</td>";
                    strHTML += "<td style='display:none'class='paymentInformation'>" +userMessage[index].paymentInformation+ "</td>";
                    strHTML += "<td style='display:none'class='qqNumber'>" +userMessage[index].qqNumber+ "</td>";
                    strHTML += "<td style='display:none'class='keepword1'>" +userMessage[index].keepword1+ "</td>";
                    strHTML += "<td style='display:none'class='keepword2'>" +userMessage[index].keepword2+ "</td>";
                    strHTML += "<td style='display:none'class='email'>" +userMessage[index].email+ "</td>";
                    strHTML += "<td style='display:none'class='keepword3'>" +userMessage[index].keepword3+ "</td>";
                    strHTML += "<td style='display: none' width='15%' class='password'>" +userMessage[index].password+ "</td>";
                    strHTML += "<td width='15%' class='role'>" +userMessage[index].role+ "</td>";
                    strHTML += "<td><button class='watch-detail btn btn-success btn-sm' >查看详情</button><button  class='watch-doc btn btn-warning btn-sm'>查看日志记录</button><button class='delect-user btn btn-danger btn-sm'>删除</button></td>";
                    strHTML += "</tr>";
                })
                    $("#all-user-message").html(strHTML);
                    $(document).on("click",".modify-role",function(e){
                        e.stopPropagation();
                    });
                    //修改用户权限
                    $(document).on("click",".role",function(){
                        var roleNumber = 0;
                        if($(this).find(".modify-role").length>0){
                            $(".modify-role").empty().remove();
                            return false;
                        }else{
                            $(".modify-role").empty().remove();
                            roleNumber = parseInt($(this).html());
                        }
                        $(this).css("position","relative");
                        window.id = parseInt($(this).parent().find(".id").html());
                        var html = '<div class="modify-role" style="text-align:center;background:#F1F1F1;width:200px;height:150px;box-shadow: -1px -1px 1px 0px #CCC;border:solid 1px #CCC;position:absolute;border-radius: 4px;top: 15px;left: -200px;padding:15px;">'+
                            '<p style="font-size: 16px;font-weight: 600;margin-bottom: 15px;">修改用户权限</p><label class="radio-inline" style="font-size: 14px;">'+
                            '<input type="radio" name="type" id="type1" value="1">普通用户'+
                            '</label>'+
                            '<label class="radio-inline" style="font-size: 14px;">'+
                            '<input type="radio" name="type" id="type2" value="2"> 管理员'+
                            '</label><button type="button" class="btn btn-info submit-role" style="margin-top: 20px;width: 150px;">提交</button></div>';
                        $(this).append(html);
                        if(roleNumber == 1){
                            $("#type1").prop("checked",true);
                        }else{
                            $("#type2").prop("checked",true);
                        }
                    });
                    
                    $(document).on("click",".submit-role",function(){
                        var role = 0;
                        if($("#type1").prop("checked")){
                            role = 1;
                        }else{
                            role = 2;
                        }
                        $.ajax({
                            url:"api/userlevel?userId="+window.id+"&type="+role,
                            type:"PATCH",
                            dataType:"json",
                            success:function(data){
                                if(data.status == 0){
                                    location.reload();
                                }
                            }
                        });
                    });

                    $(document).on("click",".user-nickname,.watch-detail",function(){
                           $(".person-submit").hide();
                           $(".all-user-message").css("display","none");
                           $(".per-user-message").css("display","block");
                           $(document).on("click","#user-back",function(){
                               $(".all-user-message").css("display","block");
                               $(".per-user-message").css("display","none");
                               $(".per-img-manage").css("display","none");
                               $(".per-video-manage,.record-manage").css("display","none");
                           })
                            var  groupId = $(this).parents("tr").children(".groupId").html();
                            var  headImg = $(this).parents("tr").children(".headImg").html();
                            var  adress = $(this).parents("tr").children(".adress").html();
                            var  status = $(this).parents("tr").children(".status").html();
                            var  paymentInformation = $(this).parents("tr").children(".paymentInformation").html();
                            var  qqNumber = $(this).parents("tr").children(".qqNumber").html();
                            var  keepword1 = $(this).parents("tr").children(".keepword1").html();
                            var  keepword2 = $(this).parents("tr").children(".keepword2").html();
                            var  email = $(this).parents("tr").children(".email").html();
                            var  keepword3 = $(this).parents("tr").children(".keepword3").html();
                            var  id = $(this).parents("tr").children(".id").html();
                            var  nickname = $(this).parents("tr").children(".user-nickname").html();
                            var  phoneNumber = $(this).parents("tr").children(".phoneNumber").html();
                            var  urole = $(this).parents("tr").children(".role").html();
                            var  username = $(this).parents("tr").children(".username").html();
                            var  password = $(this).parents("tr").children(".password").html();
                            groupId = (groupId != "null")?groupId:"";
                            status = (status != "null")?status:"";
                            phoneNumber = (phoneNumber != "null")?phoneNumber:"";
                            adress = (adress != "null")?adress:"";
                            paymentInformation = (paymentInformation != "null")?paymentInformation:"";
                            qqNumber = (qqNumber != "null")?qqNumber:"";
                            email = (email != "null")?email:"";
                            keepword2 = (keepword2 != "null")?keepword2:"";
                            keepword3 = (keepword3 != "null")?keepword3:"";
                            var  message = [headImg,nickname,username,id,groupId,phoneNumber,status,adress,urole,paymentInformation,qqNumber,email,keepword1,
                                           keepword2,keepword3];
                            $(".personal-message p").each(function(index){
                                $(this).children("span[class!='img']").html(message[index]);
                             })

                             $(".personal-message img").attr("src",headImg)
                        //修改用户信息
                        $(document).on("click",".person-modify",function(){

                            $(".personal-message span[class!='img']").html("<input type='text'/><span class='status1'>*若不填或输入为空，为原值</span>");
                            $(".user-role span").html("<input id='ROLE' type='text'/><span class='status2'>*1表示普通用户，2表示管理员(不填或输入格式错误为原值)</span>")
                            $(".user-ID span").html("<input id='userID' type='text' value="+message[3]+" disabled/>");
                            $(".user-NAME span").html("<input id='userNAME' type='text' value="+message[2]+" disabled/>");
                            $(".person-submit").show();
                            //提交用户信息
                            $(document).on("click",".person-submit",function(){
                                var role = $("#ROLE").val();
                                //用户角色，输入为空或不合法为原值
                                if(!(role == 1 || role == 2)){
                                    $("#ROLE").val(message[8]);
                                }
                                //输入为空为原值
                                $(".personal-message input").each(function(index,el){
                                    var that = $(this);
                                    if(that.val() == null || that.val() == ""){
                                        that.val(message[index+1]);
                                    }
                                })
                                //传送的数据
                                var data1 = {
                                    adress: $(".personal-message input:eq(5)").val(),
                                    email: $(".personal-message input:eq(10)").val(),
                                    groupId:$(".personal-message input:eq(3)").val(),
                                    headImg:headImg,
                                    id: $(".personal-message input:eq(2)").val(),
                                    keepword1:$(".personal-message input:eq(11)").val(),
                                    keepword2:$(".personal-message input:eq(12)").val(),
                                    keepword3: $(".personal-message input:eq(13)").val(),
                                    name: $(".personal-message input:eq(1)").val(),
                                    nickName: $(".personal-message input:eq(0)").val(),
                                    paymentInformation: $(".personal-message input:eq(8)").val(),
                                    phoneNumber: $(".personal-message input:eq(4)").val(),
                                    qqNumber: $(".personal-message input:eq(9)").val(),
                                    role: $(".personal-message input:eq(7)").val(),
                                    status: $(".personal-message input:eq(6)").val()
                                }

                                 for(var c in data1){
                                     data1[c] == "null"?delete data1[c]:data1[c];
                                 }
                                console.log(data1);
                                $.ajax({
                                    url: "api/user",
                                    type: "PATCH",
                                    contentType: "application/json",
                                    data: JSON.stringify(data1),
                                    success: function (data) {
                                        if (data.status == 0) {
                                              alert("修改成功！");
                                               window.location.reload();//刷新当前页面

                                        }
                                    }
                                })
                            })

                        })
                           //修改用户密码
                            $(document).on("click",".person-password",function(e){
                                e.preventDefault();
                                $("#inputPassword").focus();
                                $("#change-password").modal("toggle");
                                checkNull();
                                checkIsSame();
                                $(document).on("click",".add-modify",function(){
                                    checkNull();
                                    checkIsSame();
                                    var strHTML= "";
                                    $("#change-password .status").each(function(){
                                        strHTML += $(this).html();
                                    })
                                    if(strHTML== ""||strHTML==null){

                                        var data2 = {
                                            adress:adress,
                                            email: email,
                                            groupId:groupId,
                                            headImg:headImg,
                                            id: id,
                                            keepword1:keepword1,
                                            keepword2:keepword2,
                                            keepword3:keepword3,
                                            name: username,
                                            nickName:nickname,
                                            password:$("#inputPassword").val(),
                                            paymentInformation: paymentInformation,
                                            phoneNumber: phoneNumber,
                                            qqNumber: qqNumber,
                                            role: urole,
                                            status: status
                                        }
                                        $.ajax({
                                            url: "api/user",
                                            type: "PATCH",
                                            contentType: "application/json",
                                            data: JSON.stringify(data2),
                                            success: function (data) {
                                                if (data.status == 0) {
                                                    alert("修改成功！");
                                                    window.location.reload();//刷新当前页面
                                                }
                                            }
                                        })

                                    }
                                    else{
                                        alert("请检查输入格式是否正确！")
                                    }
                                })
                            })
                            //图像管理
                                $(document).on("click",".person-img",function(){
                                $(".person-submit").hide();
                                $(".per-user-message").css("display","none");
                                $(".per-img-manage").css("display","block");
                                $(document).on("click","#picture-back",function(){
                                    $(".all-user-message").css("display","none");
                                    $(".per-user-message").css("display","block");
                                    $(".per-img-manage").css("display","none");
                                    $(".per-video-manage,.record-manage").css("display","none");
                                })
                                var btn = "<div class='btn-group'><button class=' btn btn-primary kkkk btn-sm'>选择链接视频</button>"
                                    btn +="<button class='btn btn-sm btn-primary dropdown-toggle' data-toggle='dropdown'>"
                                    btn +="<span class='caret'></span></button><ul class='dropdown-menu'> </ul></div>"
                                $.ajax({
                                    type:"GET",
                                    dataType:"json",
                                    url:"api/picture/query?userId="+id,
                                    success:function(data) {
                                        if (data.status == 0) {
                                            var picturejson = eval(data.body);
                                            var strHTML = "";
                                            $.each(picturejson, function (index) {
                                                strHTML += "<tr><td><img width='170px' height='100px' src=" + picturejson[index].url + "></td>";
                                                strHTML += "<td>" + picturejson[index].name + "</td>";
                                                strHTML += "<td class='pictureid'>" + picturejson[index].id + "</td>";
                                                strHTML += "<td>" + picturejson[index].quality + "</td>";
                                                strHTML += "<td>" + picturejson[index].type + "</td>";
                                                strHTML += "<td id=" + index + ">" + btn + "</td>";
                                                strHTML += "<td class='videotype'>" + picturejson[index].videoId + "</td>";

                                            })
                                            $("tbody").html(strHTML);
                                            $.ajax({
                                                type: "GET",
                                                dataType: "json",
                                                url: "api/video/query?userId=" + id,
                                                success: function (data) {
                                                    if (data.status == 0) {
                                                        var videojson = eval(data.body);
                                                        var ulHTML = "";
                                                        $.each(videojson, function (index) {
                                                            ulHTML += "<li id=" + index + "'>" + videojson[index].name + "</li>";
                                                            $(".dropdown-menu").html(ulHTML);
                                                        })
                                                    }
                                                    $(document).on("click",".dropdown-menu li",function(){
                                                        $(this).parents(".dropdown-menu").css("display", "none");
                                                        var pIndex = $(this).parents("tr").children(".pictureid").html();
                                                        var pIdx = parseInt(pIndex);
                                                        var vIndex = $(this).attr("id");
                                                        var vIdx = parseInt(vIndex);
                                                        var videoId = videojson[vIdx].id;
                                                        $(this).parents("tr").children(".videotype").html(videojson[vIdx].id);
                                                        $.ajax({
                                                            type: "PATCH",
                                                            url: "api/picture/addvideo?pictureId=" + pIdx + "&videoId=" + videojson[vIdx].id
                                                                +"&userId="+id
                                                        });

                                                    })
                                                }
                                            })
                                            $(document).on("click",".dropdown-toggle",function(){
                                                console.log($(this).parents("td").children("ul"));
                                                $(this).parents(".btn-group").children("ul").css("display", "block");
                                            })
                                        }
                                    }
                                })
                            })
                            //视频管理
                            $(".person-video").click(function(){
                                $(".person-submit").hide();
                                $(".per-user-message").css("display","none");
                                $(".per-video-manage").css("display","block");
                                $(document).on("click","#video-back",function(){
                                    $(".all-user-message").css("display","none");
                                    $(".per-user-message").css("display","block");
                                    $(".per-img-manage").css("display","none");
                                    $(".per-video-manage,.record-manage").css("display","none");
                                })
                                $.ajax({
                                    type:"GET",
                                    dataType:"json",
                                    url:"api/video/query?userId="+id,
                                    success:function(data) {
                                        if (data.status == 0) {
                                            var json = eval(data.body);
                                            var strHTML = "";
                                            $.each(json, function (index) {
                                                strHTML += "<tr><td>" + json[index].name + "</td>";
                                                strHTML += "<td>" + json[index].id + "</td>";
                                                strHTML += "<td>" + json[index].size + "</td>";
                                                strHTML += "<td>" + json[index].originUrl + "</td>";
                                                strHTML += "<td>" + json[index].finalUrl + "</td>";
                                                strHTML += "<td>" + json[index].uploadWay + "</td></tr>";
                                                $("tbody").html(strHTML);
                                            })
                                        }
                                    }
                                })
                            })
                     })

            }
            else if(data.status == 13){
                alert("对不起，非管理员用户没有此操作权限！！");
            }else{
                alert("出现错误！");
            }
        }
    })
    //管理用户日志
    function manageRecord(){
        $(document).on("click",".watch-doc",function(){
            $(".record-manage").css("display","block");
            $(".all-user-message").css("display","none");
            $(".per-img-manage").css("display","none");
            $(".per-video-manage,.per-user-message").css("display","none");
            var userId = $(this).parents("tr").children(".id").html();
            //返回按钮
            $(document).on("click","#doc-back",function(){
                $(".record-manage").css("display","none");
                $(".all-user-message").css("display","block");
                $(".per-img-manage").css("display","none");
                $(".per-video-manage,.per-user-message").css("display","none");
            })
            var format = function (time, format){
                var t = new Date(time);
                var tf = function(i){return (i < 10 ? '0' : '') + i};
                return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
                    switch(a){
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
            $.ajax({
                type: "GET",
                dataType: "json",
                url: "api/loghistory/query/"+userId,
                success: function (data) {
                    if (data.status == 0) {
                        var docMessage = eval(data.body);
                        var strHTML = "";
                        $.each(docMessage, function (index,el) {
                            strHTML +="<tr><th class='this-id'>"+el.id+"</th>";
                            strHTML += "<th>"+el.userId+"</th>";
                            strHTML += "<th>"+format(el.operatonTime, 'yyyy-MM-dd HH:mm:ss')+"</th>";
                            strHTML += "<th>"+el.operationContent+"</th>";
                            strHTML += "<th><button class='delete-doc btn btn-sm btn-danger'>删除</button></th></tr>";
                        })
                        $("#record-manage").html(strHTML);
                        //删除日志
                        $(document).on("click",".delete-doc",function(){
                            var thisID = $(this).parents("tr").children(".this-id").html();
                            var thisTr =  $(this).parents("tr");
                            $.ajax({
                                type: "DELETE",
                                url: "api/loghistory/"+thisID,
                                success:function(data){
                                    if(data.status == 0){
                                        alert("删除成功！");
                                        thisTr.css("display","none");
                                    }
                                }
                            })
                        })
                    }
                }
            })
        })
    }
    //删除用户
    function deleteUser(){
        $(document).on("click",".delect-user",function(){
            var thisID = $(this).parents("tr").children(".id").html();
            $.ajax({
                type: "DELETE",
                url: "api/user/" + thisID,
                success: function (data) {
                    if (data.status == 0) {
                        alert("删除成功！");
                        window.location.reload();//刷新当前页面
                    }
                }
            })
        })
    }
    //判断输入是否为空
    function isNull( str ){
        if ( str == "" ) {
            return true;
        }
        var regu = "^[ ]+$";
        var re = new RegExp(regu);
        return re.test(str);
    };
    function checkNull(){
        for(var i=0;i<3;i++){
            $(" input").blur(function(){
                var str = $(this).val();
                if(isNull(str)){
                    $(this).parents(".col-sm-8").children("p").html("*输入不能为空！");}
            }).focus(function(){
                $(this).parents(".col-sm-8").children("p").html("");
            });
        }
    }
    //确认两次密码输入是否一致
    function checkIsSame() {
        $("#confirmPassword").blur(function () {
            if ($(this).val() != "") {
                var val1 = $("#inputPassword").val();
                var val2 = $(this).val();
                if (val1 != val2) {
                    $(this).parents(".col-sm-8").children("p").html("*两次密码输入不一致！");
                }
            }
        });
    }
})