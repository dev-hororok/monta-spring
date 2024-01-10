package com.hororok.monta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class CommonEntity {

    @NotBlank
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;

    @NotBlank
    @LastModifiedDate
    private LocalDateTime updated_at;


}
