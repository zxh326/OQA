package web.controller;

import model.po.Group;
import model.po.User;
import service.GroupService;
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
    private GroupService groupService;

    @Autowired
    public OqaController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String Oqa(HttpSession session){
        return "oqa";
    }

    @RequestMapping(value = "/joingroup/{groupId}", method = RequestMethod.GET)
    public String Oqa(HttpSession session, @PathVariable("groupId") Integer groupId){
        Integer userId = TokenManager.getUserId((String) session.getAttribute("token"));

        Group group = groupService.getGroup(groupId);
        if (group==null){
            return "404";
        }else{
            try{
                groupService.addGroupUsers(group.getGroupId(),userId);
            }catch (Exception ignored){

            }
        }

        return "redirect:/oqa";
    }

    @ResponseBody
    @RequestMapping(value = "/get_userinfo", method = RequestMethod.GET)
    public R index(HttpSession session){
        Integer userId = TokenManager.getUserId((String) session.getAttribute("token"));

        System.out.println(userId);
        return userService.getUserInfobyId(userId);
    }

    @ResponseBody
    @RequestMapping(value = "/create_group", method = RequestMethod.POST)
    public R CreateGroup(HttpSession session, Group group){
        Integer userId = TokenManager.getUserId((String) session.getAttribute("token"));

        User isTeacher = userService.getUserById(userId);

        if (isTeacher.getUserRole()!=1){
            return new R().error("role error");
        }

        groupService.createGroup(group);

        groupService.addGroupUsers(group.getGroupId(), userId);
        return new R().success().setData("newGroup", group);
    }
}
