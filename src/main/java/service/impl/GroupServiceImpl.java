package service.impl;

import dao.GroupDao;
import model.po.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    public GroupDao groupDao;

    @Override
    public void createGroup(Group group) {
        groupDao.createGroup(group);
    }
}
