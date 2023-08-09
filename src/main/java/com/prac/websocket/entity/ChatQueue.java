package com.prac.websocket.entity;

import com.prac.websocket.dto.MatchInfoRequestDto;
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

    private final Map<String, MatchInfoRequestDto> chatQueue = Collections.synchronizedMap(new LinkedHashMap<>());

    @Scheduled(fixedRate = 30 * 60 * 1000) // 30분(ms 단위)
    public void removeInactiveUserMatches() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);

        List<String> removedUserIds = new ArrayList<>();
        synchronized (chatQueue) {
            Iterator<Map.Entry<String, MatchInfoRequestDto>> iterator = chatQueue.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, MatchInfoRequestDto> entry = iterator.next();
                MatchInfoRequestDto matchInfoRequestDto = entry.getValue();
                if (matchInfoRequestDto.getCreatedAt().isBefore(thirtyMinutesAgo)) {
                    iterator.remove();
                    removedUserIds.add(entry.getKey().toString());
                }
            }
        }

        log.info("Removed {} inactive MatchInfoRequestDto(s) with userId(s): {}", removedUserIds.size(), removedUserIds);
    }
}
