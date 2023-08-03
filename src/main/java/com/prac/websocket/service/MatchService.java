package com.prac.websocket.service;

import com.prac.websocket.dto.MatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
@Slf4j
public class MatchService {
    private Queue<String> matchQueue = new LinkedList<>();

    public MatchDto findEnglishUser(){
        return null;
    }
    // 매칭에 참여한 순서를 큐에 추가하는 메서드
    public void addToMatchQueue(String userId) {
        matchQueue.add(userId);
    }

    // 부합하는 사람 중 제일 오래 기다린 사람을 반환하는 메서드
    public String getOldestMatchedUser() {
        return matchQueue.poll(); // 큐의 맨 앞의 요소를 제거하고 반환
    }
}

