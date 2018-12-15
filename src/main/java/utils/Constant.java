package utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import model.vo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constant {
    public static Map<String, WebSocketServerHandshaker> webSocketHandshakerMap =
            new ConcurrentHashMap<String, WebSocketServerHandshaker>();

    public static Map<Integer, ChannelHandlerContext> onlineUserMap =
            new HashMap<>();

    public static List<Integer> onlineTeacher =
            new ArrayList<>();

    public static void sendMessage(ChannelHandlerContext ctx, R message) {
        ctx.channel().writeAndFlush(message);
    }
}
