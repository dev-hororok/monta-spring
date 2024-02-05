package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`character`")
public class Character extends CommonEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "character_id")
    private UUID id;

    @NotNull
    @Column(length=100)
    private String name;

    private String description;

    @NotNull
    @Column(length=10)
    private String grade;

    @NotNull
    private String imageUrl;

    @NotNull
    private int sellPrice;

//    @OneToMany(mappedBy = "character")
//    private List<CharacterInventory> characterInventories = new ArrayList<>();
}
