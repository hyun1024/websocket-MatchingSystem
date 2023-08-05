package com.prac.websocket.controller;

import com.prac.websocket.dto.UserMatchDto;
import com.prac.websocket.service.MatchService;
import com.prac.websocket.dto.RoomStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MatchController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final MatchService matchService;

    @MessageMapping("/{endpoint}")
    public void checkMatch(@DestinationVariable String endpoint, Message message) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        UserMatchDto userMatchDto = UserMatchDto.builder()
//                .userEndpoint(headerAccessor.getNativeHeader("path").get(0))
//                .userLanguage(headerAccessor.getNativeHeader("userLanguage").get(0))
//                .targetLanguage(headerAccessor.getNativeHeader("targetLanguage").get(0))
//                .userId(headerAccessor.getNativeHeader("userId").get(0))
//                .build();
//        matchService.findMatch(userMatchDto);
        Map<String, Object> header = new HashMap<>();
        log.info("controller 진입 endpoint: " + endpoint);
        RoomStatusDto roomStatusDto = matchService.checkDirectMatch(headerAccessor.getNativeHeader("userId").get(0));
        if (roomStatusDto != null) {
            log.info("매칭이 성공하면 이게 뜹니다.");
            header.put("isMatch", true);
            header.put("path", roomStatusDto.getWaitUserEndpoint());
            simpMessageSendingOperations.convertAndSend("/match/" + endpoint, "매칭이 즉시 가능합니다. 헤더의 path를 endpoint로 연결 정보를 전송해주세요.", header);
            header.remove("path");
            header.put("path", roomStatusDto.getRequestUserEndpoint());
            simpMessageSendingOperations.convertAndSend("/match/" + roomStatusDto.getWaitUserEndpoint(), "매칭 유저가 탐지되었습니다. 헤더의 path를 endpoint로 연결 정보를 전송해주세요.", header);
        } else {
            log.info("매칭이 실패하면 이게 뜹니다.");
            header.put("isMatch", false);
            simpMessageSendingOperations.convertAndSend("/match/" + endpoint, "현재 매칭 가능한 유저가 없습니다. 조금만 더 기다려주세요.", header);
        }

    }
}
