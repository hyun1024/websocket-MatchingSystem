//package com.prac.websocket.util;
//
//import com.prac.websocket.entity.UserMatch;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import java.util.Map;
//
//@Component
//@Slf4j
//public class CustomHandshakeInterceptor implements HandshakeInterceptor {
//    private final UserMatch userMatch;
//
//    @Autowired
//    public CustomHandshakeInterceptor(UserMatch userMatch) {
//        this.userMatch = userMatch;
//    }
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        return true;
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
//    }
//}