package com.jyr.DailyLog.controller;

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

@Controller
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    private final UserService userService;

    @GetMapping("/diary")
    public String diary(Model model,
                        @AuthenticationPrincipal UserDetails userDetails){
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

        LocalDate selectedDate = (date == null) ? LocalDate.now() : LocalDate.parse(date);
        model.addAttribute("today", selectedDate.toString());

        DiaryResponseDto diary = diaryService.isTodayDiary(userId, selectedDate)
                ? diaryService.findSavedDiary(userId, selectedDate)
                : null;

        model.addAttribute("diary", diary);

        return "list";
    }

    @GetMapping("stats")
    public String diaryStats(){
        return "stats";
    }
}
