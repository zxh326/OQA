package web.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WebSocketChannelHandler extends ChannelInitializer<SocketChannel> {

    @Resource(name="httpRequestHandler")
    private ChannelHandler HttpRequestHandler;

    @Resource(name="webSocketServerHandler")
    private ChannelHandler WebSocketServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec());   // 使用自带的解码http解码器。
        ch.pipeline().addLast(new HttpObjectAggregator(65536)); // 把HTTP头、HTTP体拼成完整的HTTP请求
        ch.pipeline().addLast(HttpRequestHandler);
        ch.pipeline().addLast(new WebSocketCloseHandler());

        ch.pipeline().addLast(new MessageDecoder());
        ch.pipeline().addLast(WebSocketServerHandler);
        ch.pipeline().addLast(new MessageEncoder());
    }
}
