package com.jyr.DailyLog.domain;

import lombok.Getter;

@Getter
public enum Emotion {
    HAPPY("행복"),
    SAD("슬픔"),
    ANGER("분노"),
    CALM("평온");

    private final String description;

    Emotion(String description){
        this.description = description;
    }
}
