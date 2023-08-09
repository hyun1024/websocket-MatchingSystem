package com.prac.websocket.service;

import com.prac.websocket.dto.MatchInfoRequestDto;
import com.prac.websocket.dto.RoomStatusDto;
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

    public MatchInfoRequestDto parseHeadersInfo(StompHeaderAccessor headerAccessor) {
       String userId = headerAccessor.getNativeHeader("userId").get(0);
        return MatchInfoRequestDto.builder()
                .userEndpoint(headerAccessor.getNativeHeader("path").get(0))
                .userLanguage(headerAccessor.getNativeHeader("userLanguage").get(0))
                .targetLanguage(headerAccessor.getNativeHeader("targetLanguage").get(0))
                .build();
    }

    public void sendSearchResult(String endpoint, RoomStatusDto roomStatusDto) {

        if (roomStatusDto != null) {
            sendMatchInfoToEachUser(endpoint, roomStatusDto);
        } else {
            sendWaitMessageToRequestUser(endpoint);
        }
    }

    private void sendWaitMessageToRequestUser(String endpoint) {
        Map<String, Object> header = new HashMap<>();
        header.put("isMatch", false);
        simpMessageSendingOperations.convertAndSend("/match/" + endpoint, "현재 매칭 가능한 유저가 없습니다. 조금만 더 기다려주세요.", header);
    }

    private void sendMatchInfoToEachUser(String endpoint, RoomStatusDto roomStatusDto) {
        sendSuccessMessageToRequestUser(endpoint, roomStatusDto);
        sendSuccessMessageToWaitUser(roomStatusDto);
    }

    private void sendSuccessMessageToRequestUser(String endpoint, RoomStatusDto roomStatusDto) {
        Map<String, Object> header = new HashMap<>();
        header.put("isMatch", true);
        header.put("path", roomStatusDto.getUser1Endpoint());
        simpMessageSendingOperations.convertAndSend("/match/" + endpoint, "매칭이 즉시 가능합니다. 헤더의 path를 endpoint로 연결 정보를 전송합니다.", header);
    }
    private void sendSuccessMessageToWaitUser(RoomStatusDto roomStatusDto) {
        Map<String, Object> header = new HashMap<>();
        header.put("isMatch", true);
        header.put("path", roomStatusDto.getUser2Endpoint());
        simpMessageSendingOperations.convertAndSend("/match/" + roomStatusDto.getUser1Endpoint(), "매칭 유저가 탐지되었습니다. 헤더의 path를 endpoint로 연결 정보를 전송합니다.", header);
    }

    public void sendStopMatchResult(String endpoint) {
        simpMessageSendingOperations.convertAndSend("/match/"+endpoint, "매칭 취소가 완료되었습니다!");
    }
}