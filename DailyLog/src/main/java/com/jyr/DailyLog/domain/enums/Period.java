package com.jyr.DailyLog.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Period {
    ONE_MONTH("1", 1),
    THREE_MONTH("3", 3),
    SIX_MONTH("6", 6);

    private final String code;
    private final int months;

    public static Period fromCode(String code){
        for (Period p : values()){
            if (p.code.equals(code)) return p;
        }
        throw new IllegalArgumentException("Invalid period code: " + code);
    }
}
