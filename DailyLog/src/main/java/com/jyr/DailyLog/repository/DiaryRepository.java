package com.jyr.DailyLog.repository;

import com.jyr.DailyLog.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    boolean existsByUserIdAndDate(Long userId, LocalDate date);
}
