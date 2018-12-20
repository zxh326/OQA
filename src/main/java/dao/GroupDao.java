package dao;

import model.po.Group;
import model.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface GroupDao {

    @Insert("insert into ogroup (groupName, createTime) values (#{groupName}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "groupId")
    void createGroup(Group group);

    @Insert("insert into group_users values(#{arg0}, #{arg1})")
    void addGroupUsers(Integer groupId, Integer userId);

    Group getGroup(Integer groupId);
}
