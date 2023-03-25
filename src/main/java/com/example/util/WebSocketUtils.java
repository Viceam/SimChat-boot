package com.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketUtils.class);

    // 存储 websocket session
    public static final Map<String, Session> ONLINE_USER_SESSIONS = new ConcurrentHashMap<>();

    public static void sendMessage(Session session, String message) {
        if(session == null) return;
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null) {
            return;
        }
        try {
            basic.sendText(message);
        } catch (IOException e) {
            logger.error("sendMessage IOException ",e);
        }
    }

    public static void sendMessageAll(String message){
        ONLINE_USER_SESSIONS.forEach((sessionId, session) -> sendMessage(session, message));
    }
}
