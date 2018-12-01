package web.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import model.vo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constant;

public class WebSocketCloseHandler extends SimpleChannelInboundHandler<CloseWebSocketFrame> {
    private final Logger logger = LoggerFactory.getLogger(WebSocketCloseHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloseWebSocketFrame msg) throws Exception {
        // TODO 将用户移除连接池
        logger.info(ctx.channel().id() + ":close");

        WebSocketServerHandshaker handshaker =
                Constant.webSocketHandshakerMap.get(ctx.channel().id().asLongText());
        if (handshaker == null){
            ctx.writeAndFlush("no connextion");
        }else{
            handshaker.close(ctx.channel(),  msg.retain());
        }
    }
}
