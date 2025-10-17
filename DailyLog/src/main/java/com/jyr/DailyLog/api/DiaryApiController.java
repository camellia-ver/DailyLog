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

        return ResponseEntity
                .created(createLocation(savedDiary.getId()))
                .build();
    }

    @PutMapping
    public ResponseEntity<Void> updateDiary(
            @RequestBody DiaryRequestDto dto
    ){
        Diary updatedDiary = diaryService.updateDiary(dto);

        return ResponseEntity
                .created(createLocation(updatedDiary.getId()))
                .build();
    }

    private URI createLocation(Long id){
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
