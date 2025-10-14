package com.jyr.DailyLog.service;

import com.jyr.DailyLog.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public boolean isTodayDiary(Long userId, LocalDate today){
        return diaryRepository.existsByUserIdAndDate(userId, today);
    }
}
