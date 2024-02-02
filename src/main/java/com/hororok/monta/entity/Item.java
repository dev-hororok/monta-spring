package com.hororok.monta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "`item`")
public class Item extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long id;

    @NotNull
    @Column(length = 20)
    private String itemType;

    @NotNull
    @Column(length = 100)
    private String name;

    @Column(length = 20)
    private String grade;

    @NotNull
    private String description;

    @NotNull
    private String imageUrl;

    @NotNull
    private int cost;

    private Integer requiredStudyTime;

    @NotNull
    private int effectCode;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemInventory> itemInventories = new ArrayList<>();

}
