package com.prac.websocket.controller;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.prac.websocket.dto.MatchIdDto;
import com.prac.websocket.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MatchController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final MatchService matchService;
    @MessageMapping("/english")
    public void findEnglishUser(Message message){

        simpMessageSendingOperations.convertAndSend("/topic/test", message.getPayload());
    }
    @MessageMapping("/korean")
    public void findKoreanUser(Message message){

        simpMessageSendingOperations.convertAndSend("/match/korean", message.getPayload());
    }

    @MessageMapping("/{sendurl}")
    public void checkMatch(@DestinationVariable String sendurl, Message message){
        Boolean isMatch;
        Map<String, Object> header = new HashMap<>();

        log.info("controller 진입 sendurl : " + sendurl);
        String matchId = matchService.checkDirectMatch(sendurl);
        if(matchId!=null) {
            log.info("매칭이 성공하면 이게 뜹니다.");
            header.put("isMatch", isMatch=true);
            simpMessageSendingOperations.convertAndSend("/match/"+sendurl, matchId, header);
        } else{
            log.info("매칭이 실패하면 이게 뜹니다.");
            header.put("isMatch", isMatch=false);
            simpMessageSendingOperations.convertAndSend("/match/"+sendurl,"조금만 더 기다려주세요.", header);
        }

    }
}
