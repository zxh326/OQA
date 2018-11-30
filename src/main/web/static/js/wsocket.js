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

}