package service.impl;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import model.vo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import service.OqaService;
import utils.ChatType;
import utils.Constant;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

@Service
public class OqaServiceImpl implements OqaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OqaServiceImpl.class);
    public static final OqaService INSTANCE = new OqaServiceImpl();

    @Override
    public void register(JSONObject param, ChannelHandlerContext ctx) {
        Integer userId = (Integer)param.get("userId");
        Constant.onlineUserMap.put(userId, ctx);
        R responseJson = new R().success()
                .setData("type", ChatType.REGISTER);
        sendMessage(ctx, responseJson);
        LOGGER.info(MessageFormat.format("userId为 {0} 的用户登记到在线用户表，当前在线人数为：{1}"
                , userId, Constant.onlineUserMap.size()));

        Constant.onlineUserMap.get(userId).channel().writeAndFlush(new R().success());
    }

    private void sendMessage(ChannelHandlerContext ctx, R message) {
        ctx.channel().writeAndFlush(message);
    }

    @Override
    public void remove(ChannelHandlerContext ctx) {
        Iterator<Map.Entry<Integer, ChannelHandlerContext>> iterator =
                Constant.onlineUserMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Integer, ChannelHandlerContext> entry = iterator.next();
            if (entry.getValue() == ctx) {
                LOGGER.info("正在移除握手实例...");
                Constant.webSocketHandshakerMap.remove(ctx.channel().id().asLongText());
                LOGGER.info(MessageFormat.format("已移除握手实例，当前握手实例总数为：{0}"
                        , Constant.webSocketHandshakerMap.size()));
                iterator.remove();
                LOGGER.info(MessageFormat.format("userId为 {0} 的用户已退出聊天，当前在线人数为：{1}"
                        , entry.getKey(), Constant.onlineUserMap.size()));
                break;
            }
        }
    }

    @Override
    public void singleSend(JSONObject param, ChannelHandlerContext ctx) {
        Integer fromUserId = (Integer)param.get("fromUserId");
        Integer toUserId = (Integer)param.get("toUserId");
        String content = (String)param.get("content");

        ChannelHandlerContext toUserCtx = Constant.onlineUserMap.get(toUserId);


        if (toUserCtx == null) {
            // TODO: 缓存消息
            R responseJson = new R()
                    .error(MessageFormat.format("userId为 {0} 的用户没有登录！", toUserId));

            sendMessage(ctx, responseJson);
        } else {
            R responseJson = new R().success()
                    .setData("fromUserId", fromUserId)
                    .setData("content", content)
                    .setData("type", ChatType.SINGLESEND);
            sendMessage(toUserCtx, responseJson);
        }
    }
}