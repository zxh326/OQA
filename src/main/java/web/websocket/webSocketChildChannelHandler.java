package web.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.stereotype.Component;

@Component
public class  webSocketChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec());   // 使用自带的解码http解码器。
        ch.pipeline().addLast(new HttpObjectAggregator(65536)); // 把HTTP头、HTTP体拼成完整的HTTP请求
        ch.pipeline().addLast(new HttpRequestHandler());
        ch.pipeline().addLast(new WebSocketCloseHandler());
        ch.pipeline().addLast(new WebSocketServerHandler());
    }
}
