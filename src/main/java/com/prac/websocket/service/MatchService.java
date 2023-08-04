package com.prac.websocket.service;

import com.prac.websocket.dto.UserMatchDto;
import com.prac.websocket.entity.ActivateRoom;
import com.prac.websocket.entity.RoomStatus;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MatchService {
    private final ActivateRoom activateRoom;

    public MatchService(ActivateRoom activateRoom) {
        this.activateRoom = activateRoom;
    }

    public String checkDirectMatch(String sendUrl){
        for(RoomStatus roomStatus : activateRoom.getActivateRooms().values()){
            if(roomStatus.getRequestId().equals(sendUrl)){
                return roomStatus.getMatchId();
            }
        }

        return null;
    }

}
