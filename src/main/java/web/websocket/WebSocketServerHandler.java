package web.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 对websocket的解析器
 */
@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("active");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handlerWebSocketFrame(ctx, msg);
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame wf) throws Exception{

        String request = ((TextWebSocketFrame)wf).text();

        System.out.println(request);
    }


}
