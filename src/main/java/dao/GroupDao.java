package dao;

import model.po.Group;
import model.po.User;
import org.apache.ibatis.annotations.Insert;

public interface GroupDao {

    @Insert("insert into ogroup (groupName, createTime) values (#{groupName}, #{createTime})")
    void createGroup(Group group);

    Group getGroup(Group group);
}
