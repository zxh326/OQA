package model.po;

public class UserProfile {
    private Integer userId;
    private String userName;
    private String userDepartment;

    @Override
    public String toString() {
        return "UserProfile{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userDepartment='" + userDepartment + '\'' +
                '}';
    }

    public UserProfile(Integer userId, String userName, String userDepartment) {
        this.userId = userId;
        this.userName = userName;
        this.userDepartment = userDepartment;
    }

    public UserProfile() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }
}
