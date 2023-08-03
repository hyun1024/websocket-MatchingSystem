package com.prac.websocket.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MatchMessage {
    private UUID matchId;
    private String sender;
    private String category;
    private Object content;

    @Builder
    public MatchMessage(UUID matchId, String sender, String category,Object content) {
        this.matchId = matchId;
        this.sender = sender;
        this.category = category;
        this.content = content;
    }
    private void updateSender(String sender){
        this.sender = sender;
    }
}
