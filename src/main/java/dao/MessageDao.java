package dao;

import model.po.Group;
import org.apache.ibatis.annotations.Insert;

public interface MessageDao {

    Group getGroupMessages(Integer groupId);

    @Insert("insert into gmessage(fromId,groupId,content) values(#{arg0}, #{arg1},#{arg2})")
    void insertGroupMessages(Integer fromId, Integer groupId, String content);
}
