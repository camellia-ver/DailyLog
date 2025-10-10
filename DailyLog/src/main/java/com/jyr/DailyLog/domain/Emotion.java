package com.jyr.DailyLog.domain;

import lombok.Getter;

@Getter
public enum Emotion {
    HAPPY(7,"행복"),
    SAD(3,"슬픔"),
    ANGER(1,"분노"),
    CALM(5,"평온");

    private final int score;
    private final String description;

    Emotion(int score, String description){
        this.score = score;
        this.description = description;
    }
}
