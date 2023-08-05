package com.prac.websocket.controller;

import com.prac.websocket.dto.RoomStatusDto;
import com.prac.websocket.dto.UserMatchDto;
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
public class SocketController {
    
    private final MatchService matchService;
    private final MessageService messageService;

    @MessageMapping("/{endpoint}")
    public void checkMatch(@DestinationVariable String endpoint, Message message) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        UserMatchDto userMatchDto = messageService.parseHeadersInfo(headerAccessor);
        matchService.findMatch(userMatchDto);
        RoomStatusDto roomStatusDto = null;
        if (userMatchDto.isMatched()) {
            roomStatusDto = matchService.checkDirectMatch(userMatchDto.getUserId());
        }
        messageService.SendSearchResult(endpoint, roomStatusDto);
    }




    @MessageMapping("/chat/{endpoint}")
    public void sendChat(@DestinationVariable String endpoint, Message message){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    }
}

