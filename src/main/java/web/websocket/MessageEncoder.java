package web.websocket;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import model.vo.R;

import java.util.List;

public class MessageEncoder extends MessageToMessageEncoder<R> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, R r, List<Object> list) throws Exception {
        list.add(new TextWebSocketFrame(r.toString()));
    }
}
