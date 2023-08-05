package com.prac.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RoomStatusDto {

    private String waitUserId;
    private String requestUserId;
    private String waitUserEndpoint;
    private String requestUserEndpoint;
    private LocalDateTime createdAt;

    @Builder
    public RoomStatusDto(String waitUserId, String requestUserId, String waitUserEndpoint, String requestUserEndpoint) {
        this.waitUserId = waitUserId;
        this.requestUserId = requestUserId;
        this.waitUserEndpoint = waitUserEndpoint;
        this.requestUserEndpoint = requestUserEndpoint;
        this.createdAt = LocalDateTime.now();
    }
}
