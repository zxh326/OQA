package web.websocket;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import model.vo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.OqaService;
import service.impl.OqaServiceImpl;
import utils.ChannelHandlerPool;


/**
 * 对websocket的解析器
 */
@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

//    @Autowired/
    private OqaService oqaService = OqaServiceImpl.INSTANCE;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ChannelHandlerPool.channelGroup.add(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handlerWebSocketFrame(ctx, msg);
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame wf) throws Exception{
        String request = ((TextWebSocketFrame)wf).text();

        JSONObject param = null;
        try {
            param = JSONObject.parseObject(request);
        } catch (Exception e) {
            sendErrorMessage(ctx, "JSON字符串转换出错！");
            e.printStackTrace();
        }
        if (param == null) {
            sendErrorMessage(ctx, "参数为空！");
            return;
        }

        String type = (String) param.get("type");

        switch (type){
            case "REGISTER":
                oqaService.register(param, ctx);
                break;
            default:
                sendErrorMessage(ctx, "no");
        }

        System.out.println(param);
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        String responseJson = new R()
                .error(errorMsg)
                .toString();
        ctx.channel().writeAndFlush(responseJson);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        oqaService.remove(ctx);
    }


}
