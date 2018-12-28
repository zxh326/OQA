package service.event;

import com.alibaba.fastjson.JSONObject;
import model.vo.GroupMessage;
import model.vo.R;
import org.springframework.context.ApplicationEvent;

public class GroupSendEvent extends ApplicationEvent {
    public GroupSendEvent(GroupMessage groupMessage) {
        super(groupMessage);
    }
}
