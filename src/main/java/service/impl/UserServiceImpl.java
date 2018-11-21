package service.impl;

import common.Secret;
import dao.UserDao;
import model.po.User;
import model.vo.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import service.UserService;

import javax.servlet.http.HttpSession;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public ResponseJson login(String username, String password, HttpSession session) {
        User user = userDao.getUserByName(username);

        if (user == null){
            return new ResponseJson().error("no username");

        }
        if (!Secret.checkPassword(password, user.getUserPass())){
            return new ResponseJson().error("no password");
        }

        session.setAttribute("token", user.getUserId());
        return new ResponseJson().success();
    }
}
