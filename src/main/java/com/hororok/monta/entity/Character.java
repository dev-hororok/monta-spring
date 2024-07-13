package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`character`")
@SQLDelete(sql = "UPDATE `character` SET deleted_at = CURRENT_TIMESTAMP WHERE character_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Character extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private int id;

    @NotNull
    @Column(length=100)
    private String name;

    private String description;
    private String acquisitionSource;

    @NotNull
    @Column(length=10)
    private String grade;

    @NotNull
    private String imageUrl;

    @NotNull
    private int sellPrice;

    @OneToMany(mappedBy = "character")
    private List<CharacterInventory> characterInventories = new ArrayList<>();

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MemberCharacterCollection> memberCharacterCollections = new ArrayList<>();
}
