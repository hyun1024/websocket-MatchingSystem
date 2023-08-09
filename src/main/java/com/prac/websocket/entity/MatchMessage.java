package com.prac.websocket.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchMessage {
    private String sender;
    private String content;
}
