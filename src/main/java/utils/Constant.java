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

    public static Map<Integer, ChannelHandlerContext> onlineTeacher =
            new HashMap<>();

    public static Map<Integer, Integer> lastOnlineTeacher = new HashMap<>();

    public static void sendMessage(ChannelHandlerContext ctx, R message) {
        ctx.channel().writeAndFlush(message);
    }
}
