package dao;

import model.po.Group;
import model.po.User;
import model.po.UserProfile;
import model.vo.RegUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserDao {

    @Select("select * from user where userId=#{userId}")
    User getUserById(Integer userId);

    @Select("select * from user where loginId=#{loginId}")
    User getUserByLoginId(String loginId);

    @Select("select * from user where userName=#{username}")
    User getUserByName(String username);

    @Select("select * from user where loginId=#{loginId} and userRole=#{userRole}")
    User getUserByLoginIdWithRole(User user);

    @Select("select * from userprofile where userId=#{userId}")
    UserProfile getUserProfileById(Integer userId);

    @Insert("insert into user (loginId, userPass, userRole) values (#{loginId}, #{userPass},#{userRole})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void regUser(User user);

    @Insert("insert into userprofile (userId, userName, userDepartment) values (#{userId}, #{userName}, #{userDepartment})")
    void addUserProfile(UserProfile userProfile);

    @Select("select * from ogroup join group_users on ogroup.groupId=group_users.groupId where group_users.userId=#{userId}")
    List<Group> getUserGroup(User user);

    @Select("select * from ogroup join group_users on ogroup.groupId=group_users.groupId where group_users.userId=#{userId}")
    List<Group> getUserGroupById(Integer userId);
}
