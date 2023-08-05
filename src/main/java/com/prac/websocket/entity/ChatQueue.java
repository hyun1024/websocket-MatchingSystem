package com.prac.websocket.entity;

import com.prac.websocket.dto.UserMatchDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
@Getter
public class ChatQueue {

    private final Map<String, UserMatchDto> chatQueue = Collections.synchronizedMap(new LinkedHashMap<>());

    @Scheduled(fixedRate = 30 * 60 * 1000) // 30분(ms 단위)
    public void removeInactiveUserMatches() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);

        List<String> removedUserIds = new ArrayList<>();
        synchronized (chatQueue) {
            Iterator<Map.Entry<String, UserMatchDto>> iterator = chatQueue.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, UserMatchDto> entry = iterator.next();
                UserMatchDto userMatchDto = entry.getValue();
                if (userMatchDto.getCreatedAt().isBefore(thirtyMinutesAgo)) {
                    iterator.remove();
                    removedUserIds.add(entry.getKey());
                }
            }
        }

        log.info("Removed {} inactive UserMatchDto(s) with userId(s): {}", removedUserIds.size(), removedUserIds);
    }

}
