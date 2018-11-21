package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Resource
    private UserService userService;


    @RequestMapping(value = {"login", "/"}, method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpSession session,
                        @RequestParam String username,
                        @RequestParam String password) {
        return userService.login(username, password, session);
    }
}
