package dao;

import model.po.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

    @Select("select * from user where id=#{userId}")
    User getUserById(String userId);

    @Select("select * from user where userName=#{username}")
    User getUserByName(String username);
}
