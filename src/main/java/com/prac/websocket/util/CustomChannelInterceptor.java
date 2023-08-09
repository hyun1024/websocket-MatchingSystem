package com.prac.websocket.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class CustomChannelInterceptor implements ChannelInterceptor {


    public CustomChannelInterceptor() {
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            return message;
        } else if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            return message;
        } else if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
            return message;
        }
        return message;
    }
}