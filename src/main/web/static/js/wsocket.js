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
        // processFriendList.sending(news, $sendLi);
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

    registerReceive: function() {
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
    },

    singleSend: function(fromUserId, toUserId, content){
        console.log(fromUserId, toUserId, content)
    },

    teacherOnline: function (data) {
        var teacher = data.teacher;
        $("#tnone").remove();
        setSentMessageMap(teacher.userId)
        $("#teachers").append(
            '<li id="t'+ teacher.userId + '">' +
            '<div class="liLeft"><img src="static/img/emoji/emoji_03.png"></div>' +
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
            ;
            $newsDiv.css("margin-left", marginLeftWidth + "px");
        } else {
            $newsDiv.css("width", fixWidth + "px")
                .css("margin-left", minMarginLeftWidth + "px");
        }

        // 3. 把 调整后的消息html标签字符串 添加到已发送用户消息表
        if (toUserId.length != 0) {
            sentMessageMap.get(toUserId).push($('.newsList li').last().prop("outerHTML"));
        } else {
            sentMessageMap.get(toGroupId).push($('.newsList li').last().prop("outerHTML"));
        }

        // 4. 滚动条往底部移
        $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight);
    }

}



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
    if (toUserId.length != 0) {
        messageArray = sentMessageMap.get(toUserId);
        $('#toUserId').val(toUserId);
    } else {
        messageArray = sentMessageMap.get(toGroupId);
        $('#toGroupId').val(toGroupId);
    }
    for (var i = 0; i < messageArray.length; i++) {
        $('.newsList').append(messageArray[i]);
    }

    // 5.设置消息框滚动条滑到底部
    $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight );
}

// 自定义数据结构：已发送用户消息表
function SentMessageMap() {
    this.elements = new Array();

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
        this.elements = new Array();
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