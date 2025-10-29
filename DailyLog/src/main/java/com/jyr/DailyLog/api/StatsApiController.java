package com.jyr.DailyLog.api;

import com.jyr.DailyLog.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatsApiController {
    private final StatsService statsService;

    @GetMapping("/seven-days-emotion-score")
    public ResponseEntity<List<Map<String, Object>>> getEmotionScore(){
        List<Map<String, Object>> emotionScores = statsService.getLast7DaysEmotionScore();
        return ResponseEntity.ok(emotionScores);
    }
}
