package service.impl;

import model.po.Group;
import service.OqaService;
import utils.Secret;
import utils.TokenManager;
import dao.UserDao;
import model.po.User;
import model.po.UserProfile;
import model.vo.R;
import model.vo.RegUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final UserService INSTANCE = new UserServiceImpl();


    @Autowired
    private UserDao userDao;

    @Override
    public R login(User tuser, HttpSession session) {
        User user = userDao.getUserByLoginIdWithRole(tuser);

        if (user == null){
            return new R().error("no loginId");

        }
        if (!Secret.checkPassword(tuser.getUserPass(), user.getUserPass())){
            return new R().error("no password");
        }

        session.setAttribute("token", TokenManager.generateToken(user.getUserId()).getToken());
        return new R().success();
    }

    @Override
    public R reg(RegUser regUser) {
        if (userDao.getUserByLoginId(regUser.getLoginId())!=null){
            return new R().error("已注册");
        }
        if (regUser.getPassword().length() <= 6){
            return new R().error("密码太短");
        }
        // TODO: check dep

        User u = new User(regUser.getLoginId(), regUser.getUserRole(), Secret.enPassword(regUser.getPassword()));
        userDao.regUser(u);
        System.out.println(u.toString());
        if (u.getUserId()==null){
            return new R().error("error");
        }

        UserProfile up = new UserProfile(u.getUserId(), regUser.getUsername(), regUser.getDepartment());
        userDao.addUserProfile(up);

        return new R().success();
    }

    @Override
    public R getUserInfobyId(Integer userId) {
        User user = userDao.getUserById(userId);
        UserProfile up = userDao.getUserProfileById(userId);
        return new R().success().setData("user", user)
                                .setData("userprofile", up);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public List<Group> getUserGroup(User user) {
        return userDao.getUserGroup(user);
    }

    @Override
    public List<Group> getUserGroupById(Integer userId) {
        return userDao.getUserGroupById(userId);
    }
}
