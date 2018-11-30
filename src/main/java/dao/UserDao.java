package dao;

import model.po.User;
import model.po.UserProfile;
import model.vo.RegUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

    @Select("select * from User where userId=#{userId}")
    User getUserById(Integer userId);

    @Select("select * from User where loginId=#{loginId}")
    User getUserByLoginId(String loginId);

    @Select("select * from User where userName=#{username}")
    User getUserByName(String username);

    @Select("select * from User where loginId=#{loginId} and userRole=#{userRole}")
    User getUserByLoginIdWithRole(User user);

    @Select("select * from userprofile where userId=#{userId}")
    UserProfile getUserProfileById(Integer userId);

    @Insert("insert into User (loginId, userPass, userRole) values (#{loginId}, #{userPass},#{userRole})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void regUser(User user);

    @Insert("insert into userprofile (userId, userName, userDepartment) values (#{userId}, #{userName}, #{userDepartment})")
    void addUserProfile(UserProfile userProfile);
}
