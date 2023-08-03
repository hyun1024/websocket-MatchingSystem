package com.prac.websocket.controller;

import com.prac.websocket.entity.MatchMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MathController {
//
//    영어와 한글의 1:1 매치
//    카테고리 관심사 {1:1}
//    어느정도 대기시간이 길어지면 더 넓은 관심사로 확장해서 다시 매칭을 하게끔 어떻게 구현할지

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    @MessageMapping("/test")
    public void findEnglishUser(Message message){
        log.info("영어사용자 찾기 메서드 시작");
        MatchMessage matchMessage = MatchMessage.builder()
                .matchId(message.getHeaders().getId())
                .category("회화")
                .content(message.getPayload())
                .sender("익명")
                .build();
        simpMessageSendingOperations.convertAndSend("/topic/test", matchMessage);
    }

    @MessageMapping("/korean")
    public void findKoreanUser(Message message){
        log.info("한국어사용자 찾기 메서드 시작");
        simpMessageSendingOperations.convertAndSend("/match/korean", message.getPayload());
    }
}
