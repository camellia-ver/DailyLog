package com.jyr.DailyLog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StatsController {
    @GetMapping("/stats")
    public String diaryStats(Model model){
        return "stats";
    }
}
