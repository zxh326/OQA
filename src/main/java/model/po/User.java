package model.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User  {
    private Integer userId;
    private String loginId;
    private Integer userRole;
    private String userPass;
    
    private String avatarUrl;


    public User(String loginId, Integer userRole, String userPass) {
        this.loginId = loginId;
        this.userRole = userRole;
        this.userPass = userPass;
    }

    public User() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
        if (this.userRole == 1){
            setAvatarUrl("/static/img/teacher.jpg");
        }else{
            setAvatarUrl("/static/img/stu.jpg");
        }
    }

    @JsonIgnore
    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", loginId='" + loginId + '\'' +
                ", userRole=" + userRole +
                ", userPass='" + userPass + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
