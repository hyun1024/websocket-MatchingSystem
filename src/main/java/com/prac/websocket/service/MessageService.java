package com.prac.websocket.service;

import com.prac.websocket.dto.RoomStatusDto;
import com.prac.websocket.dto.UserMatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MessageService {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public MessageService(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }
    public UserMatchDto parseHeadersInfo(StompHeaderAccessor headerAccessor) {
        return UserMatchDto.builder()
                .userEndpoint(headerAccessor.getNativeHeader("path").get(0))
                .userLanguage(headerAccessor.getNativeHeader("userLanguage").get(0))
                .targetLanguage(headerAccessor.getNativeHeader("targetLanguage").get(0))
                .userId(headerAccessor.getNativeHeader("userId").get(0))
                .build();
    }
    public void SendSearchResult(String endpoint, RoomStatusDto roomStatusDto) {
        Map<String, Object> header = new HashMap<>();
            if (roomStatusDto != null) {
                header.put("isMatch", true);
                header.put("path", roomStatusDto.getWaitUserEndpoint());
                simpMessageSendingOperations.convertAndSend("/match/" + endpoint, "매칭이 즉시 가능합니다. 헤더의 path를 endpoint로 연결 정보를 전송해주세요.", header);
                header.remove("path");
                header.put("path", roomStatusDto.getRequestUserEndpoint());
                simpMessageSendingOperations.convertAndSend("/match/" + roomStatusDto.getWaitUserEndpoint(), "매칭 유저가 탐지되었습니다. 헤더의 path를 endpoint로 연결 정보를 전송해주세요.", header);
            } else {
            header.put("isMatch", false);
            simpMessageSendingOperations.convertAndSend("/match/" + endpoint, "현재 매칭 가능한 유저가 없습니다. 조금만 더 기다려주세요.", header);
        }
    }
}