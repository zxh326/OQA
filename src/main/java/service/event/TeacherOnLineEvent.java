package service.event;

import model.po.User;
import org.springframework.context.ApplicationEvent;

public class TeacherOnLineEvent extends ApplicationEvent {
    public TeacherOnLineEvent(User teacher) {
        super(teacher);
    }
}
