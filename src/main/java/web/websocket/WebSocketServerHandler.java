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
import utils.ChatType;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<JSONObject> {
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @Autowired
    private OqaService oqaService;

    private Map<String, String> handlerMap;

    public WebSocketServerHandler() {
        handlerMap = new HashMap<>();
        handlerMap.put(ChatType.REGISTER.name(), "register");
        handlerMap.put(ChatType.SINGLESEND.name(), "singleSend");
        handlerMap.put(ChatType.GROUPSEND.name(), "groupSend");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject param) throws Exception {

        String type = (String) param.get("type");
        Class fs = OqaService.class;
        String method = handlerMap.get(type);
        if (method==null){
            sendErrorMessage(ctx, "method not found");

        }
        Method s =  OqaService.class.getMethod(handlerMap.get(type), JSONObject.class, ChannelHandlerContext.class);
        s.invoke(oqaService, param, ctx);
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        R responseJson = new R()
                .error(errorMsg).setData("type","error");
        ctx.channel().writeAndFlush(responseJson);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        oqaService.remove(ctx);
    }
}
