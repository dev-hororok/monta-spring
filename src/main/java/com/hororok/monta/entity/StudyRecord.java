package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE study_record SET deleted_at = CURRENT_TIMESTAMP WHERE study_record_id = ?")
@Where(clause = "deleted_at IS NULL")
public class StudyRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_record_id")
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 20)
    private String status;   // Completed, Incompleted

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
