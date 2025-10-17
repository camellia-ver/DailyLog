package com.jyr.DailyLog.controller;

import com.jyr.DailyLog.domain.Diary;
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

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    private final DiaryService diaryService;
    private final UserService userService;

    @GetMapping("/")
    public String dashboard(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails){
        LocalDate today = LocalDate.now();
        Long userId = userService.findUser(userDetails.getUsername()).getId();

        boolean isTodayDiary = diaryService.isTodayDiary(userId, today);
        model.addAttribute("isTodayDiary",isTodayDiary);

        DiaryResponseDto todayDiary = diaryService.findSavedDiary(userId, today);
        model.addAttribute("todayDiary", todayDiary);

        return "index";
    }
}
