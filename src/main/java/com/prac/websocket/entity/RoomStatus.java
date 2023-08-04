package com.prac.websocket.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomStatus {

    private String matchId;
    private String requestId;
    private boolean isActive = true;
    private String session1Id;
    private String session2Id;

    @Builder
    public RoomStatus(String matchId,String requestId,String session1Id, String session2Id) {
        this.matchId = matchId;
        this.requestId = requestId;
        this.session1Id = session1Id;
        this.session2Id = session2Id;
    }

    public void closeRoom(){
        this.isActive = false;
    }
}
