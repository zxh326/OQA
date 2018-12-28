package service.event;

import dao.MessageDao;
import io.netty.channel.ChannelHandlerContext;
import model.po.User;
import model.vo.GroupMessage;
import model.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import utils.ChatType;
import utils.Constant;

@Service
public class GroupSendListener implements ApplicationListener<GroupSendEvent> {
    @Autowired
    private MessageDao messageDao;

    @Override
    public void onApplicationEvent(GroupSendEvent groupSendEvent) {

        // 获取事件传过来的对象
        GroupMessage groupMessage = (GroupMessage) groupSendEvent.getSource();

        // 封装消息
        R responseJson = new R().success()
                .setData("fromUserId", groupMessage.getFromUser().getUserId())
                .setData("toGroupId", groupMessage.getGroup().getGroupId())
                .setData("content", groupMessage.getContent())
                .setData("avatarUrl",groupMessage.getFromUser().getAvatarUrl())
                .setData("type", ChatType.GROUPSEND);
        // 保存消息记录
        messageDao.insertGroupMessages( groupMessage.getFromUser().getUserId(),groupMessage.getGroup().getGroupId(),groupMessage.getContent());

        // 遍历群成员，可能会存在不在线无法推送的问题，需要用户上线后动拉取
        for (User u:groupMessage.getGroup().getGroupUsers()
             ) {
            ChannelHandlerContext uctx = Constant.onLineAllUserMap.get(u.getUserId());
            // 发消息本人本人不推送
            if(uctx!=null && groupMessage.getCtx()!=uctx){
                Constant.sendMessage(uctx, responseJson);
            }
        }
    }
}
