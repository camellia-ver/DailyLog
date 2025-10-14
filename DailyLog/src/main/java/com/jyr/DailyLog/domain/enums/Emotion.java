package com.jyr.DailyLog.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Emotion {
    HAPPY(7,"행복", "happy"),
    SAD(3,"슬픔", "sad"),
    ANGER(1,"분노", "angry"),
    CALM(5,"평온", "calm");

    private final int score;
    private final String description;
    private final String value;

    public static Emotion fromString(String value){
        for (Emotion e : Emotion.values()){
            if (e.getValue().equalsIgnoreCase(value)){
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid emotion: " + value);
    }
}
