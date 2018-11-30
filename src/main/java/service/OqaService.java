package service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

public interface OqaService {
    public void register(JSONObject param, ChannelHandlerContext ctx);

}
