package com.prac.websocket.entity;

import com.prac.websocket.dto.RoomStatusDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
@Getter
public class ActivateRoom {

    private final Map<String, RoomStatusDto> activateRooms = Collections.synchronizedMap(new LinkedHashMap<>());
}
