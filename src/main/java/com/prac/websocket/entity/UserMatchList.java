package com.prac.websocket.entity;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;

@Component
public class UserMatchList {
    //Map으로 구현했을 때
    private final LinkedList<StompSession> englishUserMatchList = new LinkedList<>();
    private final LinkedList<StompSession> koreanUserMatchList = new LinkedList<>();

    public synchronized StompSession findMatch(String language) {
        if (language.equals("KOREAN")) {
            StompSession matchSession = findEnglishUser();
            return matchSession;
        } else if (language.equals("ENGLISH")) {
            StompSession matchSession = findKoreanUser();
            return matchSession;
        }
        return null;
    }

    public synchronized void removeFromList(StompSession session) {
        englishUserMatchList.remove(session);
    }

    private StompSession findEnglishUser() {
        if(isEnglishUser()) {
            return searchEnglishUserFromCondition();
            }
        return null;
    }
    private boolean isEnglishUser() {
        return !englishUserMatchList.isEmpty();
    }
    //카테고리 추가 등 확장성 고려해서 분리
    private StompSession searchEnglishUserFromCondition(){
        for(StompSession session : englishUserMatchList){
            //여기에 카테고리 등 조건 추가
            englishUserMatchList.remove(session);
            return session;
        }
        return null;
    }


    private StompSession findKoreanUser(){
        for (StompSession session : koreanUserMatchList){
            koreanUserMatchList.remove(session);
            return session;
        }
        return null;
    }

    private boolean isKoreanUser() {
        return !koreanUserMatchList.isEmpty();
    }
    private StompSession searchKoreanUserFromCondition(){
        for(StompSession session : koreanUserMatchList){
            koreanUserMatchList.remove(session);
            return session;
        }
        return null;
    }
}
