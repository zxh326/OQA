package service;

import model.po.User;
import model.vo.R;
import model.vo.RegUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


public interface UserService {

    R login(User user, HttpSession session);

    R reg(RegUser regUser);

    R getUserInfobyId(Integer userId);
}
