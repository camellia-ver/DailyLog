package com.jyr.DailyLog.dto;

import lombok.Data;

@Data
public class DiaryRequestDto {
    private Long id;
    private String today;
    private String emotion;
    private String content;
}
