package com.jyr.DailyLog.api;

import com.jyr.DailyLog.dto.DateScoreDto;
import com.jyr.DailyLog.dto.StatsSummaryDto;
import com.jyr.DailyLog.service.StatsService;
import com.jyr.DailyLog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatsApiController {
    private final StatsService statsService;
    private final UserService userService;

    @GetMapping("/seven-days-emotion-score")
    public ResponseEntity<List<DateScoreDto>> getSevenDayEmotionScore(@AuthenticationPrincipal UserDetails userDetails){
        Long userId = getUserId(userDetails);

        List<DateScoreDto> emotionScores = statsService.getLast7DaysEmotionScore(userId);

        if (emotionScores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(emotionScores);
    }

    @GetMapping("/emotion-ratio")
    public ResponseEntity<Map<String, Integer>> getEmotionRatio(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "period") String period){
        Long userId = getUserId(userDetails);

        if (!List.of("1", "3", "6").contains(period.toLowerCase())) {
            return ResponseEntity.badRequest().body(Map.of("error", 0));
        }

        Map<String, Integer> emotionRatioData = statsService.getEmotionRatio(userId, period);
        return ResponseEntity.ok(emotionRatioData);
    }

    @GetMapping("/emotion-trend")
    public ResponseEntity<List<?>> getEmotionScoreAvgTrend(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "period") String period){
        Long userId = getUserId(userDetails);

        List<?> trendData = statsService.getEmotionScoreAvgTrend(userId, period);

        if (trendData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(trendData);
    }

    @GetMapping("/summary")
    public ResponseEntity<StatsSummaryDto> getStatsSummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "period") String period
    ){
        Long userId = getUserId(userDetails);

        double avgEmotionScore = statsService.calcAvgEmotionScore(userId, period);
        double positiveRatio = statsService.calcPositiveRatio(userId, period) * 100;

        return ResponseEntity.ok(new StatsSummaryDto(avgEmotionScore, positiveRatio));
    }

    private Long getUserId(UserDetails userDetails){
        return userService.findUser(userDetails.getUsername())
                .getId();
    }
}
