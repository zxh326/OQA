jQuery(document).ready(function($){
    var offset = 300,
        offset_opacity = 1200,
        scroll_top_duration = 700,
        back_to_top = $('.cd-top');

    $(window).scroll(function(){
        ( $(this).scrollTop() > offset ) ? back_to_top.addClass('cd-is-visible') : back_to_top.removeClass('cd-is-visible cd-fade-out');
        if( $(this).scrollTop() > offset_opacity ) { 
            back_to_top.addClass('cd-fade-out');
        }
    });


// Top 动画
    back_to_top.on('click', function(event){
        // 阻止正常动作
        event.preventDefault();
        // 动画开始
        $('body,html').animate({
            scrollTop: 0 ,
            }, scroll_top_duration
        );
    });
});


function dianzan(event) {
    _this = $(event)
    var count = parseInt(_this.text())
    if (_this.hasClass("glyphicon glyphicon-heart-empty"))  {
        _this.attr('class', 'glyphicon glyphicon-heart')
        count +=1
    }
    else{
        _this.attr('class', 'glyphicon glyphicon-heart-empty')
        count -= 1
    }
    _this.html(" "+count)
}


// 
function SetItem(key, value){
    localStorage.setItem(key, value)
}

function getItem(key){
    // var result = null;
    // //对cookie信息进行相应的处理，方便搜索
    // var myCookie = ""+document.cookie+";";
    // var searchName = ""+name+"=";
    // console.log(document.cookie)
    // var startOfCookie = myCookie.indexOf(searchName);
    // var endOfCookie;
    // if(startOfCookie != -1){
    //     startOfCookie += searchName.length;
    //     endOfCookie = myCookie.indexOf(";",startOfCookie);
    //     result = (myCookie.substring(startOfCookie,endOfCookie));
    // }
    // return result;

    return localStorage.getItem(key)
} 

function openshare(event){
    childNodes = event.childNodes
    for (var i = childNodes.length - 1; i >= 0; i--) {
        console.log(childNodes[i].nodeName)
        if (childNodes[i].nodeName == 'IMG'){
            SetItem("share", childNodes[i].src)
        }
    }
    window.open("fa.html")
}


$(function(){
    $('.leftside a').hover(function(){
        $(this).animate({width:'70px'},300);
    },function(){
        $(this).animate({width:'50px'},300);    
    });
});


$(function(){
    var username = getItem('user')
    if (username !=null){
        $('#welcome').html("欢迎： "+ username);
    }
    else{
         $('#welcome').html("请登录");
    }
});
$(function(){
    $("#shareimg").attr("src", getItem("share"))
});
//  indexDB //

function openDB (name,version) {
    version = version || 1;
    var idbRequest = window.indexedDB.open(name, version);
    idbRequest.onerror = function(e){
        console.warn('error: %s', e.currentTarget.error.message);
    };
    
    idbRequest.onsuccess = function(e){
        db = e.target.result; //这里才是 indexedDB对象
        console.log('idbRequest === e.target: %o', idbRequest === e.target);
        console.log('db: %o, idbRequest: %o', db, idbRequest);
    };

    idbRequest.onupgradeneeded = function(e){
        console.log('DB version change to ' + version);
        var db = e.target.result;
        console.log('onupgradeneeded: db->', db);
    };
}

function deleteDB(name){
    window.indexedDB.deleteDatabase(name);
}

function saveData (dbName, version, storeName, data) {
    var idbRequest = indexedDB.open(dbName, version);

    idbRequest.onsuccess = function (e) {
        var db = idbRequest.result; 
        var transaction = db.transaction(storeName, 'readwrite');//需先创建事务
        var store = transaction.objectStore(storeName); 
        data.forEach(function (item) {
            store.add(item);//保存数据
        });
        console.log('save data done');
    }
}


// function init(){

//     body = $("#init")

//     for (var i = 0; i < all_images.length ; i++) {
//         $("<div class='thumbnail radius-0 border-0 margin-b0' onclick="openshare(this)"><img src="img/img1/xing.jpg" alt="星空"></div><div class='row margin-t0 iteminfo'><div class='col-xs-7 text-left'>    <span>大力水手</span></div><div class='col-xs-5 text-right'>    <span class='glyphicon glyphicon-heart-empty' onclick="dianzan(this)"> 151 </span>     <span class='hidden-sm hidden-xs'> &nbsp;         <span class='glyphicon glyphicon-comment'></span> 21    </span> </div>
//  </div>“)
//     }
// }