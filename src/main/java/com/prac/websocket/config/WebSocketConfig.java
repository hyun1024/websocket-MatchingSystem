package com.prac.websocket.config;

import com.prac.websocket.util.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), "/chat/korean")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*");

        registry.addHandler(new WebSocketHandler(), "/chat/english")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*");

    }
}