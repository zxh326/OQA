package service.impl;

import common.Secret;
import common.TokenManager;
import dao.UserDao;
import model.po.User;
import model.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;

import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public R login(String username, String password, HttpSession session) {
        User user = userDao.getUserByName(username);

        if (user == null){
            return new R().error("no username");

        }
        if (!Secret.checkPassword(password, user.getUserPass())){
            return new R().error("no password");
        }

        session.setAttribute("token", TokenManager.generateToken(user.getUserId()));
        return new R().success();
    }
}
