package com.prac.websocket.entity;

import com.prac.websocket.dto.UserMatchDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
@Getter
public class ChatQueue {

    private final Map<String, UserMatchDto> chatQueue = Collections.synchronizedMap(new LinkedHashMap<>());
}
