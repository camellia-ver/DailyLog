package com.jyr.DailyLog.controller;

import com.jyr.DailyLog.domain.enums.Emotion;
import com.jyr.DailyLog.dto.DiaryResponseDto;
import com.jyr.DailyLog.service.DiaryService;
import com.jyr.DailyLog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Controller
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    private final UserService userService;

    @GetMapping("/diary")
    public String diary(Model model,
                        @AuthenticationPrincipal UserDetails userDetails){
        model.addAttribute("emotions", Emotion.values());

        LocalDate today = LocalDate.now();
        model.addAttribute("today",today.toString());

        Long userId = userService.findUser(userDetails.getUsername()).getId();
        boolean isWrite = !diaryService.isTodayDiary(userId, today);
        model.addAttribute("isWrite", isWrite);

        if (!isWrite){
            DiaryResponseDto savedDiary = diaryService.findSavedDiary(userId, today);
            model.addAttribute("savedDiary", savedDiary);
        }

        return "diary";
    }

    @GetMapping("/list")
    public String diaryList(Model model,
                            @AuthenticationPrincipal UserDetails userDetails,
                            @RequestParam(required = false) String date){
        Long userId = userService.findUser(userDetails.getUsername()).getId();

        LocalDate selectedDate;
        try{
            selectedDate = (date == null) ? LocalDate.now() : LocalDate.parse(date);
        }catch (DateTimeParseException e){
            selectedDate = LocalDate.now();
        }

        boolean isToday = LocalDate.now().equals(selectedDate);

        model.addAttribute("today", selectedDate.toString());
        model.addAttribute("isToday", isToday);

        DiaryResponseDto diary = diaryService.isTodayDiary(userId, selectedDate)
                ? diaryService.findSavedDiary(userId, selectedDate)
                : null;

        if (diary != null && diary.getEmotion() != null){
            model.addAttribute("diaryEmotion", Emotion.fromString(diary.getEmotion()));
        }
        model.addAttribute("diary", diary);

        return "list";
    }
}
