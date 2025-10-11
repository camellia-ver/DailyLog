package com.jyr.DailyLog.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Emotion {
    HAPPY(7,"행복"),
    SAD(3,"슬픔"),
    ANGER(1,"분노"),
    CALM(5,"평온");

    private final int score;
    private final String description;
}
