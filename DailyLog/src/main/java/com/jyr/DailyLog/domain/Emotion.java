package com.jyr.DailyLog.domain;

import lombok.Getter;

@Getter
public enum Emotion {
    HAPPY(5,"행복"),
    SAD(-3,"슬픔"),
    ANGER(-5,"분노"),
    CALM(3,"평온");

    private final int score;
    private final String description;

    Emotion(int score, String description){
        this.score = score;
        this.description = description;
    }
}
