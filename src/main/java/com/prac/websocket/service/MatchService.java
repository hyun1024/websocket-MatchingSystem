package com.prac.websocket.service;

import com.prac.websocket.dto.MatchInfoRequestDto;
import com.prac.websocket.dto.RoomStatusDto;
import com.prac.websocket.entity.ActivateRoom;
import com.prac.websocket.entity.ChatQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Block;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MatchService {
    private final ChatQueue chatQueue;
    private final ActivateRoom activeRoom;

    @Autowired
    public MatchService(ChatQueue chatQueue, ActivateRoom activeRoom) {
        this.chatQueue = chatQueue;
        this.activeRoom = activeRoom;
    }

    public synchronized RoomStatusDto checkDirectMatch(String loginId) {
        for (RoomStatusDto roomStatusDto : activeRoom.getActivateRooms()) {
            if (roomStatusDto.getUser2Endpoint().equals(loginId)) {
                return roomStatusDto;
            }
        }
        return null;
    }


    public synchronized void findMatch(MatchInfoRequestDto matchInfoRequestDto) {
        if (chatQueue.getChatQueue().isEmpty()) {
            addUserToQueueIfEmpty(matchInfoRequestDto);
            return;
        }
        isUserAlreadyInCollection(matchInfoRequestDto);

        searchUserFromCondition(matchInfoRequestDto);
    }

    private void addUserToQueueIfEmpty(MatchInfoRequestDto matchInfoRequestDto) {
        chatQueue.getChatQueue().put(matchInfoRequestDto.getUserEndpoint(), matchInfoRequestDto);
    }

    private void isUserAlreadyInCollection(MatchInfoRequestDto matchInfoRequestDto) {
        if (chatQueue.getChatQueue().containsKey(matchInfoRequestDto.getUserEndpoint())) {
            throw new IllegalArgumentException("이미 대기열에 등록된 유저입니다. 매칭 정보를 변경하고 싶은 경우 매칭 취소 후 다시 시도해주세요");
        }
    }

    private void searchUserFromCondition(MatchInfoRequestDto requestUserDto) {
        for (MatchInfoRequestDto waitUserDto : chatQueue.getChatQueue().values()) {
            if (isTargetLanguage(waitUserDto, requestUserDto)) {
                activeRoom.getActivateRooms().add(RoomStatusDto.of(waitUserDto, requestUserDto));
                chatQueue.getChatQueue().remove(waitUserDto.getUserEndpoint());
                log.info("현재 활성화된 대화 방 수 : " + activeRoom.getActivateRooms().size());
                requestUserDto.successMatch(waitUserDto);
                return;
            }
        }
        chatQueue.getChatQueue().put(requestUserDto.getUserEndpoint(), requestUserDto);
    }

    private boolean isTargetLanguage(MatchInfoRequestDto waitUserDto, MatchInfoRequestDto requestUserDto) {
        return waitUserDto.getUserLanguage().equals(requestUserDto.getTargetLanguage());
    }

    public void removeFromList(String userId) {
        log.info("chatQueue size = " + chatQueue.getChatQueue().size());
        chatQueue.getChatQueue().remove(userId);
        log.info("remove 후 chatQueue size = " + chatQueue.getChatQueue().size());
    }
}