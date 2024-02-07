package com.hororok.monta.repository;

import com.hororok.monta.entity.StudyStreak;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface StudyStreakRepository extends JpaRepository<StudyStreak, Long> {
    Optional<StudyStreak> findByMemberId(UUID memberId);
}
