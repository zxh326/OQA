package web.websocket;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.vo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.OqaService;
import service.impl.OqaServiceImpl;
import utils.ChannelHandlerPool;

import java.lang.reflect.Method;

@Component
@Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<JSONObject> {
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @Autowired
    private OqaService oqaService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connect...");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject param) throws Exception {

        String type = (String) param.get("type");
//        Class fs = OqaService.class;
//        Method s =  fs.getMethod(type.toLowerCase(), JSONObject.class, ChannelHandlerContext.class);
//        s.invoke(oqaService, param, ctx);
        switch (type) {
            case "REGISTER":
                oqaService.register(param, ctx);
                break;
            case "SINGLESEND":
                oqaService.singleSend(param, ctx);
                break;
            default:
                sendErrorMessage(ctx, "no");
                break;
//        }
        }
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        R responseJson = new R()
                .error(errorMsg);
        ctx.channel().writeAndFlush(responseJson);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        oqaService.remove(ctx);
    }
}
