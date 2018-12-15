function getUserInfo() {
    $.ajax({
        url:'oqa/get_userinfo',
        type: 'GET',
        dataType: 'json',
        async: true,
        success:function (data) {
            console.log(data);
            userId = data.data.user.userId;
        }
    })
}


var ws ={
    register: function() {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState === WebSocket.OPEN) {
            var data = {
                "userId" : userId,
                "type" : "REGISTER"
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("Websocket连接没有开启！");
        }
    },

    registerReceive: function() {
        console.log("userId为 " + userId + " 的用户登记到在线用户表成功！");
    },

    sendInfos: function (data) {
        var groups = data.groups;
        var teachers = data.teachers;
        var groupListHTML = "";
        var teachListHTML = "";

        if (teachers.length === 0){
            $("teachers").append('<li><div>当前没有教师在线</div></li>')
        }
        if (groups.length === 0){
            $("groups").append('<li><div>没有加群</div></li>')
        }
        for(var i = 0;i < teachers.length;i++){
            teachListHTML += '<li>' +
                '<div class="liLeft"><img src="'+ teachers[i].avatarUrl+'"></div>' +
                '<div class="liRight">' +
                '<span class="hidden-groupId">' + teachers[i].userId + '</span>' +
                '<span class="intername">' + teachers[i].loginId + '</span>' +
                '<span class="infor"></span>' +
                '</div>' +
                '</li>';
        }
        for (var i = 0; i < groups.length; i++){
            groupListHTML +=
                '<li>' +
                '<div class="liLeft"><img src="static/img/emoji/emoji_03.png"></div>' +
                '<div class="liRight">' +
                '<span class="hidden-groupId">' + groups[i].groupId + '</span>' +
                '<span class="intername">' + groups[i].groupName + '</span>' +
                '<span class="infor"></span>' +
                '</div>' +
                '</li>';
        }
        $("#teachers").append(teachListHTML)
        $("#groups").append(groupListHTML);
        $('.conLeft ul li').on('click', friendLiClickEvent);
    }
};


function friendLiClickEvent() {
    $(this).addClass('bg').siblings().removeClass('bg');

    // 2. 设置显示右侧消息框
    $('.conRight').css("display", "-webkit-box");
    var intername=$(this).children('.liRight').children('.intername').text();
    var toUserId = $(this).children('.liRight').children('.hidden-userId').text();
    var toGroupId = $(this).children('.liRight').children('.hidden-groupId').text();
    $('.headName').text(intername);
    $('#toUserId').val("");
    $('#toGroupId').val("");
}