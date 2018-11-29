package service;

import model.vo.R;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface UserService {

    R login(String username, String password, HttpSession session);
}
