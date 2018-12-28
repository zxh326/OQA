package service.event;

import io.netty.channel.ChannelHandlerContext;
import model.po.User;
import model.vo.R;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import utils.ChatType;
import utils.Constant;

import java.util.Map;

@Service
public class NotifyTonlineListener implements ApplicationListener<TeacherOnLineEvent> {

    @Override
    public void onApplicationEvent(TeacherOnLineEvent teacherOnLineEvent) {
        User teacher = (User) teacherOnLineEvent.getSource();
        boolean canNotify = System.currentTimeMillis() / 1000 -
                Constant.lastOnlineTeacher.getOrDefault(teacher.getUserId(), 0) > 30;

        // 是否推送
        if (canNotify){
            // 遍历在线用户表
            for (Map.Entry<Integer, ChannelHandlerContext> entry: Constant.onlineUserMap.entrySet()
            ) {
                Constant.sendMessage(entry.getValue(), new R()
                        .success()
                        .setData("teacher", teacher)
                        .setData("type", ChatType.NOTIFYONLINE));
            }
            Constant.lastOnlineTeacher.put(teacher.getUserId(), (int) (System.currentTimeMillis()/1000));
        }
    }
}
