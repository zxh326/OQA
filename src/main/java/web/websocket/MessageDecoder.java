package web.websocket;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import model.vo.R;

import java.util.List;

public class MessageDecoder extends MessageToMessageDecoder<WebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {
        String request = ((TextWebSocketFrame)webSocketFrame).text();

        JSONObject param = null;
        try {
            param = JSONObject.parseObject(request);
        } catch (Exception e) {
            sendErrorMessage(ctx, "JSON字符串转换出错！");
            return;
//            e.printStackTrace();
        }
        if (param == null) {
            sendErrorMessage(ctx, "参数为空！");
        }

        list.add(param);
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        R responseJson = new R()
                .error(errorMsg);
        ctx.channel().writeAndFlush(responseJson);
    }
}
