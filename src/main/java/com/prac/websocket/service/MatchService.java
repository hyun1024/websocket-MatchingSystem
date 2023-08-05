package com.prac.websocket.service;

import com.prac.websocket.dto.RoomStatusDto;
import com.prac.websocket.dto.UserMatchDto;
import com.prac.websocket.entity.ActivateRoom;
import com.prac.websocket.entity.ChatQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public synchronized RoomStatusDto checkDirectMatch(String userId) {
        for (RoomStatusDto roomStatusDto : activeRoom.getActivateRooms()) {
            if (roomStatusDto.getRequestUserId().equals(userId)) {
                return roomStatusDto;
            }
        }
        return null;
    }


    public synchronized UserMatchDto findMatch(UserMatchDto userMatchDto) {
        log.info("findMatch 시작");
        if (chatQueue.getChatQueue().isEmpty()) {
            directInsertQueueIfFirstUser(userMatchDto);
            return null;
        }
        if (chatQueue.getChatQueue().containsKey(userMatchDto.getUserId())) {
            log.info("이미 대기열에 참여중인 userId가 재시도.");
            return null;
        }
        searchUserFromCondition(userMatchDto);
        return userMatchDto;
    }

    private void directInsertQueueIfFirstUser(UserMatchDto userMatchDto) {
        log.info("대기중인 유저가 없으므로 대기리스트에 추가");
        chatQueue.getChatQueue().put(userMatchDto.getUserId(), userMatchDto);

    }

    private UserMatchDto searchUserFromCondition(UserMatchDto requestUserDto) {
        for (UserMatchDto waitUserDto : chatQueue.getChatQueue().values()) {
            if (waitUserDto.getUserLanguage().equals(requestUserDto.getTargetLanguage())) {
                activeRoom.getActivateRooms().add(RoomStatusDto.builder()
                        .waitUserId(waitUserDto.getUserId())
                        .requestUserId(requestUserDto.getUserId())
                        .waitUserEndpoint(waitUserDto.getUserEndpoint())
                        .requestUserEndpoint(requestUserDto.getUserEndpoint())
                        .build()
                );
                chatQueue.getChatQueue().remove(waitUserDto.getUserId());
                log.info("매칭 성공. 남은 " + requestUserDto.getTargetLanguage() + " 매칭 대기자 : " + chatQueue.getChatQueue().size() + " 명");
                log.info("추가 후 현재 활성화된 대화 방 수 : " + activeRoom.getActivateRooms().size());
                requestUserDto.successMatch(waitUserDto);
                return requestUserDto;
            } else {
                chatQueue.getChatQueue().put(requestUserDto.getUserId(), requestUserDto);
                log.info("매칭에 실패했으므로 대기 리스트에 추가. 현재 " + requestUserDto.getUserLanguage() + " 사용자 매칭 대기자 : " + chatQueue.getChatQueue().size() + " 명");
                return requestUserDto;
            }
        }
        return null;
    }

    public void removeFromList(String userId) {
        chatQueue.getChatQueue().remove(userId);
    }
}
