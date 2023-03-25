package com.example.controller;

import com.example.util.WebSocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

import static com.example.util.WebSocketUtils.ONLINE_USER_SESSIONS;
import static com.example.util.WebSocketUtils.sendMessageAll;

@RestController
@ServerEndpoint("/websocket/{username}")
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){

        ONLINE_USER_SESSIONS.put(username, session);
        String message = "欢迎用户[" + username + "] 来到聊天室！";
        logger.info("用户登录："+message);
        sendMessageAll(message);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username){
        logger.info("发送消息："+message);
        sendMessageAll("[" + username + "] : " + message);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        try {
            session.close();
        } catch (IOException e) {
            logger.error("onError exception",e);
        }
        logger.info("Throwable msg "+throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username){
        //当前的Session 移除
        ONLINE_USER_SESSIONS.remove(username);
        //广播
        sendMessageAll("用户[" + username + "] 已经离开聊天室了！");
        try {
            session.close();
        } catch (IOException e) {
            logger.error("onClose error",e);
        }
    }
}
