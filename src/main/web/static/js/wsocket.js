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

    sendGroups: function (data) {
        var group = data.groups;
        var groupListHTML = "";
        for (var i = 0; i < group.length; i++){
            groupListHTML +=
                '<li>' +
                '<div class="liLeft"><img src="static/img/emoji/emoji_03.png"></div>' +
                '<div class="liRight">' +
                '<span class="hidden-groupId">' + group[i].groupId + '</span>' +
                '<span class="intername">' + group[i].groupName + '</span>' +
                '<span class="infor"></span>' +
                '</div>' +
                '</li>';
        }
            $("#groups").append(groupListHTML);
    }
};