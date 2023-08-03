package com.prac.websocket.util;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.prac.websocket.entity.MatchMessage;
import com.prac.websocket.entity.UserMatchList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    private final UserMatchList userMatchList;

    @Autowired
    public CustomHandshakeInterceptor(UserMatchList userMatchList) {
        this.userMatchList = userMatchList;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 요청의 헤더에서 언어 정보 추출
        String language = request.getHeaders().getFirst("language");
        if (language != null) {
            findMatch(attributes, language);
            attributes.put("language", language);
        }

        return true;
    }

    private void findMatch(Map<String, Object> attributes, String language) {
       StompSession session = userMatchList.findMatch(language);
        if (session != null) {
            String matchId = UUID.randomUUID().toString();
            session.subscribe("/chat/" + matchId, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    // 메시지의 타입을 지정
                    return MatchMessage.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    // 메시지 처리 로직 작성
                    MatchMessage message = (MatchMessage) payload;
                    // ...
                }

            });
            session.send("/chat/" + matchId, "{\"sender\" : \"asdf\", \"content\" : \"나랑 대화할래?\"}");
            attributes.put("successMatching", true);
        } else {
            attributes.put("successMatching", false);
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
    }
}