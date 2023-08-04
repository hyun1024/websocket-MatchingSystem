package com.prac.websocket.util;

import com.prac.websocket.dto.UserMatchDto;
import com.prac.websocket.entity.UserMatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class CustomChannelInterceptor implements ChannelInterceptor {

    private final UserMatch userMatch;

    public CustomChannelInterceptor(UserMatch userMatch) {
        this.userMatch = userMatch;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            UserMatchDto userMatchDto = UserMatchDto.builder()
                    .matchId(headerAccessor.getNativeHeader("path").get(0))
                    .language(headerAccessor.getNativeHeader("language").get(0))
                    .sessionId(headerAccessor.getSessionId())
                    .build();
            UserMatchDto findMatchResult = userMatch.findMatch(userMatchDto);
            log.info("CONNECT preSend 완료");
            return message;
        } else if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            log.info("SUBSCRIBE 시작");
            String path = headerAccessor.getNativeHeader("destination").get(0);
            String[] paths = path.split("/");
            log.info("path = " + paths[paths.length - 1]);
            log.info("SUBSCRIBE");
            return message;
        } else if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
            return message;
        }
        return message;
    }
}
