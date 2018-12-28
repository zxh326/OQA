package model.vo;

import io.netty.channel.ChannelHandlerContext;
import model.po.Group;
import model.po.User;

public class GroupMessage {
    private Group group;
    private User fromUser;
    private String content;
    private ChannelHandlerContext ctx;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
