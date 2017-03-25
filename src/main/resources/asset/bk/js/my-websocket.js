/**
 * Created by qmaolong on 2016/11/6.
 */
/**
 * Websocket
 * @param params
 */
var MyWebsocket = {
    //初始化连接
    initWebsocket : function(params){
        if(undefined == params || params == null){
            return;
        }
        var websocket = null;

        //判断当前浏览器是否支持WebSocket
        if(MyWebsocket.isSupportWebsocket()){
            websocket = new WebSocket("ws://" + params.url);
        }else{
            alert('浏览器不支持Websocket')
        }
        //连接发生错误的回调方法
        websocket.onerror = function(event){
            if(params.onError != undefined && typeof params.onError == 'function'){
                params.onError(event, websocket);
            }else{
                alert("连接错误！")
            }
        };

        //连接成功建立的回调方法
        websocket.onopen = function(event){
            if(params.onOpen != undefined && typeof params.onOpen == 'function'){
                params.onOpen(params.event, websocket);
            }else{
                alert("连接成功！")
            }
        }

        //接收到消息的回调方法
        websocket.onmessage = function(event){
            if(params.onMessage != undefined && typeof params.onMessage == 'function'){
                params.onMessage(event, websocket);
            }else{
                alert(event.data)
            }
        }

        //连接关闭的回调方法
        websocket.onclose = function(){
            if(params.onClose != undefined && typeof params.onClose == 'function'){
                params.onClose(websocket);
            }else{
                alert("连接关闭")
            }
        }

        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function(){
            websocket.close();
        }
    },
    //判断浏览器是否支持websocket
    isSupportWebsocket : function(){
        return 'WebSocket' in window;
    }
}

