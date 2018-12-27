function getUserInfo() {
    $.ajax({
        url:'oqa/get_userinfo',
        type: 'GET',
        dataType: 'json',
        async: true,
        success:function (data) {
            console.log(data);
            userId = data.data.user.userId;
            $("#username").html(data.data.user.userId);

            if (data.data.user.userRole === 1){
                $("#teachers").remove();
                $("#groups_title").remove();
            }else{
                $("#modal-create").remove();
            }
            $("#avatarUrl").attr("src", data.data.user.avatarUrl);
        }
    })
}


function logout() {
    // 1. 关闭websocket连接
    ws.remove();

    // 2. 注销登录状态
    $.ajax({
        type : 'POST',
        url : '/auth/logout',
        dataType: 'json',
        async : true,
        success: function(data) {
            if (data.status == 200) {
                console.log("注销成功！");
                window.location.href="login";
            } else {
                alert(data.msg);
            }
        }
    });
}

var sentMessageMap = new SentMessageMap();

function setSentMessageMap(key) {
    sentMessageMap.put(key, []);
}


// 绑定发送按钮回车事件
$('#dope').keydown(function(e) {
    if (e.keyCode == 13) {
        $('.sendBtn').trigger('click');
        e.preventDefault(); //屏蔽enter对系统作用。按后增加\r\n等换行
    }
});

// 绑定发送按钮点击事件
$('.sendBtn').on('click',function(){
    var fromUserId = userId;
    var toUserId = $('#toUserId').val();
    var toGroupId = $('#toGroupId').val();
    var news = $('#dope').val();
    if (toUserId == '' && toGroupId == '') {
        alert("请选择对话方");
        return;
    }
    if(news == ''){
        alert('消息不能为空');
        return;
    } else {
        if (toUserId.length != 0) {
            ws.singleSend(fromUserId, toUserId, news);
        } else {
            ws.groupSend(fromUserId, toGroupId, news);
        }

        $('#dope').val('');
        var avatarUrl = $('#avatarUrl').attr("src");
        var msg = '';
        msg += '<li>'+
            '<div class="news">' + news + '</div>' +
            '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
            '</li>';

        // 消息框处理：
        processMsgBox.sendMsg(msg, toUserId, toGroupId)

        // 好友列表处理：
        var $sendLi = $('.conLeft').find('li.bg');
        processFriendList.sending(news, $sendLi);
    }
})

$('.ExP').on('mouseenter',function(){
    $('.emjon').show();
})

$('.emjon').on('mouseleave',function(){
    $('.emjon').hide();
})

