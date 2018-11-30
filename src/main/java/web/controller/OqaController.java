package web.controller;

import utils.TokenManager;
import model.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/oqa")
public class OqaController {

    private final UserService userService;

    @Autowired
    public OqaController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String Oqa(){
        return "oqa";
    }

    @ResponseBody
    @RequestMapping(value = "/get_userinfo", method = RequestMethod.GET)
    public R index(HttpSession session){
        Integer userId = TokenManager.getUserId((String) session.getAttribute("token"));

        System.out.println(userId);
        return userService.getUserInfobyId(userId);
    }
}
