package com.prac.websocket.entity;

import com.prac.websocket.dto.UserMatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class UserMatch {
    private final Map<String, UserMatchDto> userMatchList = new LinkedHashMap<>();
    private final ActivateRoom activateRoom;

    @Autowired
    public UserMatch(ActivateRoom activateRoom) {
        this.activateRoom = activateRoom;
    }

    public synchronized UserMatchDto findMatch(UserMatchDto userMatchDto) {
        log.info("findMatch 시작");
        if (userMatchDto.getLanguage().equals("KOREAN")) {
            return findEnglishUser(userMatchDto);

        } else if (userMatchDto.getLanguage().equals("ENGLISH")) {
            return findKoreanUser(userMatchDto);
        }
        log.info("language = null로 return null 발생. exception 추가 필요");
        return null;
    }

    public synchronized void removeFromList(UserMatchDto userMatchDto) {
        userMatchList.remove(userMatchDto.getMatchId());
    }

    private UserMatchDto findEnglishUser(UserMatchDto userMatchDto) {
        if (isNotEmpty()) {
            return searchEnglishUserFromCondition(userMatchDto);
        }
        userMatchList.put(userMatchDto.getMatchId(), userMatchDto);
        log.info("대기중인 유저가 없으므로 대기리스트에 추가");
        return userMatchDto;
    }

    //카테고리 추가 등 확장성 고려해서 분리
    private UserMatchDto searchEnglishUserFromCondition(UserMatchDto userMatchDto) {
        for (UserMatchDto info : userMatchList.values()) {
            //여기에 카테고리 등 조건 추가
            if (info.getLanguage().equals("ENGLISH")) {

                activateRoom.getActivateRooms().put(info.getMatchId(),
                        RoomStatus.builder()
                                .session1Id(info.getSessionId())
                                .session2Id(userMatchDto.getSessionId())
                                .matchId(info.getMatchId())
                                .requestId(userMatchDto.getMatchId())
                                .build());
                userMatchList.remove(info.getMatchId());
                userMatchDto.successMatching(info);
                log.info("매칭에 성공했으므로 매치성공여부 update. 현재 한국어 사용자 매칭 대기자 : " + userMatchList.size() + " 명");
                log.info("추가 후 현재 활성화된 대화 방 수 : " + activateRoom.getActivateRooms().size());
                return userMatchDto;
            }
        }
        userMatchList.put(userMatchDto.getMatchId(), userMatchDto);
        log.info("매칭에 실패했으므로 대기 리스트에 추가. 현재 한국어 사용자 매칭 대기자 : " + userMatchList.size() + " 명");
        return userMatchDto;
    }


    private UserMatchDto findKoreanUser(UserMatchDto userMatchDto) {
        if (isNotEmpty()) {
            return searchKoreanUserFromCondition(userMatchDto);
        }
        log.info("대기중인 유저가 없으므로 대기리스트에 추가");
        userMatchList.put(userMatchDto.getMatchId(), userMatchDto);
        return userMatchDto;
    }

    private UserMatchDto searchKoreanUserFromCondition(UserMatchDto userMatchDto) {
        for (UserMatchDto info : userMatchList.values()) {
            if (info.getLanguage().equals("KOREAN")) {
                activateRoom.getActivateRooms().put(info.getMatchId(),
                        RoomStatus.builder()
                                .session1Id(info.getSessionId())
                                .session2Id(userMatchDto.getSessionId())
                                .matchId(info.getMatchId())
                                .requestId(userMatchDto.getMatchId())
                                .build());
                userMatchList.remove(info.getMatchId());
                userMatchDto.successMatching(info);
                log.info("매칭에 성공했으므로 매치성공여부 update. 현재 영어 사용자 매칭 대기자 : " + userMatchList.size() + " 명");
                log.info("추가 후 현재 활성화된 대화 방 수 : " + activateRoom.getActivateRooms().size());
                return userMatchDto;
            }
        }
        userMatchList.put(userMatchDto.getMatchId(), userMatchDto);
        log.info("매칭에 실패했으므로 대기 리스트에 추가.  현재 영어 사용자 매칭 대기자 : " + userMatchList.size() + " 명");
        return userMatchDto;
    }

    private boolean isNotEmpty() {
        return !userMatchList.isEmpty();
    }
}
