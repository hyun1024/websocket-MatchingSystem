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
    private final ActivateRoom activateRoom;

    @Autowired
    public MatchService(ChatQueue chatQueue, ActivateRoom activateRoom) {
        this.chatQueue = chatQueue;
        this.activateRoom = activateRoom;
    }

    public RoomStatusDto checkDirectMatch(String userId) {
        for (RoomStatusDto roomStatusDto : activateRoom.getActivateRooms().values()) {
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
            log.info("이미 대기열에 참여중인 사람입니다. 대기열을 취소 후 다시 시도해주세요.");
           throw new RuntimeException("이미 대기열에 참여중인 사람입니다. 대기열을 취소 후 다시 시도해주세요.");
        }
        searchUserFromCondition(userMatchDto);
        return userMatchDto;
    }

    private void directInsertQueueIfFirstUser(UserMatchDto userMatchDto) {
        chatQueue.getChatQueue().put(userMatchDto.getUserId(), userMatchDto);
        log.info("대기중인 유저가 없으므로 대기리스트에 추가");
    }

    private void searchUserFromCondition(UserMatchDto requestUserDto) {
        for (UserMatchDto waitUserDto : chatQueue.getChatQueue().values()) {
            if (waitUserDto.getUserLanguage().equals(requestUserDto.getTargetLanguage())) {
                activateRoom.getActivateRooms().put(waitUserDto.getUserId(),
                        RoomStatusDto.builder()
                                .waitUserId(waitUserDto.getUserId())
                                .requestUserId(requestUserDto.getUserId())
                                .waitUserEndpoint(waitUserDto.getUserEndpoint())
                                .requestUserEndpoint(requestUserDto.getUserEndpoint())
                                .build()
                );
                chatQueue.getChatQueue().remove(waitUserDto.getUserId());
                log.info("매칭에 성공했으므로 매치성공여부 update. 남은 " + requestUserDto.getTargetLanguage() + " 사용자 매칭 대기자 : " + chatQueue.getChatQueue().size() + " 명");
                log.info("추가 후 현재 활성화된 대화 방 수 : " + activateRoom.getActivateRooms().size());
                requestUserDto.successMatch(waitUserDto);
            } else {
                chatQueue.getChatQueue().put(requestUserDto.getUserId(), requestUserDto);
                log.info("매칭에 실패했으므로 대기 리스트에 추가. 현재 " + requestUserDto.getUserLanguage() + " 사용자 매칭 대기자 : " + chatQueue.getChatQueue().size() + " 명");

            }
        }
    }

    public synchronized void removeFromList(UserMatchDto userMatchDto) {
        chatQueue.getChatQueue().remove(userMatchDto.getUserId());
    }
}
