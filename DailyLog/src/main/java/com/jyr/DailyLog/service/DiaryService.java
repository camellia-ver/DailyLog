package com.jyr.DailyLog.service;

import com.jyr.DailyLog.domain.Diary;
import com.jyr.DailyLog.domain.User;
import com.jyr.DailyLog.domain.enums.Emotion;
import com.jyr.DailyLog.dto.DiaryRequestDto;
import com.jyr.DailyLog.dto.DiaryResponseDto;
import com.jyr.DailyLog.exception.DiaryNotFoundException;
import com.jyr.DailyLog.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserService userService;

    public boolean isTodayDiary(Long userId, LocalDate today){
        return diaryRepository.existsByUserIdAndDate(userId, today);
    }

    public Diary createDiary(String email, DiaryRequestDto dto){
        User user = userService.findUser(email);

        String content = dto.getContent();
        if (content == null || content.isBlank()){
            throw new IllegalArgumentException("Diary content cannot be empty");
        }

        Diary diary = Diary.builder()
                .date(LocalDate.parse(dto.getToday()))
                .user(user)
                .emotion(Emotion.fromString(dto.getEmotion()))
                .content(content)
                .build();

        return diaryRepository.save(diary);
    }

    public DiaryResponseDto findSavedDiary(Long userId, LocalDate today){
        return diaryRepository.findByUserIdAndDate(userId, today)
                .map(diary -> new DiaryResponseDto(diary.getEmotion().getValue(), diary.getContent()))
                .orElseThrow(() -> new DiaryNotFoundException(today + "의 일기를 찾을 수 없습니다."));
    }

    public Diary updateDiary(String email, DiaryRequestDto dto){
        return null;
    }
}
