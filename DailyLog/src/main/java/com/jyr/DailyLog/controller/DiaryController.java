package com.jyr.DailyLog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DiaryController {
    @GetMapping("/diary")
    public String diary(){
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
