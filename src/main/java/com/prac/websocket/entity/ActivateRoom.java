package com.prac.websocket.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
@Getter
public class ActivateRoom {

    private final Map<String, RoomStatus> activateRooms = new LinkedHashMap<>();
}
