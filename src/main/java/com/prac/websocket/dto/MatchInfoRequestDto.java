package com.prac.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MatchInfoRequestDto {
    private String userLanguage;
    private String targetLanguage;
    private String userEndpoint;
    private boolean isMatched = false;
    private LocalDateTime createdAt;
    @Builder
    public MatchInfoRequestDto(String userLanguage, String targetLanguage, String userEndpoint) {
        this.userLanguage = userLanguage;
        this.targetLanguage = targetLanguage;
        this.userEndpoint = userEndpoint;
        this.createdAt = LocalDateTime.now();
    }

    public void successMatch(MatchInfoRequestDto matchInfoRequestDto){
        this.isMatched = true;
    }
}