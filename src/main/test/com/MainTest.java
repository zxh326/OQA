package com;

import dao.GroupDao;
import dao.MessageDao;
import dao.UserDao;
import model.po.Group;
import model.po.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.ChatType;
import utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainTest extends SpringTestCase{

    @Autowired
    GroupDao groupDao;

    @Autowired
    MessageDao messageDao;

    @Autowired
    UserDao userDao;

    @Test
    public void getGroups(){
        System.out.println(groupDao.getGroup(1).toString());
    }

    @Test
    public void createGroup(){
        Group group = new Group();
        group.setGroupName("test");

        groupDao.createGroup(group);

        groupDao.addGroupUsers(group.getGroupId(), 4);

        System.out.println(groupDao.getGroup(group.getGroupId()));
    }

    @Test
    public void getUserGroup(){
        User u = new User();
        u.setUserId(4);

        System.out.println(userDao.getUserGroup(u));
    }

    @Test
    public void getUsers(){

        List<Integer> ids = new ArrayList<>();

        ids.add(1);
        ids.add(2);

        System.out.println(userDao.getUserByIds(ids));

    }

    @Test
    public void con(){
        System.out.println(System.currentTimeMillis()/1000);
        Constant.lastOnlineTeacher.put(1, (int) System.currentTimeMillis());

        System.out.println(ChatType.REGISTER.name());
    }

    @Test
    public void getMessages(){
        System.out.println(messageDao.getGroupMessages(1));
    }

}
