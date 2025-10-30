package com.jyr.DailyLog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthScoreDto {
    private String month;
    private Double score;
}
