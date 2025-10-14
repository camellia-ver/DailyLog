package com.jyr.DailyLog.dto;

import lombok.Data;

@Data
public class DiaryWriteRequestDto {
    private String emotion;
    private String content;
}
