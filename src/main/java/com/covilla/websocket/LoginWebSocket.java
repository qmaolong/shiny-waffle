package com.covilla.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
@ServerEndpoint(value="/websocket",configurator=GetHttpSessionConfigurator.class)
public class LoginWebSocket {
    private Logger logger = LoggerFactory.getLogger(LoginWebSocket.class);
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<LoginWebSocket> connections =
            new CopyOnWriteArraySet<LoginWebSocket>();

    private final String nickname;
    private Session session;
    private HttpSession httpSession;
    private String loginCode;

    public LoginWebSocket() {
        nickname = "游客ID：" + connectionIds.getAndIncrement();
    }

    @OnOpen
    public void start(Session session) {
        System.out.println("opened");
        this.session = session;
        this.httpSession = (HttpSession)session.getUserProperties().get(HttpSession.class.getName());
        connections.add(this);
        WebSocketPool.SOCKETS.add(this);
        String message = String.format("微信扫码登录");
        sendMessage(message);
    }


    @OnClose
    public void end() {
        connections.remove(this);
        WebSocketPool.removeSocket(this);
        System.out.println(this.getLoginCode() + "失效");
    }


    @OnMessage
    public void receive(String message) {
        // Never trust the client
        this.loginCode = message;
    }

    /**
     * 异常时触发
     * @param relationId
     * @param session
     */
    @OnError
    public void onError(@PathParam("loginCode") String relationId,
                        Throwable throwable,
                        Session session) {
        logger.info(throwable.getMessage(), throwable);
        WebSocketPool.removeSocket(this);
    }

    public void sendMessage(String message){
        try {
            this.session.getBasicRemote().sendText(message);
        }catch (IOException e){
            connections.remove(this);
            try {
                this.session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }


    private static void broadcast(String msg) {
        for (LoginWebSocket client : connections) {
            try {
                client.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("* %s %s",
                        client.nickname, "has been disconnected.");
                broadcast(message);
            }//try 
        }//for
    }//void broadcast(String msg)

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
}