$('.emjon li').on('click',function(){
    var imgSrc=$(this).children('img').attr('src');
    $('.emjon').hide();
    var fromUserId = userId;
    var toUserId = $('#toUserId').val();
    var toGroupId = $('#toGroupId').val();
    var content  = '<img class="Expr" src="' + imgSrc + '">';
    if (toUserId == '' && toGroupId == '') {
        alert("请选择对话方");
        return;
    }
    if (toUserId.length != 0) {
        ws.singleSend(fromUserId, toUserId, content);
    } else {
        ws.groupSend(fromUserId, toGroupId, content);
    }
    var avatarUrl = $('#avatarUrl').attr("src");
    var msg = '';
    msg += '<li>'+
        '<div class="news">' + content + '</div>' +
        '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
        '</li>';
    processMsgBox.sendMsg(msg, toUserId, toGroupId);
    var $sendLi = $('.conLeft').find('li.bg');
    content = "[图片]";
    // processFriendList.sending(content, $sendLi);
})



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

    registerReceive: function(data) {
        console.log("userId为 " + userId + " 的用户登记到在线用户表成功！");
    },

    sendInfos: function (data) {
        var groups = data.groups;
        var teachers = data.teachers;
        var groupListHTML = "";
        var teachListHTML = "";

        console.log(teachers)

        if (teachers.length === 0) {
            $("#teachers").append('<li id="tnone"><div>当前没有教师在线</div></li>')
        }
        if (groups.length === 0) {
            $("#groups").append('<li><div>没有加群</div></li>')
        }
        for (var i = 0; i < teachers.length; i++) {
            setSentMessageMap(teachers[i].userId)
            teachListHTML += '<li id="t'+ teachers[i].userId + '">' +
                '<div class="liLeft"><img src="' + teachers[i].avatarUrl + '"></div>' +
                '<div class="liRight">' +
                '<span class="hidden-userId">' + teachers[i].userId + '</span>' +
                '<span class="intername">' + teachers[i].loginId + '</span>' +
                '<span class="infor"></span>' +
                '</div>' +
                '</li>';
        }
        for (var i = 0; i < groups.length; i++) {
            setSentMessageMap(groups[i].groupId)
            groupListHTML +=
                '<li id="g'+ groups[i].groupId + '">' +
                '<div class="liLeft"><img src="static/img/group.jpg"></div>' +
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
    },

    singleSend: function(fromUserId, toUserId, content){
        var data = {
            "fromUserId": parseInt(fromUserId),
            "toUserId": parseInt(toUserId),
            "content": content,
            "type": "SINGLESEND"
        };

        socket.send(JSON.stringify(data));
        console.log(fromUserId, toUserId, content)
    },

    receiveSingle: function(data){
      console.log(data);

        console.log(data);
        var fromUserId = data.fromUserId;
        var content = data.content;
        var fromAvatarUrl = data.avatarUrl;
        var $receiveLi;
        if (sentMessageMap.get(fromUserId) === false){
            setSentMessageMap(fromUserId)
            $("#groups").append(
                '<li id="t'+ fromUserId + '">' +
                '<div class="liLeft"><img src="' + fromAvatarUrl + '"></div>' +
                '<div class="liRight">' +
                '<span class="hidden-userId">' + fromUserId + '</span>' +
                '<span class="intername">' + fromUserId + '</span>' +
                '<span class="infor"></span>' +
                '</div>' +
                '</li>'
            );
            $('.conLeft ul li').on('click', friendLiClickEvent);
        }
        $('.conLeft').find('span.hidden-userId').each(function(){
            if (this.innerHTML === fromUserId) {
                fromAvatarUrl = $(this).parent(".liRight")
                    .siblings(".liLeft").children('img').attr("src");
                $receiveLi = $(this).parent(".liRight").parent("li");
            }
        })
        var answer='';
        answer += '<li>' +
            '<div class="answers">'+ content +'</div>' +
            '<div class="answerHead"><img src="' + fromAvatarUrl + '" alt=""/></div>' +
            '</li>';

        // 消息框处理
        processMsgBox.receiveSingleMsg(answer, fromUserId);
    },
    groupSend: function(fromUserId, toGroupId, content) {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            var data = {
                "fromUserId" : parseInt(fromUserId),
                "toGroupId" : parseInt(toGroupId),
                "content" : content,
                "type" : "GROUPSEND"
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("Websocket连接没有开启！");
        }
    },



    groupReceive: function(data) {
        // 获取、构造参数
        console.log(data);
        var fromUserId = data.fromUserId;
        var content = data.content;
        var toGroupId = data.toGroupId;
        var fromAvatarUrl = data.avatarUrl;
        var $receiveLi;
        $('.conLeft').find('span.hidden-groupId').each(function(){
            if (this.innerHTML == toGroupId) {
                $receiveLi = $(this).parent(".liRight").parent("li");
            }
        })
        var answer='';
        answer += '<li>' +
            '<div class="answers">'+ content +'</div>' +
            '<div class="answerHead"><img src="' + fromAvatarUrl + '"/></div>' +
            '</li>';

        // 消息框处理
        processMsgBox.receiveGroupMsg(answer, toGroupId);
        // 列表处理
        // processFriendList.receiving(content, $receiveLi);
    },

    teacherOnline: function (data) {
        var teacher = data.teacher;
        $("#tnone").remove();
        setSentMessageMap(teacher.userId)
        $("#teachers").append(
            '<li id="t'+ teacher.userId + '">' +
            '<div class="liLeft"><img src="' + teacher.avatarUrl + '"></div>' +
            '<div class="liRight">' +
            '<span class="hidden-userId">' + teacher.userId + '</span>' +
            '<span class="intername">' + teacher.loginId + '</span>' +
            '<span class="infor"></span>' +
            '</div>' +
            '</li>'
        )
        $('.conLeft ul li').on('click', friendLiClickEvent);
    },

    teacherDown: function (data) {
        var teacher = data.teacher;
        var s = "#t"+ teacher.userId;
        $(s).remove();
    }
};

