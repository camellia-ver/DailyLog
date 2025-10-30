package com.jyr.DailyLog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsSummaryDto {
    private final double avg;
    private final double ratio;
}
