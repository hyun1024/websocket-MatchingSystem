package com.prac.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserMatchDto {
    private String language;
    private String matchId;
    private String sessionId;
    private boolean isMatched = false;
    @Builder
    public UserMatchDto(String language, String matchId,String sessionId) {
        this.language = language;
        this.matchId = matchId;
        this.sessionId = sessionId;
    }
    public void successMatching(UserMatchDto userMatchDto) {
        this.isMatched = true;
        this.matchId = userMatchDto.getMatchId();
    }
}
