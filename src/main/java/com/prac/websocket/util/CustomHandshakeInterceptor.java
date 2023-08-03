package com.prac.websocket.util;

import com.prac.websocket.entity.UserMatchList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    private final UserMatchList userMatchList;

    @Autowired
    public CustomHandshakeInterceptor(UserMatchList userMatchList) {
        this.userMatchList = userMatchList;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 요청의 헤더에서 언어 정보 추출
        log.info("language 추출");
        String language = request.getHeaders().getFirst("language");
        if(language!=null){
            findMatch(attributes, language);
            attributes.put("language", language);
        }
        log.info("language 저장");
        // 연결 정보에 언어 정보 저장

        return true;
    }

    private void findMatch(Map<String, Object> attributes, String language) {
        StompSession session = userMatchList.findMatch(language);
        if(session!=null){
            String sessionId = session.getSessionId();
            attributes.put("successMatching", true);
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
    }
}