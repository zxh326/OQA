package service;

import model.po.Group;
import model.po.User;
import model.vo.R;
import model.vo.RegUser;

import javax.servlet.http.HttpSession;
import java.util.List;


public interface UserService {

    R login(User user, HttpSession session);

    R reg(RegUser regUser);

    R getUserInfobyId(Integer userId);

    User getUserById(Integer userId);

    List<Group> getUserGroup(User user);

    List<Group> getUserGroupById(Integer userId);
}
