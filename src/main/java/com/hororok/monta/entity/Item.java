package com.hororok.monta.entity;

import com.hororok.monta.dto.request.item.PostItemRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

    @NotNull
    private boolean isHidden;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemInventory> itemInventories = new ArrayList<>();

    public Item(PostItemRequestDto requestDto) {
        itemType = requestDto.getItemType();
        name = requestDto.getName();
        grade = requestDto.getGrade();
        description = requestDto.getDescription();
        imageUrl = requestDto.getImageUrl();
        cost = requestDto.getCost();
        requiredStudyTime = requestDto.getRequiredStudyTime();
        effectCode = requestDto.getEffectCode();
        isHidden = requestDto.isHidden();
    }
}
