package service.event;

import dao.UserDao;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import model.po.Group;
import model.po.User;
import model.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import utils.ChatType;
import utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SendInfoListener implements ApplicationListener<UserRegisterEvent> {
    @Autowired
    UserDao userDao;

    @Override
    public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
        User user = (User) userRegisterEvent.getSource();

        // 发送用户group 和当前在线teacher信息
        List<Group> groups = userDao.getUserGroupById(user.getUserId());
        ChannelHandlerContext ctx;

        List<Integer> teacherIds = new ArrayList<>(Constant.onlineTeacher.keySet());

        if (teacherIds.isEmpty()){
            teacherIds.add(0);
        }
        R responseData = new R().success()
                .setData("groups", groups)
                .setData("type", ChatType.SENDINFOS);
        if (user.getUserRole() != 1){
            ctx  = Constant.onlineUserMap.get(user.getUserId());
            responseData.setData("teachers", userDao.getUserByIds(teacherIds));
        }else{
            ctx = Constant.onlineTeacher.get(user.getUserId());
        }
        Constant.sendMessage(ctx, responseData);
    }
}
