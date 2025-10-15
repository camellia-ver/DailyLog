package com.jyr.DailyLog.repository;

import com.jyr.DailyLog.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    boolean existsByUserIdAndDate(Long userId, LocalDate date);
    Optional<Diary> findByUserIdAndDate(Long userId, LocalDate date);
}
