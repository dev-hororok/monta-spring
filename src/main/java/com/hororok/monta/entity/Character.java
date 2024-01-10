package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
public class Character extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Column(length=100)
    private String name;

    private String description;

    @NotBlank
    @Column(length=10)
    private String grade;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private int sellPrice;
}
