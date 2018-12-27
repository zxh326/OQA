package model.po;

public class GroupMesage {
    private Integer id;
    private Integer fromId;
    private Integer groupId;
    private String fromavatarUrl;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromavatarUrl() {
        return fromavatarUrl;
    }

    public void setFromavatarUrl(String fromavatarUrl) {
        this.fromavatarUrl = fromavatarUrl;
    }

    @Override
    public String toString() {
        return "GroupMesage{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", groupId=" + groupId +
                ", fromavatarUrl='" + fromavatarUrl + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
