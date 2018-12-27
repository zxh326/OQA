package model.po;

import java.util.Date;
import java.util.List;

public class Group {
    private Integer groupId;

    private String groupName;

    private Date createTime;

    private List<User> groupUsers;

    private List<GroupMesage> groupMessages;


    public Group() {
        this.createTime = new Date();
    }

    public List<User> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<User> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<GroupMesage> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(List<GroupMesage> groupMessages) {
        this.groupMessages = groupMessages;
    }


    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", createTime=" + createTime +
                ", groupUsers=" + groupUsers +
                ", groupMessages=" + groupMessages +
                '}';
    }
}
