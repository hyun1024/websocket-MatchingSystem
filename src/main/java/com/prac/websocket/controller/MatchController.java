package com.prac.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MatchController {
    //private final SimpMessageSendingOperations simpMessageSendingOperations;
    @MessageMapping("/english")
    public void findEnglishUser(Message message){


  //      simpMessageSendingOperations.convertAndSend("/topic/test");
    }

    @MessageMapping("/korean")
    public void findKoreanUser(Message message){
   //     simpMessageSendingOperations.convertAndSend("/match/korean", message.getPayload());
    }
}
