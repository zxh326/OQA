package service.impl;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import model.vo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.OqaService;
import utils.ChatType;
import utils.Constant;

import java.text.MessageFormat;

public class OqaServiceImpl implements OqaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OqaServiceImpl.class);

    @Override
    public void register(JSONObject param, ChannelHandlerContext ctx) {
        String userId = (String)param.get("userId");
        Constant.onlineUserMap.put(userId, ctx);
        String responseJson = new R().success()
                .setData("type", ChatType.REGISTER)
                .toString();
        sendMessage(ctx, responseJson);
        LOGGER.info(MessageFormat.format("userId为 {0} 的用户登记到在线用户表，当前在线人数为：{1}"
                , userId, Constant.onlineUserMap.size()));
    }

    private void sendMessage(ChannelHandlerContext ctx, String message) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }
}
