package com.jyr.DailyLog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateScoreDto {
    private LocalDate date;
    private Integer score;
}
