package service.event;

import dao.UserDao;
import io.netty.channel.ChannelHandlerContext;
import model.po.Group;
import model.po.User;
import model.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import utils.ChatType;
import utils.Constant;

import java.util.List;
import java.util.Map;

@Service
public class SendInfoListener implements ApplicationListener<UserRegisterEvent> {
    @Autowired
    UserDao userDao;

    @Override
    public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
        User user = (User) userRegisterEvent.getSource();

        // 发送用户group 和当前在线teacher信息
        List<Group> groups = userDao.getUserGroupById(user.getUserId());

        List<Integer> teacherIds = Constant.onlineTeacher;

        if (teacherIds.isEmpty()){
            teacherIds.add(0);
        }



        Constant.sendMessage(Constant.onlineUserMap.get(user.getUserId()), new R().success()
                .setData("groups",groups)
                .setData("teachers", userDao.getUserByIds(teacherIds))
                .setData("type", ChatType.SENDINFOS));

    }
}