var processMsgBox = {
    sendMsg: function (msg, toUserId, toGroupId) {
        // 1. 把内容添加到消息框
        $('.newsList').append(msg);

        // 2. 手动计算、调整回显消息的宽度
        var $newsDiv = $('.newsList li').last().children("div").first();
        var fixWidth = 300; // 自定义的消息框本身的最长宽度
        var maxWidth = 493; // 消息框所在行(div)的满宽度(不包含头像框的宽度部分)
        var minMarginLeftWidth = 224; // 按理说应该是 maxwidth - fixWidth，这里出现了点问题
        var marginLeftWidth; // 要计算消息框的margin-left宽度
        if ($newsDiv.actual('width') < fixWidth) {
            marginLeftWidth = maxWidth - $newsDiv.actual('width');
            $newsDiv.css("margin-left", marginLeftWidth + "px");
        } else {
            $newsDiv.css("width", fixWidth + "px")
                .css("margin-left", minMarginLeftWidth + "px");
        }

        // 3. 把 调整后的消息html标签字符串 添加到已发送用户消息表
        if (toUserId.length !== 0) {
            sentMessageMap.get(toUserId).push($('.newsList li').last().prop("outerHTML"));
        } else {
            sentMessageMap.get(toGroupId).push($('.newsList li').last().prop("outerHTML"));
        }

        // 4. 滚动条往底部移
        $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight);
    },

    receiveSingleMsg: function(msg, fromUserId) {
        // 1. 设置消息框可见
        $('.conRight').css("display", "-webkit-box");

        // 2. 把新消息放到暂存区$('.newsList-temp)，如果用户正处于与发出新消息的用户的消息框，则消息要回显
        $('.newsList-temp').append(msg);
        var $focusUserId = $(".conLeft .bg").find('span.hidden-userId');
        if ($focusUserId.length > 0 && $focusUserId.html()  == fromUserId) {
            $('.newsList').append(msg);
        }

        // 3. 利用暂存区手动计算、调整新消息的宽度；
        var $answersDiv = $('.newsList-temp li').last().children("div").first();
        var fixWidth = 300; // 消息框本身的最长宽度
        var maxWidth = 480; // 消息框所在行(div)的满宽度(不包含头像框的宽度部分)
        var minMarginRightWidth = 212; // 按理说应该是 maxwidth - fixWidth，这里出现了点问题
        var marginRightWidth; // 要计算消息框的margin-right宽度
        if ($answersDiv.actual('width') < fixWidth) {
            marginRightWidth = maxWidth - $answersDiv.actual('width');
            $answersDiv.css("margin-right", marginRightWidth + "px");
            if ($focusUserId.length > 0 && $focusUserId.html()  == fromUserId) {
                $('.newsList li').last().children("div").first()
                    .css("margin-right", marginRightWidth + "px");
            }
        } else {
            $answersDiv.css("width", fixWidth + "px")
                .css("margin-right", minMarginRightWidth + "px");
            if ($focusUserId.length > 0 && $focusUserId.html()  == fromUserId) {
                $('.newsList li').last().children("div").first()
                    .css("width", fixWidth + "px")
                    .css("margin-right", minMarginRightWidth + "px");
            }
        }

        // 4. 把 调整后的消息html标签字符串 添加到已发送用户消息表，并清空暂存区
        sentMessageMap.get(fromUserId).push($('.newsList-temp li').last().prop("outerHTML"));
        $('.newsList-temp').empty();

        // 5. 滚动条滑到底
        $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight );

        processFriendList.receiving(content, $receiveLi);

    },

    receiveGroupMsg: function(msg, toGroupId) {
        // 1. 设置消息框可见
        $('.conRight').css("display", "-webkit-box");

        // 2. 把新消息放到暂存区$('.newsList-temp)，如果用户正处于与发出新消息的用户的消息框，则消息要回显
        $('.newsList-temp').append(msg);
        var $focusGroupId = $(".conLeft .bg").find('span.hidden-groupId');
        if ($focusGroupId.length > 0 && $focusGroupId.html() == toGroupId) {
            $('.newsList').append(msg);
        }

        // 3. 手动计算、调整回显消息的宽度
        var $answersDiv = $('.newsList-temp li').last().children("div").first();
        var fixWidth = 300; // 消息框本身的最长宽度
        var maxWidth = 480; // 消息框所在行(div)的满宽度(不包含头像框的宽度部分)
        var minMarginRightWidth = 212; // 按理说应该是 maxwidth - fixWidth，这里出现了点问题
        var marginRightWidth; // 要计算消息框的margin-right宽度
        if ($answersDiv.actual('width') < fixWidth) {
            marginRightWidth = maxWidth - $answersDiv.actual('width');
            $answersDiv.css("margin-right", marginRightWidth + "px");
            if ($focusGroupId.length > 0 && $focusGroupId.html() == toGroupId) {
                $('.newsList li').last().children("div").first()
                    .css("margin-right", marginRightWidth + "px");
            }
        } else {
            $answersDiv.css("width", fixWidth + "px")
                .css("margin-right", minMarginRightWidth + "px");
            if ($focusGroupId.length > 0 && $focusGroupId.html() == toGroupId) {
                $('.newsList li').last().children("div").first()
                    .css("width", fixWidth + "px")
                    .css("margin-right", minMarginRightWidth + "px");
            }
        }

        // 4. 把 调整后的消息html标签字符串 添加到已发送用户消息表，并清空暂存区
        sentMessageMap.get(toGroupId).push($('.newsList-temp li').last().prop("outerHTML"));
        $('.newsList-temp').empty();

        // 5. 滚动条滑到底
        $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight);

        processFriendList.receiving(content, $receiveLi);

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

    $('.newsList').html('');
    var messageArray;
    if (toUserId.length !== 0) {
        messageArray = sentMessageMap.get(toUserId);
        $('#toUserId').val(toUserId);
        $('#shareGroup').hide();
    } else {
        messageArray = sentMessageMap.get(toGroupId);
        $('#toGroupId').val(toGroupId);
        $('#shareGroup').show();
        $('#shareGroupLink').attr('href','http://127.0.0.1:8080/oqa/joingroup/'+toGroupId);
    }
    for (var i = 0; i < messageArray.length; i++) {
        $('.newsList').append(messageArray[i]);
    }
    $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight );

    var $badge = $(this).find(".layui-badge");
    if ($badge.length > 0) {
        $badge.remove();
    }
}


