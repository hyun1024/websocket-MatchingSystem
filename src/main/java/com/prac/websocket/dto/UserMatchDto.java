package com.prac.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserMatchDto {
    private String userLanguage;
    private String targetLanguage;
    private String userEndpoint;
    private String userId;
    private boolean isMatched = false;
    private LocalDateTime createdAt;
    @Builder
    public UserMatchDto(String userLanguage, String targetLanguage, String userEndpoint, String userId) {
        this.userLanguage = userLanguage;
        this.targetLanguage = targetLanguage;
        this.userEndpoint = userEndpoint;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

   public void successMatch(UserMatchDto userMatchDto){
        this.isMatched = true;

    }

}
