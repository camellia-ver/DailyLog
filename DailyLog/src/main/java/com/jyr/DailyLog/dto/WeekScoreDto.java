package com.jyr.DailyLog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeekScoreDto {
    private int year;
    private int week;
    private Double score;
}
