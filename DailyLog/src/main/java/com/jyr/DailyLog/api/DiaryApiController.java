package com.jyr.DailyLog.api;

import com.jyr.DailyLog.domain.Diary;
import com.jyr.DailyLog.dto.DiaryRequestDto;
import com.jyr.DailyLog.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryApiController {
    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Void> createDiary(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DiaryRequestDto dto){
        Diary savedDiary = diaryService.createDiary(userDetails.getUsername(), dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDiary.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }
}
