package com.jyr.DailyLog.domain;

import lombok.Getter;

@Getter
public enum EmotionLevel {
    VERY_NEGATIVE(-2, "매우 부정적"),
    NEGATIVE(-1, "부정적"),
    NEUTRAL(0, "중립적"),
    POSITIVE(1, "긍정적"),
    VERY_POSITIVE(2, "매우 긍정적");

    private final int score;
    private final String description;

    EmotionLevel(int score, String description){
        this.score = score;
        this.description = description;
    }
}
