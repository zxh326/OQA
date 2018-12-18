package service.impl;

import com.alibaba.fastjson.JSONObject;
import dao.GroupDao;
import dao.UserDao;
import io.netty.channel.ChannelHandlerContext;
import model.po.Group;
import model.po.User;
import model.vo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import service.OqaService;
import service.event.TeacherDownEvent;
import service.event.TeacherOnLineEvent;
import service.event.UserRegisterEvent;
import utils.ChatType;
import utils.Constant;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

import static utils.Constant.sendMessage;

@Service
public class OqaServiceImpl implements OqaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OqaServiceImpl.class);
//    public static final OqaService INSTANCE = new OqaServiceImpl();

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void register(JSONObject param, ChannelHandlerContext ctx) {
        Integer userId = (Integer)param.get("userId");
        R responseJson = new R().success()
                .setData("type", ChatType.REGISTER);
        sendMessage(ctx, responseJson);
        LOGGER.info(MessageFormat.format("userId为 {0} 的用户登记到在线用户表，当前在线人数为：{1}"
                , userId, Constant.onLineAllUserMap.size()));

        User isTeacher = userDao.getUserById(userId);

        if (isTeacher.getUserRole() == 1) {
            Constant.onlineTeacher.put(userId, ctx);
            applicationEventPublisher.publishEvent(new TeacherOnLineEvent(isTeacher));
        }else{
            Constant.onlineUserMap.put(userId, ctx);
        }
        Constant.onLineAllUserMap.put(userId, ctx);
        applicationEventPublisher.publishEvent(new UserRegisterEvent(isTeacher));
    }


    @Override
    public void singleSend(JSONObject param, ChannelHandlerContext ctx) {
        Integer fromUserId = (Integer)param.get("fromUserId");
        Integer toUserId = (Integer)param.get("toUserId");
        String content = (String)param.get("content");

        ChannelHandlerContext toUserCtx = Constant.onlineUserMap.get(toUserId);
        if (toUserCtx==null){
            toUserCtx = Constant.onlineTeacher.get(toUserId);
        }
        if (toUserCtx == null) {
            // TODO: 缓存消息
            R responseJson = new R()
                    .error(MessageFormat.format("userId为 {0} 的用户没有登录！", toUserId));

            sendMessage(ctx, responseJson);
        } else {
            R responseJson = new R().success()
                    .setData("fromUserId", fromUserId)
                    .setData("content", content)
                    .setData("avatarUrl", userDao.getUserById(fromUserId).getAvatarUrl())
                    .setData("type", ChatType.SINGLESEND);
             sendMessage(toUserCtx, responseJson);
        }
    }

    @Override
    public void groupSend(JSONObject param, ChannelHandlerContext ctx) {
        Integer fromUserId = (Integer)param.get("fromUserId");
        Integer toGroupId = (Integer)param.get("toGroupId");
        String content = (String)param.get("content");

        Group group = groupDao.getGroup(toGroupId);

        if (group == null) {
            R r = new R().error("该群id不存在");
            sendMessage(ctx, r);
            return;
        }
        R responseJson = new R().success()
                .setData("fromUserId", fromUserId)
                .setData("toGroupId", group.getGroupId())
                .setData("content", content)
                .setData("avatarUrl", userDao.getUserById(fromUserId).getAvatarUrl())
                .setData("type", ChatType.GROUPSEND);
        for (User u:group.getGroupUsers()
             ) {
            ChannelHandlerContext uctx = Constant.onLineAllUserMap.get(u.getUserId());
            if(uctx!=null && ctx!=uctx){
                sendMessage(uctx, responseJson);
            }
        }
    }


    @Override
    public void remove(ChannelHandlerContext ctx) {
        Iterator<Map.Entry<Integer, ChannelHandlerContext>> iterator =
                Constant.onlineUserMap.entrySet().iterator();
        Iterator<Map.Entry<Integer, ChannelHandlerContext>> iterator2 =
                Constant.onlineTeacher.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<Integer, ChannelHandlerContext> entry = iterator.next();
            if (entry.getValue() == ctx) {
                Constant.onLineAllUserMap.remove(entry.getKey());
                LOGGER.info("正在移除握手实例...");
                Constant.webSocketHandshakerMap.remove(ctx.channel().id().asLongText());
                iterator.remove();
                LOGGER.info(MessageFormat.format("userId为 {0} 的用户已退出系统，当前在线人数为：{1}"
                        , entry.getKey(), Constant.onLineAllUserMap.size()));
                break;
            }
        }
        while(iterator2.hasNext()) {
            Map.Entry<Integer, ChannelHandlerContext> entry = iterator2.next();
            if (entry.getValue() == ctx) {
                LOGGER.info("正在移除握手实例...");
                Constant.onLineAllUserMap.remove(entry.getKey());
                Constant.webSocketHandshakerMap.remove(ctx.channel().id().asLongText());
                iterator2.remove();
                User ifTeacher = userDao.getUserById(entry.getKey());
                if (ifTeacher.getUserRole() == 1){
                    applicationEventPublisher.publishEvent(new TeacherDownEvent(ifTeacher));
                }
                LOGGER.info(MessageFormat.format("userId为 {0} 的teacher已退出系统，当前在线人数为：{1}"
                        , entry.getKey(), Constant.onLineAllUserMap.size()));
                break;
            }
        }
    }
}
