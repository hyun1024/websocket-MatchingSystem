package com.prac.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchIdDto {

    private String matchId;
    @Builder
    public MatchIdDto(String matchId) {
        this.matchId = matchId;
    }
}
