package com.jyr.DailyLog.domain;

import com.jyr.DailyLog.domain.enums.Emotion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "diary",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_date", columnNames = {"user_id", "date"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Builder
    public Diary(User user, LocalDate date, String content, Emotion emotion){
        this.user = user;
        this.date = date;
        this.content = content;
        this.emotion = emotion;
    }

    public void update(String content, Emotion emotion){
        if (content != null && !content.isBlank()){
            this.content = content;
        }
        if (emotion != null){
            this.emotion = emotion;
        }
    }
}
