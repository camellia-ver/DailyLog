package com.jyr.DailyLog.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaryWriteRequestDto {
    private String today;
    private String emotion;
    private String content;
}
