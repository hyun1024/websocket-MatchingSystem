package com.prac.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RoomStatusDto {

    private String user1Endpoint;
    private String user2Endpoint;
    private LocalDateTime createdAt;

    @Builder
    public RoomStatusDto(String user1Endpoint, String user2Endpoint) {
        this.user1Endpoint = user1Endpoint;
        this.user2Endpoint = user2Endpoint;
        this.createdAt = LocalDateTime.now();
    }

    public static RoomStatusDto of(MatchInfoRequestDto waitUserDto, MatchInfoRequestDto requestUserDto){
        return RoomStatusDto.builder()
                .user1Endpoint(waitUserDto.getUserEndpoint())
                .user2Endpoint(requestUserDto.getUserEndpoint())
                .build();
    }
}

