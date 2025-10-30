package com.jyr.DailyLog.service;

import com.jyr.DailyLog.domain.Diary;
import com.jyr.DailyLog.domain.enums.Emotion;
import com.jyr.DailyLog.domain.enums.EmotionType;
import com.jyr.DailyLog.domain.enums.Period;
import com.jyr.DailyLog.dto.DateScoreDto;
import com.jyr.DailyLog.dto.MonthScoreDto;
import com.jyr.DailyLog.dto.WeekScoreDto;
import com.jyr.DailyLog.repository.DiaryRepository;
import com.jyr.DailyLog.util.YearWeek;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import javax.annotation.processing.Completion;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final DiaryRepository diaryRepository;

    public List<DateScoreDto> getLast7DaysEmotionScore(Long userId){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        List<Diary> diaries = diaryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        Map<LocalDate, Integer> scoreMap = diaries.stream()
                .filter(d -> d.getEmotion() != null)
                .collect(Collectors.toMap(
                        Diary::getDate,
                        d -> d.getEmotion().getScore(),
                        (existing, replacement) -> replacement
                ));

        List<DateScoreDto> result = IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = startDate.plusDays(i);
                    return new DateScoreDto(date, scoreMap.get(date));
                })
                .collect(Collectors.toList());

        return result;
    }

    public Map<String, Integer> getEmotionRatio(Long userId, String period){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calcStartDate(endDate, period);

        List<Diary> diaries = diaryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        return diaries.stream()
                .collect(Collectors.groupingBy(
                        d -> d.getEmotion().getDescription(),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    public List<?> getEmotionScoreAvgTrend(Long userId, String period){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calcStartDate(endDate, period);

        List<Diary> diaries = diaryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        if (period.equals("1")) {
            return diaries.stream()
                    .collect(Collectors.groupingBy(Diary::getDate,
                            Collectors.averagingInt(d -> d.getEmotion().getScore())))
                    .entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> new DateScoreDto(e.getKey(), e.getValue().intValue()))
                    .collect(Collectors.toList());
        }else if(period.equals("3")){
            WeekFields weekFields = WeekFields.of(Locale.getDefault());

            Map<YearWeek, Double> grouped = diaries.stream()
                    .collect(Collectors.groupingBy(
                            d -> {
                                int week = d.getDate().get(weekFields.weekOfWeekBasedYear());
                                int year = d.getDate().get(weekFields.weekBasedYear());
                                return new YearWeek(year, week);
                            },
                            Collectors.averagingInt(d -> d.getEmotion().getScore())
                    ));

            return grouped.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> new WeekScoreDto(e.getKey().year(), e.getKey().week(), e.getValue()))
                    .collect(Collectors.toList());
        }else{
            Map<YearMonth, Double> grouped = diaries.stream()
                    .collect(Collectors.groupingBy(d -> YearMonth.from(d.getDate()),
                            Collectors.averagingInt(d -> d.getEmotion().getScore())));


            return grouped.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> new MonthScoreDto(e.getKey().toString(), e.getValue()))
                    .collect(Collectors.toList());
        }
    }

    public double calcAvgEmotionScore(Long userId, String period){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calcStartDate(endDate, period);

        List<Diary> diaries = diaryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        return diaries.stream()
                .map(Diary::getEmotion)
                .filter(Objects::nonNull)
                .mapToInt(Emotion::getScore)
                .average()
                .orElse(0);
    }

    public double calcPositiveRatio(Long userId, String period){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calcStartDate(endDate, period);

        List<Diary> diaries = diaryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        Map<EmotionType, Long> groupedCount = diaries.stream()
                .map(Diary::getEmotion)
                .filter(Objects::nonNull)
                .map(
                        e -> {
                            String desc = e.getDescription();
                            return ("행복".equals(desc) || "평온".equals(desc)) ? EmotionType.POSITIVE : EmotionType.NEGATIVE;
                        })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long positive = groupedCount.getOrDefault(EmotionType.POSITIVE, 0L);
        long negative = groupedCount.getOrDefault(EmotionType.NEGATIVE, 0L);
        long total = positive + negative;

        return total == 0 ? 0 : (double) positive / total;
    }

    private LocalDate calcStartDate(LocalDate endDate, String periodCode){
        Period period = Period.fromCode(periodCode);
        return endDate.minusMonths(period.getMonths());
    }
}
