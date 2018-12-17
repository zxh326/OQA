package service.event;

import model.po.User;
import org.springframework.context.ApplicationEvent;

public class TeacherDownEvent extends ApplicationEvent {
    public TeacherDownEvent(User teacher) {
        super(teacher);
    }
}
