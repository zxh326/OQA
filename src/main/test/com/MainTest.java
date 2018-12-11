package com;

import dao.GroupDao;
import dao.UserDao;
import model.po.Group;
import model.po.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MainTest extends SpringTestCase{

    @Autowired
    GroupDao groupDao;

    @Autowired
    UserDao userDao;

    @Test
    public void getGroups(){
        Group group = new Group();
        group.setGroupId(1);
        System.out.println(groupDao.getGroup(group).toString());
    }

    @Test
    public void createGroup(){
        Group group = new Group();
        group.setGroupName("test");

        groupDao.createGroup(group);

        System.out.println(group);
    }

    @Test
    public void getUserGroup(){
        User u = new User();
        u.setUserId(4);

        System.out.println(userDao.getUserGroup(u));
    }

}
