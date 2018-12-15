package service.event;

import model.po.User;
import org.springframework.context.ApplicationEvent;

public class UserRegisterEvent extends ApplicationEvent {
    public UserRegisterEvent(User user) {
        super(user);
    }
}
