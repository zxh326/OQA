package service.impl;

import com.alibaba.fastjson.JSONObject;
import dao.UserDao;
import io.netty.channel.ChannelHandlerContext;
import model.po.User;
import model.vo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import service.OqaService;
import service.event.TeacherOnLineEvent;
import service.event.UserRegisterEvent;
import utils.ChatType;
import utils.Constant;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

@Service
public class OqaServiceImpl implements OqaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OqaServiceImpl.class);
//    public static final OqaService INSTANCE = new OqaServiceImpl();

    @Autowired
    private UserDao userDao;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void register(JSONObject param, ChannelHandlerContext ctx) {
        Integer userId = (Integer)param.get("userId");
        Constant.onlineUserMap.put(userId, ctx);
        R responseJson = new R().success()
                .setData("type", ChatType.REGISTER);
        Constant.sendMessage(ctx, responseJson);
        LOGGER.info(MessageFormat.format("userId为 {0} 的用户登记到在线用户表，当前在线人数为：{1}"
                , userId, Constant.onlineUserMap.size()));

        User isTeacher = userDao.getUserById(userId);

        if (isTeacher.getUserRole() == 1) {
            Constant.onlineTeacher.add(userId);
            applicationEventPublisher.publishEvent(new TeacherOnLineEvent(isTeacher));
        }

        applicationEventPublisher.publishEvent(new UserRegisterEvent(isTeacher));
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

            Constant.sendMessage(ctx, responseJson);
        } else {
            R responseJson = new R().success()
                    .setData("fromUserId", fromUserId)
                    .setData("content", content)
                    .setData("type", ChatType.SINGLESEND);
             Constant.sendMessage(toUserCtx, responseJson);
        }
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
                iterator.remove();
                LOGGER.info(MessageFormat.format("userId为 {0} 的用户已退出聊天，当前在线人数为：{1}"
                        , entry.getKey(), Constant.onlineUserMap.size()));
                break;
            }
        }
    }
}
