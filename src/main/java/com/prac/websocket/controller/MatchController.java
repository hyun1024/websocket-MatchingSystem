package com.prac.websocket.controller;

import com.prac.websocket.dto.MatchInfoRequestDto;
import com.prac.websocket.dto.RoomStatusDto;
import com.prac.websocket.service.MatchService;
import com.prac.websocket.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MatchController {

    private final MatchService matchService;
    private final MessageService messageService;

    @MessageMapping("/start/{endpoint}")
    public void checkMatch(@DestinationVariable String endpoint, Message message) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        MatchInfoRequestDto matchInfoRequestDto = messageService.parseHeadersInfo(headerAccessor);
        matchService.findMatch(matchInfoRequestDto);
        RoomStatusDto roomStatusDto = null;
        if (matchInfoRequestDto.isMatched()) {
            roomStatusDto = matchService.checkDirectMatch(matchInfoRequestDto.getUserEndpoint());
        }
        messageService.sendSearchResult(endpoint, roomStatusDto);
    }

    @MessageMapping("/stop/{endpoint}")
    public void stopMatch(@DestinationVariable String endpoint, Message message){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String userId = headerAccessor.getNativeHeader("userId").get(0);
        matchService.removeFromList(userId);

        messageService.sendStopMatchResult(endpoint);
    }
}
