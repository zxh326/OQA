package common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import web.websocket.WebSocketServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Scope("singleton")
public class AppContext {

    @Autowired
    private WebSocketServer webSocketServer;


    private Thread nettyThread;

    @PostConstruct
    public void init() {
        System.out.println("init");
        nettyThread = new Thread(webSocketServer);
        nettyThread.start();;
    }

    @PreDestroy
    public void close() {
        webSocketServer.close();
        System.out.println("close");
    }

}
