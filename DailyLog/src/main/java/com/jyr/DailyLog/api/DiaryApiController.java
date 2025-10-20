package com.jyr.DailyLog.api;

import com.jyr.DailyLog.domain.Diary;
import com.jyr.DailyLog.dto.DiaryRequestDto;
import com.jyr.DailyLog.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

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
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDiary.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .build();
    }

    @PutMapping
    public ResponseEntity<Void> updateDiary(
            @RequestBody DiaryRequestDto dto
    ){
        diaryService.updateDiary(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long id){
        diaryService.deleteDiary(id);
        return ResponseEntity.noContent().build();
    }
}
