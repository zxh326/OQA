package service;

import model.po.Group;

public interface GroupService {

    void createGroup(Group group);

    void addGroupUsers(Integer groupId, Integer userId);

    Group getGroup(Integer groupId);

    Group getMessage(Integer groupID);

}
