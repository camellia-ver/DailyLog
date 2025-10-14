package com.jyr.DailyLog.controller;

import com.jyr.DailyLog.service.DiaryService;
import com.jyr.DailyLog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

        return "diary";
    }

    @GetMapping("/list")
    public String diaryList(){
        return "list";
    }

    @GetMapping("stats")
    public String diaryStats(){
        return "stats";
    }
}