var processFriendList = {
    sending: function(content, $sendLi) {
        // 1. 设置部分新消息提醒
        if (content.length > 8) {
            content = content.substring(0, 8) + "...";
        }
        $('.conLeft').find('li.bg').children('.liRight').children('.infor').text(content);
        // 2. 如果存在新消息提醒徽章，则去除徽章
        if ($sendLi.find('.layui-badge').length > 0) {
            $sendLi.find('.layui-badge').remove();
        }
        //$('.conLeft ul').prepend('<li class="bg">' + $sendLi.html() + '</li>');
        // 3. 好友框新消息置顶
        $('.conLeft ul').prepend($sendLi.prop("outerHTML"));
        $sendLi.remove();
        $('.conLeft ul li').first().on('click', friendLiClickEvent)
    },

    receiving: function(content, $receiveLi) {
        // 1. 设置红色提醒徽章
        var $badge = $receiveLi.find(".layui-badge");
        if ($badge.length > 0) {
            $badge.html(parseInt($badge.html()) + 1);
        } else {
            var badgeHTML = '<span class="layui-badge badge-avatar">1</span>';
            $receiveLi.children(".liLeft").prepend(badgeHTML);
        }
        // 2. 设置部分新消息提醒
        if (content.length > 8) { // 只显示前八个字符
            content = content.substring(0, 8) + "...";
        }
        if (content.search("<img") != -1) { // 若是图片，显示 “[图片]”
            content = "[图片]";
        }
        $receiveLi.children(".liRight").children('.infor').text(content);

        // 3. 新消息置顶
        $('.conLeft ul').prepend($receiveLi.prop("outerHTML"));
        $('.conLeft ul li').first().on('click',friendLiClickEvent);
        $receiveLi.remove();
    }
}

// 已发送用户消息表
function SentMessageMap() {
    this.elements = [];

    //获取MAP元素个数
    this.size = function () {
        return this.elements.length;
    };

    //判断MAP是否为空
    this.isEmpty = function () {
        return (this.elements.length < 1);
    };

    //删除MAP所有元素
    this.clear = function () {
        this.elements = [];
    };

    //向MAP中增加元素（key, value)
    this.put = function (_key, _value) {
        this.elements.push({
            key: _key,
            value: _value
        });
    };

    //删除指定KEY的元素，成功返回True，失败返回False
    this.removeByKey = function (_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //删除指定VALUE的元素，成功返回True，失败返回False
    this.removeByValue = function (_value) {//removeByValueAndKey
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //删除指定VALUE的元素，成功返回True，失败返回False
    this.removeByValueAndKey = function (_key, _value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value && this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //获取指定KEY的元素值VALUE，失败返回NULL
    this.get = function (_key) {
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    return this.elements[i].value;
                }
            }
        } catch (e) {
            return false;
        }
        return false;
    };

    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
    this.element = function (_index) {
        if (_index < 0 || _index >= this.elements.length) {
            return null;
        }
        return this.elements[_index];
    };

    //判断MAP中是否含有指定KEY的元素
    this.containsKey = function (_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //判断MAP中是否含有指定VALUE的元素
    this.containsValue = function (_value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //判断MAP中是否含有指定VALUE的元素
    this.containsObj = function (_key, _value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value && this.elements[i].key == _key) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //获取MAP中所有VALUE的数组（ARRAY）
    this.values = function () {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].value);
        }
        return arr;
    };

    //获取MAP中所有VALUE的数组（ARRAY）
    this.valuesByKey = function (_key) {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            if (this.elements[i].key == _key) {
                arr.push(this.elements[i].value);
            }
        }
        return arr;
    };

    //获取MAP中所有KEY的数组（ARRAY）
    this.keys = function () {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].key);
        }
        return arr;
    };

    //获取key通过value
    this.keysByValue = function (_value) {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            if (_value == this.elements[i].value) {
                arr.push(this.elements[i].key);
            }
        }
        return arr;
    };

    //获取MAP中所有KEY的数组（ARRAY）
    this.keysRemoveDuplicate = function () {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            var flag = true;
            for (var j = 0; j < arr.length; j++) {
                if (arr[j] == this.elements[i].key) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                arr.push(this.elements[i].key);
            }
        }
        return arr;
    };
}