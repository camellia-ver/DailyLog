package com.jyr.DailyLog.service;

import com.jyr.DailyLog.domain.Diary;
import com.jyr.DailyLog.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final DiaryRepository diaryRepository;

    public List<Map<String, Object>> getLast7DaysEmotionScore(){
        LocalDate endDate = LocalDate.now();
        LocalDate sevenDaysAgo = endDate.minusDays(7);

        List<Diary> diaries = diaryRepository.findByDateBetween(sevenDaysAgo, endDate);

        Map<LocalDate, Integer> scoreMap = diaries.stream()
                .collect(Collectors.toMap(
                        Diary::getDate,
                        d -> d.getEmotion().getScore()
                ));

        List<Map<String, Object>> chartData = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = sevenDaysAgo.plusDays(i);
            Integer score = scoreMap.getOrDefault(date, null);

            Map<String, Object> data = new HashMap<>();
            data.put("date", date);
            data.put("score", score);
            chartData.add(data);
        }

        return chartData;
    }
}
