package service;

import model.po.User;
import model.vo.ResponseJson;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface UserService {

    ResponseJson login(String username, String password, HttpSession session);
}
