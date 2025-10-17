package com.jyr.DailyLog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiaryResponseDto {
    private Long id;
    private String emotion;
    private String content;
}
